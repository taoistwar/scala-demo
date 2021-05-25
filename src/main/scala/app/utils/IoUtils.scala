package app.utils

import scala.io.Source

class IoUtils

object IoUtils {
  def text(resource: String): String = {
    val url = this.getClass.getClassLoader.getResource(resource)
    val in = Source.fromURL(url, "utf-8")
    val text = in.mkString
    in.close()
    text
  }
}
