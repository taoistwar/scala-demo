package app.review

import app.review.ReviewApp.{frame, mapper, tree, v1, v2}
import app.review.vo.Node
import app.utils.IoUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import com.github.difflib.DiffUtils
import com.github.difflib.patch.DeltaType

import java.awt.{BorderLayout, Frame}
import javax.swing.event.{TreeSelectionEvent, TreeSelectionListener}
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.{JDesktopPane, JFrame, JPanel, JScrollPane, JSplitPane, JTextArea, JTree}
import scala.collection.JavaConverters.{asScalaBufferConverter, seqAsJavaListConverter}
import scala.collection.mutable.ListBuffer

trait ReviewHelper {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  val v1Text = IoUtils.text("old.json")
  val v2Text = IoUtils.text("new.json")
  val v1 = mapper.readValue[Map[String, Any]](v1Text)
  val v2 = mapper.readValue[Map[String, Any]](v2Text)

  val root = compareFields()
  val tree = genTreeRoot()
  val oldTa = new JTextArea(20, 30)
  val oldSp = new JScrollPane(oldTa)
  val newsTa = new JTextArea(20, 30)
  val newsSp = new JScrollPane(newsTa)

  val frame = {
    val frame = new JFrame("Review")
    val container = frame.getContentPane
    //    compareContainer.add()
    container.setLayout(new BorderLayout())
    container.add(tree, BorderLayout.WEST)
    tree.setSize(500, 1000)

    container.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, oldSp, newsSp), BorderLayout.CENTER)

    frame.setSize(800, 600)
    //    frame.setUndecorated(true); //去处边框
    frame.setExtendedState(Frame.MAXIMIZED_BOTH); //最大化
    frame.setAlwaysOnTop(false); //总在最前面
    frame.setResizable(false); //不能改变大小
    frame
  }

  def writeValueAsString(x: Any): String = {
    x match {
      case x:String=>
        x
      case _=>
        var tmp = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(x)
        tmp = tmp.replace("\\n", "\n")
        tmp
    }

  }

  def compareItem(parentNode: DefaultMutableTreeNode, name: String, d1: Any, d2: Any): Unit = {
    d1 match {
      case x: Seq[_] =>
        d2 match {
          case y: Seq[_] =>

            x.foreach(item => {
              println(x, y)
            })
          case _ =>
            val node = Node(name, writeValueAsString(d1), writeValueAsString(d2))
            parentNode.add(new DefaultMutableTreeNode(node))
        }
      case x: Map[String, _] =>
        d2 match {
          case y: Map[String, _] =>
            x.foreach(entry => {
              val key = entry._1
              val e1 = entry._2
              val e2 = y.getOrElse(entry._1, null)
              if (e1 != e2) {
                compareItem(parentNode, key, e1, e2)
              }
            })
          case _ =>
            val node = Node(name, writeValueAsString(d1), writeValueAsString(d2))
            parentNode.add(new DefaultMutableTreeNode(node))
        }
      case _ =>
        val node = Node(name, writeValueAsString(d1), writeValueAsString(d2))
        parentNode.add(new DefaultMutableTreeNode(node))
    }
  }

  def compareNodes(parentNode: DefaultMutableTreeNode, d1: Seq[Any], d2: Seq[Any]): Unit = {
    for (i <- d1.indices) {
      val item = d1(i)
      val node = item.asInstanceOf[Map[String, Any]]
      val oldId = node.getOrElse("id", "")
      val newsNode = d2.find(tmp => {
        val newsItem = tmp.asInstanceOf[Map[String, Any]]
        val newsId = newsItem.getOrElse("id", null)
        oldId == newsId
      }).orNull
      if (newsNode == null) {
        parentNode.add(new DefaultMutableTreeNode(Node(s"${i}", writeValueAsString(node), "")))
      } else {
         var isAdd = false
        if (node != newsNode) {
          val curNode = new DefaultMutableTreeNode(Node(s"${i}", writeValueAsString(node), writeValueAsString(newsNode)))
          node.foreach(x => {
            if (x._1 != "id") {
              val key = x._1
              val e1 = x._2
              val e2 = newsNode.asInstanceOf[Map[String, _]].getOrElse(x._1, null)
              if (e1 != e2) {
                if (!isAdd) {
                  isAdd = true
                  parentNode.add(curNode)
                }
                compareItem(curNode, key, e1, e2)
              }
            }
          })
        }
      }
    }
    for (i <- d2.indices) {
      val item = d2(i)
      val node = item.asInstanceOf[Map[String, Any]]
      val name = node.getOrElse("id", "")
      val newsNode = d1.find(tmp => {
        val newsItem = tmp.asInstanceOf[Map[String, Any]]
        val newsName = newsItem.getOrElse("id", null)
        name == newsName
      })
      if (newsNode.isEmpty) {
        parentNode.add(new DefaultMutableTreeNode(Node(name.toString, "", writeValueAsString(node))))
      }
    }
  }

  def compareFields(): DefaultMutableTreeNode = {
    val root = new DefaultMutableTreeNode("root")
    v1.foreach(entry => {
      val d1 = entry._2
      val d2 = v2.getOrElse(entry._1, null)
      val key = entry._1

      if (d1 != d2) {
        if ("nodes" == key) {
          val currentNode = new DefaultMutableTreeNode("nodes")
          root.add(currentNode)
          compareNodes(currentNode, d1.asInstanceOf[Seq[Any]], d2.asInstanceOf[Seq[Any]])
        } else {
          compareItem(root, entry._1, d1, d2)
        }
      }
    })
    root
  }


  def launch(): Unit = {
    frame.setVisible(true)
  }

  def genTreeRoot(): JTree = {
    val tree = new JTree(root)
    tree.addTreeSelectionListener(new TreeSelectionListener {
      override def valueChanged(e: TreeSelectionEvent): Unit = {
        println(e.getPath)
        println(e.toString)
        val node = tree.getLastSelectedPathComponent.asInstanceOf[DefaultMutableTreeNode]

        if (node == null) {
          return
        }

        val obj = node.getUserObject
        if (node.isLeaf) {
          val user = obj.asInstanceOf[Node]
          oldTa.setText(user.old)
          newsTa.setText(user.news)
          val oldList= user.old.split("\n").toList.asJava
          val newsList =  user.news.split("\n").toList.asJava
          val deltas = DiffUtils.diff(oldList, newsList)

          deltas.getDeltas.asScala.foreach(delta=> {
            delta.getType match {
              case DeltaType.INSERT=>
                import com.github.difflib.patch.Chunk
                val insert: Chunk[String] = delta.getTarget
                val position: Int = insert.getPosition
                System.out.println("+ " + (position + 1) + " " + insert.getLines)

              case DeltaType.CHANGE=>
                import com.github.difflib.patch.Chunk
                val source: Chunk[String] = delta.getSource
                val target1: Chunk[String] = delta.getTarget
                System.out.println("\n- " + (source.getPosition + 1) + " " + source.getLines + "\n+ " + "" + (target1.getPosition + 1) + " " + target1.getLines)

              case DeltaType.DELETE=>
                import com.github.difflib.patch.Chunk
                //删除
                val delete: Chunk[String] = delta.getSource
                System.out.println("- " + (delete.getPosition + 1) + " " + delete.getLines)

              case DeltaType.EQUAL=>
                val equal = delta.getSource
                val lines = equal.getLines
                val position = equal.getPosition

            }
          })
          System.out.println("你选择了：" + user.toString)
        }
      }
    })
    tree
  }
}
