package demo

import org.apache.http.Consts
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.{ContentType, StringEntity}
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils;

object LoginDemo {
  val url = "http://120.253.32.3:49090/sign-in"
  val body = "{\"realm\":\"zeta\",\"creds\":[{\"accountName\":\"super\",\"password\":\"super\"}]}"
  def main(args: Array[String]): Unit = {

    val client = HttpClients.createDefault()
    val request =  new HttpPost(url);
    request.setEntity(new StringEntity(body, Consts.UTF_8))
    val res = client.execute(request);
    res.getAllHeaders.foreach(println)
    val data = EntityUtils.toString(res.getEntity)
    println(data)
  }
}
