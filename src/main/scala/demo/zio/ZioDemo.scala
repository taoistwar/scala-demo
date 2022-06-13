package demo.zio

import zio.clock.Clock
import zio.duration.durationInt
import zio.{Task, ZIO}

import java.util.concurrent.TimeUnit

object ZioDemo {
  def main(args: Array[String]): Unit = {
    val goShopping: Task[Unit] = ZIO.effect(println("Going to xx"))
   goShopping.run

  }
}
