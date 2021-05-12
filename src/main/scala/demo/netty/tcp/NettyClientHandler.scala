package demo.netty.tcp

import io.netty.buffer.{ByteBuf, Unpooled}
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.CharsetUtil

class NettyClientHandler extends ChannelInboundHandlerAdapter{
  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    System.out.println("client " + ctx)
    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: (>^ω^<)喵", CharsetUtil.UTF_8))
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    val buf = msg.asInstanceOf[ByteBuf]
    System.out.println("服务器回复的消息:" + buf.toString(CharsetUtil.UTF_8))
    System.out.println("服务器的地址： " + ctx.channel.remoteAddress)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    super.exceptionCaught(ctx, cause)
    cause.printStackTrace()
  }
}
