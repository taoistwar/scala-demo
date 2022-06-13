//package app.apkserver
//
//import app.apkserver.ApkServerApp.{parse, sdf}
//import org.joda.time.DateTime
//
//import java.io.FileWriter
//import scala.io.Source
//
//case class PackageSize(md5: String, packageName: String, fileSizeMB: String, fileSizeByte: String)
//
//object ApkMerge {
//  def main(args: Array[String]): Unit = {
//    val data1 = Source.fromFile("d:\\data\\27号整天下载解压.csv").getLines().toList.map(parse)
//    val data2  = Source.fromFile("d:\\data\\包大小.csv").getLines().toList.map(parsePackageSize)
//    val fileWriter = new FileWriter("d:\\data\\merge-27.csv")
//    fileWriter.write("idc, log_time, md5, paas_msg, packageName, fileSizeMB, fileSizeByte\n")
//
//    data1.foreach(item=> {
//       val format = data2.find(d2=> d2.md5 == item.md5) match {
//         case Some(value) =>
//           s"${item.idc}, ${item.log_time}, ${item.md5}, ${item.paas_msg}, ${item.packageName}, ${value.fileSizeMB}, ${value.fileSizeByte}"
//         case None=>
//           s"${item.idc}, ${item.log_time}, ${item.md5}, ${item.paas_msg}, ${item.packageName}, , "
//       }
//      fileWriter.write(format+"\n")
//
//    })
//    fileWriter.flush();
//    fileWriter.close()
//  }
//  def parsePackageSize(line: String)= {
//    val  items = line.split(",").map(item=>  {
//      if (item.startsWith("\"")) {
//        item.substring(1, item.length-1)
//      } else {
//        item
//      }
//    })
//    PackageSize(items(0), items(1), items(2), items(3))
//  }
//}
