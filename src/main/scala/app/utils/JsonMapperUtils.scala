//package app.utils
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
//
//trait JsonMapperUtils {
//  val jsonMapper = newJsonMapper()
//
//  def newJsonMapper(): ObjectMapper with ScalaObjectMapper = {
//    val mapper = new ObjectMapper() with ScalaObjectMapper
//    mapper.registerModule(DefaultScalaModule)
//    mapper
//  }
//}
