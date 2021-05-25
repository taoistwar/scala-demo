package demo.netty.websocket

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup

object NettyWebsocketServerDemo {
  val bossGroup = new NioEventLoopGroup()
  val workGroup = new NioEventLoopGroup()

  def main(args: Array[String]): Unit = {
    try {
      val serverBootstrap = new ServerBootstrap()
      serverBootstrap.group(bossGroup, workGroup)

    }  finally {
      bossGroup.shutdownGracefully()
      workGroup.shutdownGracefully()
    }
  }
}
