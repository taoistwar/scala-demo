package demo.spring.boot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan


@ComponentScan(value = Array(
  "demo.spring.boot.controller"
))
@SpringBootApplication
class SpringBootApp

object SpringBootApp extends App {
  SpringApplication.run(classOf[SpringBootApp])
}

