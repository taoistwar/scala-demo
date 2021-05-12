package demo.netty.tcp

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelFuture, ChannelFutureListener, ChannelInitializer, ChannelOption}

object NettyServerDemo {
  //创建BossGroup 和 WorkerGroup
  //说明
  //1. 创建两个线程组 bossGroup 和 workerGroup
  //2. bossGroup 只是处理连接请求 , 真正的和客户端业务处理，会交给 workerGroup完成
  //3. 两个都是无限循环
  //4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
  //   默认实际 cpu核数 * 2
  val bossGroup = new NioEventLoopGroup(1)
  val workerGroup = new NioEventLoopGroup()
  val bootstrap = new ServerBootstrap()

  def main(args: Array[String]): Unit = {
    bootstrap.group(bossGroup, workerGroup)
      .channel(classOf[NioServerSocketChannel]) //使用NioSocketChannel 作为服务器的通道实现
      .option[Integer](ChannelOption.SO_BACKLOG, 128) // 设置线程队列等待连接个数
      .childOption[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
      //      .handler(null) // 该 handler对应 bossGroup , childHandler 对应 workerGroup
      .childHandler(new ChannelInitializer[SocketChannel] {
        override def initChannel(ch: SocketChannel): Unit = {
          println("客户socket channel hashcode=" + ch.hashCode())
          //可以使用一个集合管理 SocketChannel， 再推送消息时，可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
          ch.pipeline().addLast(new NettyServerHandler())
        }
      })
    println(".....服务器 is ready...")

    //绑定一个端口并且同步生成了一个 ChannelFuture 对象（也就是立马返回这样一个对象）
    //启动服务器(并绑定端口)
    val cf = bootstrap.bind(6668).sync();

    //给cf 注册监听器，监控我们关心的事件
    cf.addListener(new ChannelFutureListener() {
      override def operationComplete(future: ChannelFuture): Unit = {
        if (cf.isSuccess) {
          System.out.println("监听端口 6668 成功");
        } else {
          System.out.println("监听端口 6668 失败");
        }
      }
    });

    //对关闭通道事件  进行监听
    cf.channel().closeFuture().sync();
  }


}
