//package app.es.index.audit
//
//import app.utils.{JsonMapperUtils, MathUtils}
//import com.fasterxml.jackson.annotation.JsonProperty
//
//import scala.io.Source
//
//class EsIndexAuditApp
//
///**
// * GET _cat/indices?v=true&format=JSON&bytes=mb
// */
//object EsIndexAuditApp extends JsonMapperUtils {
//  def main(args: Array[String]): Unit = {
//    val url = classOf[EsIndexAuditApp].getClassLoader.getResource("indices.json")
//    val data: Seq[Index] = jsonMapper.readValue[Seq[Index]](url)
//    //    this.findEmptyIndices(data);
//    var total = 0
//    data.foreach(item => {
//      if (item.index.contains("cloud_platform_hopper_") && item.storeSize != null) {
//        total += item.storeSize.toInt
//      }
//    })
//    println(MathUtils.bandwidthMb(total))
//        find(data, 20*1024, 1, "小于1G,大于1个shard")
//        find(data, 50*1024, 3, "小于20G,大于3个shard")
//  }
//
//  def findEmptyIndices(data: Seq[Index]): Unit = {
//    println("empty indices")
//    data.foreach(item => {
//      if (item.storeSize == null && item.status != "close") {
//        this.print(item)
//      }
//    })
//  }
//
//  def print(item: Index): Unit = {
//    println(s"${item.index},  store:${MathUtils.bandwidthMb(item.storeSize.toLong)}, (pri: ${item.pri}, rep: ${item.rep})")
//  }
//
//  def find(data: Seq[Index], minStoreSize: Int, maxShards: Int, tips: String = null): Seq[Index] = {
//    if (data == null || data.isEmpty) {
//      return Nil
//    }
//    val seq = data.filter(item => {
//      if (item.storeSize == null) {
//        false
//      } else {
//        val storeSize = item.storeSize.toInt
//        val shards = item.pri.toInt
//        if (storeSize < minStoreSize && shards > maxShards) {
//          true
//        } else {
//          false
//        }
//      }
//    })
//
//    println("====================")
//    println(tips)
//    seq.foreach(item =>
//      this.print(item)
//    )
//
//    seq
//  }
//}
