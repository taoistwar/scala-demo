package app.es.index.pattern

import scala.io.Source

object IndexPatternApp {
  val patterns = List("cloud_platform_bandwidth_pcu_",
    "cloud_platform_rom_bandwidth",
    "cloud_platform_bandwidth_offline",
    "cloud_platform_plana",
    "cloud_platform_bandwidth_result",
    "cloud_platform_bandwidth",
    "cloud_platform_rom_original",
    "cloud_platform_rom_salt_original",
    "instance_order",
    "cloud_platform_r_streamer",
    "streamer_caton-",
    "cloud_platform_saas_timer",
    "cloud_platform_pcu",
    "cloud_platform_operation",
    "cloud_game_hopper_analyse",
    "cloud_game_micro_data",
    "cloud_platform_hopper",
    "cloud_platform_instance_monitor",
    "lark_web_pc_data",
    "zabbix_platform",
    "cloud_platform_r",
    "cloud_platform_instance_statistical",
    "cloud_platform_open_api",
    "cloud_game_detailed",
    "cloud_platform_request_pcu",
    "cloud_platform_rom_source",
    "decoding_time",
    "xiaowo_d_",
    "cloud_platform_instance_status",
    "cloud_platform_peakbitrate",
    "shrink-cloud_platform_hopper",
    "parse_error_cloud_platform_hopper",
    "cloud_platform_peak_pcu_",
    "cloud_platform_rom_original",
    "cloud_platform_h5_expand_data",
    "shrink-cloud_platform_h5_expand_data",
    "cloud_platform_indicator_audit_data",
    "cloud_platform_bid_business_realtime_dat",
    "shrink-cloud_game_caton_detail",
    "cloud_game_caton_detail",
    "cloud_phone_m_",
    "micro_cloud_game",
    "cloud_platform_value-added",
    "cloud_platform_new_and_remain_"
  )
  def filterPatterns(index: String):Boolean = {
    if (index.startsWith(".")) {
      return false
    }
    for (pattern <- patterns) {
      if (index.startsWith(pattern)) {
        return false
      }
    }
    true
  }

  def main(args: Array[String]): Unit = {
    val file = "/Users/taoistwar/haima-biz/indices.log"
    val io = Source.fromFile(file)


    val lines = io.getLines().toList
    val indices = lines.filter(filterPatterns)
    indices.foreach(println)
    io.close()
    println("-----")
    patterns.sorted.foreach(println)
  }
}
