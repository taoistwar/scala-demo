package demo.netty.chat

import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

class NettyChatClientHandler extends SimpleChannelInboundHandler[String]{
  /**
   * 从服务器拿到的数据
   */
  override def channelRead0(ctx: ChannelHandlerContext, msg: String): Unit = {
     println(msg.trim());
  }
}
