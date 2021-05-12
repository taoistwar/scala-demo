package demo.netty.chat

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}

import java.util.Scanner


object NettyChatClientDemo {
  val group = new NioEventLoopGroup()
  val bootstrap = new Bootstrap()

  def main(args: Array[String]): Unit = {
    try {
      val host = if (args.length > 0) args(0) else "127.0.0.1"
      val port = if (args.length > 1) args(1).toInt else 6678

      bootstrap.group(group)
        .channel(classOf[NioSocketChannel])
        .handler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit = {
            //得到pipeline
            val pipeline = ch.pipeline()
            //加入相关handler
            pipeline.addLast("decoder", new StringDecoder())
            pipeline.addLast("encoder", new StringEncoder())
            //加入自定义的handler
            pipeline.addLast(new NettyChatClientHandler())
          }
        })
      val channelFuture = bootstrap.connect(host, port).sync()
      //得到channel
      val channel = channelFuture.channel()
      println("-------" + channel.localAddress() + "--------")
      //客户端需要输入信息，创建一个扫描器
      val scanner = new Scanner(System.in)
      while (scanner.hasNextLine) {
        val msg = scanner.nextLine()
        //通过channel 发送到服务器端
        channel.writeAndFlush(msg + "\r\n")
      }
    } catch {
      case e: Exception =>
        e.printStackTrace()
    } finally {
      group.shutdownGracefully();
    }
  }
}
