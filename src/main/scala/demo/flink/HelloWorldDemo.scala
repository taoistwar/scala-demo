package demo.flink

import demo.flink.source.{ConsoleSource, DimensionSource}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.configuration.{Configuration, RestOptions}
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector

import java.util.Properties

object HelloWorldDemo {
  val broadcastSchema = new MapStateDescriptor[String, String]("broadcast", classOf[String], classOf[String])

  def main(args: Array[String]): Unit = {
    val config = new Configuration()
    config.setInteger(RestOptions.PORT, 8080) // Flink WebUI port

    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config)

    val testSource: DataStream[Map[String, String]] = env.addSource(new DimensionSource())
    val consoleSource = env.addSource(new ConsoleSource())
    val broadcast = testSource.broadcast(broadcastSchema)

    val broadcastStream: BroadcastConnectedStream[String, Map[String, String]] = consoleSource.connect(broadcast)
    val output = broadcastStream
      .process[String](new BroadcastProcessFunction[String, Map[String, String], String] {
        override def processElement(data: String, ctx: BroadcastProcessFunction[String, Map[String, String], String]#ReadOnlyContext, out: Collector[String]): Unit = {
          val state = ctx.getBroadcastState(broadcastSchema)
          val value = state.get(data) match {
            case null =>
              "not match"
            case x =>
              x
          }
          out.collect(value)
        }

        override def processBroadcastElement(value: Map[String, String], ctx: BroadcastProcessFunction[String, Map[String, String], String]#Context, out: Collector[String]): Unit = {
          val state = ctx.getBroadcastState(broadcastSchema)
          value.foreach((k) => {
            state.put(k._1, k._2);
          })
          out.collect(value.toString())
        }
      })

    output.print()
    env.execute()
  }

  def enableCheckpoint(env: StreamExecutionEnvironment) = {
     
  }

  def kafkaSource(topic: String): FlinkKafkaConsumer[String] = {
    val prop = new Properties()
    prop.setProperty("bootstrap.servers", "")
    prop.setProperty("group.id", "f1")
    new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), prop)
  }
}
