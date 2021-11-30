package demo.opt


import joptsimple.OptionParser
import joptsimple.util.{KeyValuePair, PathConverter}

import java.util

object OptDemo {
  val parser = new OptionParser()
  val helpOption = parser.acceptsAll(util.Arrays.asList("h", "help"), "Show help").forHelp();
  val silentOption = parser.acceptsAll(util.Arrays.asList("s", "silent"), "Show minimal output")
  val verboseOption =
    parser.acceptsAll(util.Arrays.asList("v", "verbose"), "Show verbose output").availableUnless(silentOption)
  val settingOption = parser.accepts("E", "Configure a setting").withRequiredArg().ofType(classOf[KeyValuePair])
  val versionOption = parser.acceptsAll(util.Arrays.asList("V", "version"),
    "Prints Elasticsearch version information and exits");
  val daemonizeOption = parser.acceptsAll(util.Arrays.asList("d", "daemonize"),
    "Starts Elasticsearch in the background")
    .availableUnless(versionOption);
  val pidfileOption = parser.acceptsAll(util.Arrays.asList("p", "pidfile"),
    "Creates a pid file in the specified path on start")
    .availableUnless(versionOption)
    .withRequiredArg()
    .withValuesConvertedBy(new PathConverter());
  val quietOption = parser.acceptsAll(util.Arrays.asList("q", "quiet"),
    "Turns off standard output/error streams logging in console")
    .availableUnless(versionOption)
    .availableUnless(daemonizeOption);


  def main(args: Array[String]): Unit = {
    val options = parser.parse(args: _*)
    if (options.has(helpOption)) {
      parser.printHelpOn(System.out)
      return;
    }

    if (options.has(silentOption)) {
       println("silent")
    }
    if (options.has(verboseOption)) {
      println("verbose")
    }
  }
}
