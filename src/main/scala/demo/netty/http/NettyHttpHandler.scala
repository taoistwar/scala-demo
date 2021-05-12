package demo.netty.http

import io.netty.buffer.Unpooled
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}
import io.netty.handler.codec.http._
import io.netty.util.CharsetUtil

import java.net.URI

/*
说明
1. SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 子类。
2. HttpObject 客户端和服务器端相互通讯的数据被封装成 HttpObject。
 */
class NettyHttpHandler extends  SimpleChannelInboundHandler[HttpObject]{
  /**
   *  读取客户端数据
   * @param ctx
   * @param msg
   */
  override def channelRead0(ctx: ChannelHandlerContext, msg: HttpObject): Unit = {
    System.out.println("对应的channel=" + ctx.channel + " pipeline=" + ctx.pipeline + " 通过pipeline获取channel" + ctx.pipeline.channel)

    System.out.println("当前ctx的handler=" + ctx.handler)

    //判断 msg 是不是 http request请求
    msg match {
      case httpRequest: HttpRequest =>
        System.out.println("ctx 类型=" + ctx.getClass)
        System.out.println("pipeline hashcode" + ctx.pipeline.hashCode + " TestHttpServerHandler hash=" + this.hashCode)
        System.out.println("msg 类型=" + msg.getClass)
        System.out.println("客户端地址" + ctx.channel.remoteAddress)
        //获取uri, 过滤指定的资源
        val uri = new URI(httpRequest.uri)
        if ("/favicon.ico" == uri.getPath) {
          System.out.println("请求了 favicon.ico, 不做响应")
          return
        }
        //回复信息给浏览器 [http协议]
        val content = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8)
        //构造一个http的相应，即 http response
        val response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content)
        response.headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain")
        response.headers.set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes)
        //将构建好 response返回
        ctx.writeAndFlush(response)
      case _ =>
    }
  }

  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
    ctx.flush()
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    ctx.close()
  }
}
