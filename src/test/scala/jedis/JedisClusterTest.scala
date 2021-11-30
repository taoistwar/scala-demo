package jedis


import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{HostAndPort, JedisCluster}

import java.util.concurrent.TimeUnit
import scala.collection.JavaConverters._

case class RedisMate(maxIdle: Int, maxTotal: Int, minIdle: Int, connTimeout: Int = 10000, maxAttempts: Int = 5)

object JedisClusterTest {
  def main(args: Array[String]): Unit = {
    val mate = RedisMate(1, 20, 1)
    val genericObjectPoolConfig = new GenericObjectPoolConfig
    genericObjectPoolConfig.setMaxIdle(mate.maxIdle)
    genericObjectPoolConfig.setMaxTotal(mate.maxTotal)
    genericObjectPoolConfig.setMinIdle(mate.minIdle)
    val hosts = "guanggaofenxi-03:7000,guanggaofenxi-03:7001,guanggaofenxi-04:7000,guanggaofenxi-04:7001,guanggaofenxi-05:7000,guanggaofenxi-05:7001".split(",")
    val password = "721ecb273cf2857bf6674d2b82364c7e"
    val hostAndPorts = hosts.map(h => {
      val host = h.split(":")
      new HostAndPort(host(0), host(1).toInt)
    }).toSet.asJava
    val cluster = new JedisCluster(hostAndPorts,
      mate.connTimeout, mate.connTimeout, mate.maxAttempts, password, genericObjectPoolConfig)
    for (j <- 0 to 100) {
      new Thread(new Runnable {
        override def run(): Unit = {
          while (true) {
            for (i<- 0 to 12) {
              val key = s"test${i}${j}"
              cluster.set(key, System.currentTimeMillis()+"");
              println(cluster.get(key))
            }
          }
        }
      }).start()
    }
    while (true) {

      TimeUnit.SECONDS.sleep(10)
    }
  }
}
