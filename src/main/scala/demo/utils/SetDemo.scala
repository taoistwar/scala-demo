package demo.utils

import java.util
import scala.collection.mutable

class SetDemo
object SetDemo {
  def main(args: Array[String]): Unit = {
    println(Set(1,2,3) -- Set(2,4))
    println( Set(1,2,3) &~ Set(2,4) )
    println(Set(1,2,3) diff Set(2,4))

    val map1  = new util.HashMap[String, String]()
    val map2 = mutable.HashMap()
    classOf[SetDemo].getClassLoader;
    ClassLoader.getPlatformClassLoader
  }
}
