package demo.netty.http

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.{ChannelInitializer, ChannelOption}
import io.netty.handler.codec.http._

import java.net.URI

object NettyHttpClientDemo {
  val workerGroup = new NioEventLoopGroup();
  val host = "127.0.0.1"
  val port = 6668

  def main(args: Array[String]): Unit = {
    try {
      val b = new Bootstrap();
      b.group(workerGroup);
      b.channel(classOf[NioSocketChannel]);
      b.option[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true);
      b.handler(new ChannelInitializer[SocketChannel]() {
        override def initChannel(ch: SocketChannel): Unit = {
          // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
//          ch.pipeline().addLast(new HttpResponseDecoder());
          // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
//          ch.pipeline().addLast(new HttpRequestEncoder());
          // HttpClientCodec = HttpResponseDecoder + HttpRequestEncoder
          ch.pipeline().addLast(new HttpClientCodec())
          ch.pipeline().addLast(new NettyHttpClientHandler());
        }
      });
      val f = b.connect(host, port).sync();
      val uri = new URI("http://127.0.0.1:8000");
      val msg = "Are you ok?";
      val request = new DefaultFullHttpRequest(
        HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString(),
        Unpooled.wrappedBuffer(msg.getBytes()));
      // 构建http请求
      request.headers().set(HttpHeaderNames.HOST, host);
      request.headers().set(HttpHeaderNames.CONNECTION,
        HttpHeaderNames.CONNECTION);
      request.headers().set(HttpHeaderNames.CONTENT_LENGTH,
        request.content().readableBytes());
      request.headers().set("messageType", "normal");
      request.headers().set("businessType", "testServerState");
      // 发送http请求
      f.channel().write(request);
      f.channel().flush();
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
    }
  }
}
