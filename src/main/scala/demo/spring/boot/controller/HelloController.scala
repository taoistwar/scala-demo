package demo.spring.boot.controller

import org.springframework.web.bind.annotation.{RequestMapping, RestController}

@RestController
class HelloController {
  @RequestMapping(value = Array("/hello"))
  def index(): String = {
    "hello scala"
  }
}
