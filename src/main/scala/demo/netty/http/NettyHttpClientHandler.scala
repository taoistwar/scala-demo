package demo.netty.http

import io.netty.buffer.{ByteBuf, Unpooled}
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.handler.codec.http.{HttpContent, HttpHeaderNames, HttpResponse, HttpUtil}

import scala.reflect.internal.ClassfileConstants.instanceof
import scala.reflect.internal.util.NoSourceFile.content

class NettyHttpClientHandler extends ChannelInboundHandlerAdapter {
  private var reader: ByteBufToBytes = null

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    msg match {
      case response: HttpResponse =>
        System.out.println("CONTENT_TYPE:"
          + response.headers().get(HttpHeaderNames.CONTENT_TYPE));
        if (HttpUtil.isContentLengthSet(response)) {
          reader = new ByteBufToBytes(
            HttpUtil.getContentLength(response).toInt);
        }
      case _ =>
    }
    msg match {
      case httpContent: HttpContent =>
        val content = httpContent.content();
        reader.reading(content);
        content.release();
        if (reader.isEnd()) {
          val resultStr = new String(reader.readFull());
          System.out.println("Server said:" + resultStr);
          ctx.close();
        }
      case _ =>
    }
  }
}

class ByteBufToBytes(length: Int) {
  private val temp: ByteBuf = Unpooled.buffer(length);
  private var end = true;


  def reading(buf: ByteBuf): Unit = {
    buf.readBytes(temp, buf.readableBytes());
    if (this.temp.writableBytes() != 0) {
      end = false;
    } else {
      end = true;
    }
  }

  def isEnd(): Boolean = {
    return end;
  }

  def readFull(): Array[Byte] = {
    if (end) {
      val readableBytes = this.temp.readableBytes();
      val contentByte = new Array[Byte](readableBytes);
      this.temp.readBytes(contentByte);
      this.temp.release();
      contentByte;
    } else {
      null;
    }
  }

  def read(buf: ByteBuf): Array[Byte] = {
    val bytes = new Array[Byte](buf.readableBytes());
    buf.readBytes(bytes);
    bytes;
  }
}