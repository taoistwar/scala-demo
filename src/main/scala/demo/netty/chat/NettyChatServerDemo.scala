package demo.netty.chat

import io.netty.bootstrap.{Bootstrap, ServerBootstrap}
import io.netty.channel.{ChannelInitializer, ChannelOption}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}

object NettyChatServerDemo {
  val port = 6678
  val bossGroup = new NioEventLoopGroup()
  val workerGroup = new NioEventLoopGroup()
  val bootstrap = new ServerBootstrap()
  def main(args: Array[String]): Unit = {
    try {
      bootstrap.group(bossGroup, workerGroup)
        .channel(classOf[NioServerSocketChannel])
        .option[java.lang.Integer](ChannelOption.SO_BACKLOG, 128)
        .childOption[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)
        .childHandler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit = {
            val pipeline = ch.pipeline()
            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());
            pipeline.addLast(new NettyChatServerHandler())
          }
        })
      System.out.println("netty 服务器启动")
      val channelFuture = bootstrap.bind(port).sync

      //监听关闭
      channelFuture.channel.closeFuture.sync
    } finally {
      bossGroup.shutdownGracefully()
      workerGroup.shutdownGracefully()
    }
  }
}
