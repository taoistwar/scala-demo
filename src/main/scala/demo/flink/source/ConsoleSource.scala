package demo.flink.source

import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

import scala.io.Source

class ConsoleSource extends RichSourceFunction[String] {
  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    Source.fromInputStream(System.in).getLines().foreach(line=>{
      ctx.collect(line)
    })
  }

  override def cancel(): Unit = {

  }
}
