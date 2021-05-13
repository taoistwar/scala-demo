package demo.netty.chat


import demo.netty.chat.NettyChatServerHandler.{channels, uid}
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.{Channel, ChannelHandlerContext, ChannelId, SimpleChannelInboundHandler}
import io.netty.handler.timeout.{IdleState, IdleStateEvent}
import io.netty.util.concurrent.GlobalEventExecutor

import java.text.SimpleDateFormat
import scala.collection.mutable

object NettyChatServerHandler {
  //使用一个hashmap 管理私聊（私聊本案例并未实现，只是提供个思路）
  val channels = new mutable.HashMap[ChannelId, Channel]()

  //定义一个channle 组，管理所有的channel
  //GlobalEventExecutor.INSTANCE) 是全局的事件执行器，是一个单例
  val channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  var uid = 0
}

class NettyChatServerHandler extends SimpleChannelInboundHandler[String] {

  import demo.netty.chat.NettyChatServerHandler.{channelGroup, sdf}
  //handlerAdded 表示连接建立，一旦连接，第一个被执行
  //将当前channel 加入到  channelGroup

  override def handlerAdded(ctx: ChannelHandlerContext): Unit = {
    val channel = ctx.channel()
    //将该客户加入聊天的信息推送给其它在线的客户端

    //该方法会将 channelGroup 中所有的channel 遍历，并发送消息，我们不需要自己遍历
    channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天" + sdf.format(new java.util.Date()) + " \n")
    channelGroup.add(channel)

    //私聊如何实现
    channels += channel.id() -> channel //TODO 登录后，绑定用户信息
  }

  /**
   * 断开连接, 将xx客户离开信息推送给当前在线的客户
   */
  override def handlerRemoved(ctx: ChannelHandlerContext): Unit = {
    val channel = ctx.channel()
    channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n")
    println("channelGroup size" + channelGroup.size())
    val item = channels.find(entry => entry._2 == channel)
    if (item.nonEmpty) {
      channels -= item.get._1
    }
  }

  /**
   * 表示channel 处于活动状态, 提示 xx上线
   */
  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    //这个是给服务端看的，客户端上面已经提示xxx加入群聊了
    println(ctx.channel().remoteAddress() + "上线了~")
  }

  /**
   * 表示channel 处于不活动状态, 提示 xx 离线了
   */
  override def channelInactive(ctx: ChannelHandlerContext): Unit = {
    println(ctx.channel().remoteAddress() + "离线了~")
  }

  /**
   * 读取数据，转发给在线的每一个客户端
   */
  override def channelRead0(ctx: ChannelHandlerContext, msg: String): Unit = {
    //获取到当前channel
    val channel = ctx.channel()
    //这时我们遍历channelGroup, 根据不同的情况，回送不同的消息
    val iter = channelGroup.iterator()
    while (iter.hasNext) {
      val ch = iter.next()
      if(channel != ch) { //不是当前的channel,转发消息
        ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息" + msg + "\n")
      }else {//回显自己发送的消息给自己
        ch.writeAndFlush("[自己]发送了消息" + msg + "\n")
      }
    }
  }
  @SuppressWarnings(Array("deprecation"))
  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    ctx.close()
  }
  override def userEventTriggered(ctx: ChannelHandlerContext, evt: Any): Unit = {
    evt match {
      case event: IdleStateEvent =>
        val eventType = event.state() match {
          case IdleState.READER_IDLE =>
            "读空闲"
          case IdleState.WRITER_IDLE =>
            "写空闲"
          case IdleState.ALL_IDLE =>
            "读写空闲"
        }
        println(ctx.channel().remoteAddress() + "--超时时间--" + eventType)
        println("服务器做相应处理..")
    }
    //如果发生空闲，我们关闭通道
    // ctx.channel().close();
  }
}
