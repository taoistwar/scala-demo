//package app.apkserver
//
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
//import com.fasterxml.jackson.annotation.JsonInclude.Include
//import com.fasterxml.jackson.annotation.{JsonInclude, PropertyAccessor}
//import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
//import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
//import com.google.gson.Gson
//import elt.csv.{CsvSplitConfig, CsvSplitProcess}
//import org.apache.commons.csv.CSVFormat
//import org.apache.http.HttpHost
//import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
//import org.apache.http.entity.ContentType
//import org.apache.http.impl.client.BasicCredentialsProvider
//import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
//import org.apache.http.nio.entity.NStringEntity
//import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback
//import org.elasticsearch.client.{Request, RestClient}
//import org.joda.time.DateTime
//
//import java.io.{FileReader, FileWriter}
//import java.text.SimpleDateFormat
//import java.util.TimeZone
//import scala.collection.JavaConverters._
//import scala.io.Source
//
//case class Record(idc: String,
//log_time: String,
//md5: String,
//paas_msg: String,
//packageName: String)
//
//object ApkServerApp {
//  final lazy val mapper = {
//    val _mapper = new ObjectMapper() with ScalaObjectMapper /*with CaseClassObjectMapper*/
//    _mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY)
//    _mapper.registerModule(DefaultScalaModule)
//    /*_mapper.setAllCaseClassEnabled(true)*/
//    _mapper.setSerializationInclusion(Include.NON_ABSENT)
//    _mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//    _mapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true)
//    _mapper.configure(SerializationFeature.INDENT_OUTPUT, false)
//    _mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//    _mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//    _mapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//    _mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//    _mapper
//  }
//
//
//  val credentialsProvider =
//    new BasicCredentialsProvider();
//  credentialsProvider.setCredentials(AuthScope.ANY,
//    new UsernamePasswordCredentials("elastic", "elastic"));
//
//  def restClient = RestClient.builder(
//    new HttpHost("10.201.10.115", 9200, "http"))
//    .setHttpClientConfigCallback(new HttpClientConfigCallback() {
//      @Override
//      override def customizeHttpClient(
//         httpClientBuilder:HttpAsyncClientBuilder): HttpAsyncClientBuilder= {
//        return httpClientBuilder
//          .setDefaultCredentialsProvider(credentialsProvider);
//      }
//    })
//    .build();
//  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm")
//
//  def main(args: Array[String]): Unit = {
//    val data1 = Source.fromFile("d:\\data\\55.csv").getLines().toList.map(parse)
////    val fileWriter = new FileWriter("d:\\data\\stats-1.csv")
////    fileWriter.write("$idc, $time, $downing, $downStart, $downComp, $extracting, $extractStart, $extractComp, $packagesStr, $packagesCount\n")
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
//        val packagesStr = packages.mkString("|")
//        val packagesCount = packages.size
//        val idc = allTimeData.head.idc
//        def writeToES() = {
//          val date = sdf.parse(time)
//          val id = s"${idc}_${date.getTime}"
//          val request = new Request("POST", "/apk-server-case-1/_doc/" + id);
//          val map = Map[String,Any] ("idc"->idc, "log_time"-> date, "downing"->downing,
//            "downStart"->downStart, "downComp"->downComp, "extracting" -> extracting,
//            "extractStart"->extractStart,
//          "extractComp" -> extractComp, "packagesStr"->packagesStr, "packagesCount"->packagesCount)
//          val json = mapper.writeValueAsString(map)
//          request.setEntity(new NStringEntity(json,             ContentType.APPLICATION_JSON));
//          restClient.performRequest(request)
//          request
//        }
//        writeToES()
////        val format = s"""$idc, $time, $downing, $downStart, $downComp, $extracting, $extractStart, $extractComp, "$packagesStr", $packagesCount"""
////        fileWriter.write(format+"\n")
//      })
//    })
//  }
//
//
//  def getPackages(all: List[Record]) = {
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
//        ""
//      } else {
//        s"${packageName} extracting:${extracting} downing:$downing "
//      }
//    }).filter(_ != "").toList
//
//  }
//
//
//  def parse(line: String):Record ={
//    val  items = line.split(",").map(item=>  {
//      item.substring(1, item.length-1)
//    })
//    val date = new DateTime(items(1)).toDate
//    val str = sdf.format(date)
//    Record(items(0), str, items(2), items(3), items(4))
//  }
//
//}
