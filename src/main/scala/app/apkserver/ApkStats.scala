//package app.apkserver
//
//import app.apkserver.ApkServerApp.{mapper, parse, restClient, sdf}
//import org.apache.http.entity.ContentType
//import org.apache.http.nio.entity.NStringEntity
//import org.elasticsearch.client.Request
//import org.joda.time.DateTime
//
//import java.io.FileWriter
//import scala.io.Source
//
//case class Merge(idc: String,
//                 log_time: String,
//                 md5: String,
//                 paas_msg: String,
//                 packageName: String,
//                 packageSize: Int)
//object ApkStats  {
//  def parse(line: String)= {
//    val  items = line.split(",")
//    val packageSize = if (items.length > 5) {
//      items(5).trim
//    } else {
//      ""
//    }
//    val size = if (packageSize != null && packageSize != "") {
//      packageSize.toInt
//    } else {
//      0
//    }
//    try {
//      Merge(items(0).trim, items(1).trim, items(2).trim, items(3).trim, items(4).trim, size)
//    } catch {
//      case e: Exception=>
//        println(s"raw:${line}")
//        throw e
//    }
//  }
//
//  def main(args: Array[String]): Unit = {
//    val data1 = Source.fromFile("d:\\data\\merge-27.csv").getLines().toList.filter(!_.contains(" log_time")).map(parse)
//    val fileWriter = new FileWriter("d:\\data\\stats-2.csv")
//    fileWriter.write("$idc, $time, $downing, $downStart, $downComp, $extracting, $extractStart, $extractComp, $extractingTotal, $downingTotal, $packagesStr, $packagesCount\n")
//
//    data1.groupBy(_.idc).foreach(data=> {
//      val allIdcData = data._2
//      val times = allIdcData.map(_.log_time).distinct.sorted
//      times.foreach(time=> {
//        val allTimeData = allIdcData.filter(_.log_time <= time )
//        val downStart = allTimeData.count(_.paas_msg == "start hotUpdateDownload by md5")
//        val downComp = allTimeData.count(_.paas_msg == "sync to other APK complete")
//        val extractStart = allTimeData.count(_.paas_msg == "start hot update extract by md5")
//        val extractComp = allTimeData.count(_.paas_msg == "extract file complete")
//        var downing = downStart - downComp
//        if (downing < 0) {
//          downing = 0
//        }
//        var extracting = extractStart - extractComp
//        if (extracting<0) {
//          extracting = 0
//        }
//        val packages = getPackages(allTimeData)
//        val packagesStr = packages.map(item=>s"${item.packageName} downing:${item.downing} extracting:${item.extracting}").mkString("|")
//        val packagesCount = packages.size
//
//        val downingTotal = packages.filter(_.downing>0).map(item=> item.downing*item.fileSize).sum
//        val extractingTotal = packages.filter(_.extracting>0).map(item=>item.extracting*item.fileSize).sum
//
//        val idc = allTimeData.head.idc
//
//        val format = s"""$idc, $time, $downing, $downStart, $downComp, $extracting, $extractStart, $extractComp, $extractingTotal, $downingTotal, "$packagesStr", $packagesCount"""
//        fileWriter.write(format+"\n")
//      })
//    })
//  }
//
//
//  def getPackages(all: List[Merge]) = {
//    all.groupBy(_.packageName).map(item=> {
//      val packageName = item._1
//      val list = item._2
//      val downStart = list.count(_.paas_msg == "start hotUpdateDownload by md5")
//      val downComp = list.count(_.paas_msg == "sync to other APK complete")
//      val extractStart = list.count(_.paas_msg == "start hot update extract by md5")
//      val extractComp = list.count(_.paas_msg == "extract file complete")
//      var downing = downStart - downComp
//      if (downing < 0) {
//        downing = 0
//      }
//      var extracting = extractStart - extractComp
//      if (extracting<0) {
//        extracting = 0
//      }
//      if (extracting < 1 & downing < 1) {
//        None
//      } else {
//        Some(PackageStats(packageName, extracting, downing, list.head.packageSize))
//      }
//
//    }).filter(_.nonEmpty).map(_.get).toList
//
//  }
//}
//
//case class PackageStats(packageName: String, extracting:Int, downing: Int, fileSize:Int)