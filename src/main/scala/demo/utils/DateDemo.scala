package demo.utils

import java.util.Calendar

object DateDemo {
  def main(args: Array[String]): Unit = {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, 1960)
    println(calendar.getTime.getTime)
  }
}
