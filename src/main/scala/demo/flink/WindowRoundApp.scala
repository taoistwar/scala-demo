package demo.flink

import demo.flink.source.DimensionSource
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object WindowRoundApp {
  val broadcastSchema = new MapStateDescriptor[String, String]("broadcast", classOf[String], classOf[String])

  def main(args: Array[String]): Unit = {
    val config = new Configuration()
    config.setInteger(RestOptions.PORT, 8080) // Flink WebUI port

    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config)

    val testSource: DataStream[Map[String, String]] = env.addSource(new DimensionSource())

    val output = testSource.keyBy(_.getOrElse("k", Math.random())).window(TumblingEventTimeWindows.of(Time.seconds(5)))
      .reduce((x, y) => x)
    output.print()
    env.execute()
  }

}
