package app.ha

import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry;

object DmpHaByZk {
  def main(args: Array[String]): Unit = {
    val retryPolicy = new ExponentialBackoffRetry(1000, 3);

    val client = CuratorFrameworkFactory.newClient("domain1.book.zookeeper:2181", 5000, 3000, retryPolicy);

    client.start();

    Thread.sleep(Integer.MAX_VALUE);
  }
}
