package demo.netty.tcp

import io.netty.buffer.{ByteBuf, Unpooled}
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.CharsetUtil

import java.util.concurrent.TimeUnit
/*
说明
1. 我们自定义一个Handler 需要继承netty 规定好的某个HandlerAdapter(规范)
2. 这时我们自定义一个Handler , 才能称为一个handler
 */
class NettyServerHandler extends ChannelInboundHandlerAdapter{
  //读取数据事件(这里我们可以读取客户端发送的消息)
  /*
  1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
  2. Object msg: 就是客户端发送的数据 默认Object
   */ @throws[Exception]
  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    // 比如这里我们有一个非常耗时长的业务-> 异步执行 -> 提交该channel 对应的
    // NIOEventLoop 的 taskQueue中,

    // 解决方案1 用户程序自定义的普通任务

    ctx.channel.eventLoop.execute(new Runnable() {
      override def run(): Unit = {
        try {
          Thread.sleep(5 * 1000)
          ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵2", CharsetUtil.UTF_8))
          System.out.println("channel code=" + ctx.channel.hashCode)
        } catch {
          case ex: Exception =>
            System.out.println("发生异常" + ex.getMessage)
        }
      }
    })
    ctx.channel.eventLoop.execute(new Runnable() {
      override def run(): Unit = {
        try {
          Thread.sleep(5 * 1000)
          ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵3", CharsetUtil.UTF_8))
          System.out.println("channel code=" + ctx.channel.hashCode)
        } catch {
          case ex: Exception =>
            System.out.println("发生异常" + ex.getMessage)
        }
      }
    })
    //解决方案2 : 用户自定义定时任务 -》 该任务是提交到 scheduleTaskQueue中
    ctx.channel.eventLoop.schedule(new Runnable() {
      override def run(): Unit = {
        try {
          Thread.sleep(5 * 1000)
          ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵4", CharsetUtil.UTF_8))
          System.out.println("channel code=" + ctx.channel.hashCode)
        } catch {
          case ex: Exception =>
            System.out.println("发生异常" + ex.getMessage)
        }
      }
    }, 5, TimeUnit.SECONDS)

    System.out.println("go on ...")

    def directRead(): Unit = {
      println("服务器读取线程 " + Thread.currentThread.getName + " channle =" + ctx.channel)
      println("server ctx =" + ctx)
      println("看看channel 和 pipeline的关系")
      val channel = ctx.channel
      val pipeline = ctx.pipeline //本质是一个双向链表
      //将 msg 转成一个 ByteBuf
      //ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer.
      val buf = msg.asInstanceOf[ByteBuf]
      println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8))
      println("客户端地址:" + channel.remoteAddress)
    }

//    directRead
  }

  //数据读取完毕
  @throws[Exception]
  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = { //writeAndFlush 是 write + flush
    //将数据写入到缓存，并刷新
    //一般讲，我们对这个发送的数据进行编码
    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵1", CharsetUtil.UTF_8))
  }

  //发生异常后, 一般是需要关闭通道
  @throws[Exception]
  @Override
  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    super.exceptionCaught(ctx, cause)
    ctx.close
  }
}
