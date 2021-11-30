package app.saas

import java.sql.{Connection, DriverManager}
import scala.collection.immutable.TreeMap
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object MapPerformanceTest {
  def main(args: Array[String]): Unit = {
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
}
