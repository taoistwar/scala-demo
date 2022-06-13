package demo

trait  A {
  val name: String
  println(s"Hello  $name")
}
class Cat(var raw:String) extends A {
  val name = raw
  println(s"Cat: Hello $name")
}
class Dog(val name:String) extends A {
  println(s"Dog: Hello $name")
}

object ScalaDemo {
  def main(args: Array[String]): Unit = {
    new Cat("cat")
    new Dog("dog")
  }
}
