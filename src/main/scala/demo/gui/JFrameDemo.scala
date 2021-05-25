package demo.gui

import java.awt.Color
import javax.swing.JFrame

object JFrameDemo {
  def main(args: Array[String]): Unit = {
    val frame = new JFrame()
    frame.setTitle("xx")
    frame.setSize(800,200)
//    frame.pack()
    frame.setBackground(Color.WHITE)
    frame.setVisible(true)

  }
}
