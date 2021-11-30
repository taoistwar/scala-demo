
import org.joda.time.{DateTime, DateTimeZone}

import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}

object DateTimeTest extends App {
  val raw = 1627553434000L
  val zone = TimeZone.getTimeZone("Asia/Shanghai")
  println(zone)
  println(new DateTime(raw).toString("yyyy-MM-dd HH:mm:ss"))
  println(new DateTime(raw).withZone(DateTimeZone.UTC).toDate.toString())
  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  println(sdf.format(raw))
  sdf.applyPattern("yyyy-MM-dd HH:mm:ss Z")
  println(sdf.format(new Date(raw)))

}
