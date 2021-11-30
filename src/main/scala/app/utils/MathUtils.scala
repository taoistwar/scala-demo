package app.utils

import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode

object MathUtils {
  val kb = 1024
  val mb = 1024 * kb
  val gb = 1024 * mb
  val tb = 1024 * gb


  def bandwidthB(value: Long): String = {
    if (value > 1024*1024*1024*1024) { // tb=1024*1024*1024*1024
      val res = (BigDecimal(value) / BigDecimal(1024*1024*1024*1024)).setScale(2,RoundingMode.DOWN)
      return s"${res.toDouble}Tb"
    }
    if (value > 1024*1024*1024) { // gb=1024*1024*1024b
      val res = (BigDecimal(value) / BigDecimal(1024*1024*1024 )).setScale(2,RoundingMode.DOWN)
      return s"${res.toDouble}Gb"
    }
    if (value > 1024*1024) { // mb=1024*1024b
      val res = (BigDecimal(value) / BigDecimal(1024*1024 )).setScale(2,RoundingMode.DOWN)
      return s"${res.toDouble}Mb"
    }
    if (value > 1024) { // kb=1024b
      val res = (BigDecimal(value) / BigDecimal(1024 )).setScale(2,RoundingMode.DOWN)
      return s"${res.toDouble}Kb"
    }
    s"${value}"
  }

  def bandwidthMb(value: Long): String = {
    if (value > 1024 * 1024) { // tb=1024*1024
      val res = (BigDecimal(value) / BigDecimal(1024 * 1024)).setScale(2,RoundingMode.DOWN)
      return s"${res.toDouble}Tb"
    }
    if (value > 1024) { // gb=1024
      val res = (BigDecimal(value) / BigDecimal(1024 )).setScale(2,RoundingMode.DOWN)
      return s"${res.toDouble}Gb"
    }
    s"${value}Mb"
  }
}
