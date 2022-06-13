package demo

import scala.collection.immutable

object TemplateDemo {
  def sum(collections: Iterable[Iterable[_]]): Int = {
    collections.map(_.size).sum
  }
  def main(args: Array[String]): Unit = {

    sum(List(Set(1,2), List(3,4)))
    val d1 = Set(List(3,4),Set(1,2))
    sum(d1)
    val d2 = d1.map(_.size)
    println(d2)
  }

}
