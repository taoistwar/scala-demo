package app.saas

import app.saas.EventFinderApp.dic
import org.jsoup.Jsoup

import java.util.regex.Pattern
import scala.collection.mutable.ListBuffer
import scala.io.Source

object RegexApp {
  def main(args: Array[String]): Unit = {
    val html = Source.fromURL(getClass.getClassLoader.getResource("test.html")).mkString
    val doc = Jsoup.parse(html)
    val tbody =   doc.getElementById("sdk111")
    val trs = tbody.getElementsByTag("tr")
    val iterator =trs.iterator();
    val list = ListBuffer[(String, String)]()
    while (iterator.hasNext) {
      val tr =iterator.next()
      val tds = tr.getElementsByTag("td")
      val iter2 = tds.iterator()
      var i =0
      var id: String=""
      var name : String = ""
      while(iter2.hasNext) {
        val td = iter2.next()
        if (i == 0) {
          id =td.text()
          i+=1
        } else if (i == 1) {
          name = td.text()
          i+=1
        }

      }
      if (id.nonEmpty) {
        list.append((id, name))
        println(id, name)
      }
    }
    val ids = list.map(_._1)
    val dictIds = dic.split("\n").filter(_.nonEmpty).toSet
    val need = ids -- (dictIds)
    println(need.toList.mkString(", "))

    need.foreach(id=>{
      val name = list.find(_._1==id) match {
        case Some(x)=> x._2
        case _=> "xxx"
      }
      println(
        s"""
           |INSERT INTO `t_dict_meta` (`module`, `dic_id`, `dic_name`, `dic_desc`, `type`) VALUES ('SDK', '${id}', '${name}', '', 'EventID');
           |""".stripMargin.trim)
    })

  }
}
