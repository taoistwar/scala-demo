package demo.netty.chat

import io.netty.bootstrap.{Bootstrap, ServerBootstrap}
import io.netty.channel.{ChannelInitializer, ChannelOption}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}
import io.netty.handler.timeout.IdleStateHandler

import java.util.concurrent.TimeUnit

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
            //加入一个netty 提供 IdleStateHandler
            /*
            说明
            1. IdleStateHandler 是netty 提供的处理空闲状态的处理器
            2. long readerIdleTime : 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接。0不检测
            3. long writerIdleTime : 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接。0不检测
            4. long allIdleTime : 表示多长时间没有读写, 就会发送一个心跳检测包检测是否连接。0不检测

            5. 文档说明
            triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
            read, write, or both operation for a while.
            6. 当 IdleStateEvent 触发后 , 就会传递给管道 的下一个handler去处理，通过调用(触发)
            下一个handler 的 userEventTiggered , 在该方法中去处理 IdleStateEvent(读空闲，写空闲，读写空闲)
            7.handlerRemoved有时候是无法感知连接断掉，所以还是需要心跳包的检测来判断连接是否还有效
             */
            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS))
            pipeline.addLast("decoder", new StringDecoder())
            pipeline.addLast("encoder", new StringEncoder())
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
