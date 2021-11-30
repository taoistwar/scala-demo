package app.saas

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.util.Collector

import java.sql.{Connection, DriverManager}
import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}
import scala.collection.mutable

object ThreadTest {
  def main(args: Array[String]): Unit = {
    import java.util.concurrent.Executors
    val scheduledExecutorService = Executors.newScheduledThreadPool(1)
    val future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
      override def run(): Unit = {
        println("xx")

      }
    }, 0, 3, TimeUnit.SECONDS)
    TimeUnit.SECONDS.sleep(3)
    scheduledExecutorService.shutdownNow()

  }
}

class TestFunction extends KeyedProcessFunction[String, String, String] {
  @transient
  var dict : mutable.HashMap[String, mutable.Map[String, String]] = _
  @transient
  var scheduledExecutorService: ScheduledExecutorService = _

  override def processElement(value: String, ctx: KeyedProcessFunction[String, String, String]#Context, out: Collector[String]): Unit = ???

  override def open(parameters: Configuration): Unit = {
    // 同步加载字典数据
    dict = mutable.HashMap[String, mutable.Map[String, String]]()
    this.load()
    // 定期异步刷新
    scheduledExecutorService  = Executors.newScheduledThreadPool(1)
    scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
      override def run(): Unit = {
        load()
      }
    }, 3, 3, TimeUnit.MINUTES)
  }

  /**
   * 加载字典数据
   */
  def load(): Unit = {
    // biz
    val username = "bigdata"
    val password = "bigdata"
    val url = "jdbc:mysql://10.201.10.252:3306/sage_bigdata"

    val connection: Connection = try {
      DriverManager.getConnection(url + "?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf8", username, password)
    } catch {
      case e: Exception => e.printStackTrace()
        throw new Exception("connect to mysql fail:", e)
    }

    val statement = connection.createStatement()

    val resultSet = statement.executeQuery(
      """
        |SELECT company_id as bid, bid as fictitious_bid,  bid_name as fictitious_bid_name
        |FROM sys_organization_tenant
        |
        |""".stripMargin)
    val map = mutable.HashMap[String, (String, String)]()
    while (resultSet.next()) {
      map += resultSet.getString("bid")->
        (resultSet.getString("fictitious_bid"), resultSet.getString("fictitious_bid_name"))
    }

  }

  override def close(): Unit = {
    this.scheduledExecutorService.shutdownNow()
  }
}


