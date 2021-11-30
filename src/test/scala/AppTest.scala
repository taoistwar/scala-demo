object AppTest {
  def main(args: Array[String]): Unit = {
    val data = Set("a", "b", "c")
    data.zipWithIndex.foreach(println)
    data.zip(Stream from 1).foreach(x => println(x._1, x._2))
    val map = System.getenv()
    println(map)
  }
}
