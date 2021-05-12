package demo.netty.http

import io.netty.channel.socket.SocketChannel
import io.netty.channel.{ChannelInitializer, ChannelPipeline}
import io.netty.handler.codec.http.{HttpRequestDecoder, HttpResponseEncoder, HttpServerCodec}

class NettyHttpServerInitializer extends ChannelInitializer[SocketChannel] {
  override def initChannel(ch: SocketChannel): Unit = {
    //向管道加入处理器//向管道加入处理器
    //得到管道
    val pipeline: ChannelPipeline = ch.pipeline
    //加入一个netty 提供的httpServerCodec codec =>[coder - decoder]
    //HttpServerCodec 说明
    //1. HttpServerCodec 是netty 提供的处理http的 编-解码器
    // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
//    ch.pipeline().addLast(new HttpResponseEncoder());
//    // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
//    ch.pipeline().addLast(new HttpRequestDecoder());
    // HttpServerCodec = HttpResponseEncoder + HttpRequestDecoder
    pipeline.addLast("MyHttpServerCodec", new HttpServerCodec())
    //2. 增加一个自定义的handler
    pipeline.addLast("MyTestHttpServerHandler", new NettyHttpHandler())

    System.out.println("ok~~~~")
  }
}
