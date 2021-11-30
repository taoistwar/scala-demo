package demo.flink.windows

import org.apache.flink.streaming.api.scala.{DataStream, _}
import org.apache.flink.streaming.api.windowing.assigners.{SlidingEventTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.runtime.operators.window.CountWindow

import java.util.Random


class WindowDemo {

  import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows


  def timeWindow(source: DataStream[Map[String, String]]): Unit = {
    import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
    source.windowAll(SlidingEventTimeWindows.of(Time.minutes(1), Time.seconds(10))).sum(1);
    val random = new Random()
    val output = source.keyBy(_.getOrElse("k",  random.nextInt(10)+""))
      .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
      .reduce((x,y)=> {
        val v1 = x.get("v")
        val v2 = y.get("v")
        if (v1.isEmpty) {
          y
        } else if (v2.isEmpty) {
          x
        } else {
          if (v1.get > v2.get) {
            x
          } else {
            y
          }
        }
      })
    output.print()
  }
}
