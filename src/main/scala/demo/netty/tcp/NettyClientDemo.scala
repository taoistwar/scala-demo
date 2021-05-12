package demo.netty.tcp

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel

object NettyClientDemo extends App {
  val group = new NioEventLoopGroup()
  val bootstrap = this.init

  private def init = {
    val bootstrap = new Bootstrap()
    //设置相关参数
    bootstrap.group(group) //设置线程组
      .channel(classOf[NioSocketChannel]) // 设置客户端通道的实现类(反射)
      .handler(new ChannelInitializer[SocketChannel]() {
        @throws[Exception]
        protected def initChannel(ch: SocketChannel): Unit = {
          ch.pipeline.addLast(new NettyClientHandler) //加入自己的处理器
        }
      })
    println("客户端 ok..")
    bootstrap
  }

  //启动客户端去连接服务器端
  //关于 ChannelFuture 要分析，涉及到netty的异步模型
  val channelFuture = bootstrap.connect("127.0.0.1", 6668).sync
  //对关闭通道事件  进行监听
  channelFuture.channel.closeFuture.sync

  group.shutdownGracefully()
}
