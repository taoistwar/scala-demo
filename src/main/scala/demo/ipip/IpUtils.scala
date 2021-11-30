package demo.ipip


import net.ipip.ipdb.{City, MetaData}

case class Mo(opId: Int)
object IpUtils {
  def main(args: Array[String]): Unit = {
//    val path = "/Users/taoistwar/Workspaces/java/sage-bigdata-zeta/common/src/main/resources/ip_location.ipdb"
    val path = "/Users/taoistwar/Downloads/ipv4_china_line_cn.ipdb";
    val city = new City(path)
    val res = city.find("108.160.162.102", "CN")
    res.toList.foreach(println)


  }
}
