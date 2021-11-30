package demo.flink.source

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

import java.util.Random

class DimensionSource extends RichSourceFunction[Map[String, String]] {
  override def open(parameters: Configuration): Unit = super.open(parameters)

  val BOUND = 100;
  val rnd = new Random()
  var count = 1;

  override def run(ctx: SourceFunction.SourceContext[Map[String, String]]): Unit = {
    while (true) {
      val map = Map[String, String](s"ch${count}" -> s"${count}");
      ctx.collect(map)
      Thread.sleep(60000L)
      count += 1
    }
  }

  override def cancel(): Unit = {

  }
}
