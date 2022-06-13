package demo.pattern

class T1

class T2(val x: String) extends T1
case class T3(x: String) extends T1

object PatternMatchDemo extends App {
  val obj1: T1 = new T2("xx")
  val obj2 = obj1 match {
    case T3(x)=>
      x
    case t: T2=>
      t.x
  }
  println(obj2)
}
