package demo.log4j2

import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory
import org.apache.logging.log4j.core.impl.Log4jContextFactory
import org.apache.logging.log4j.status.{StatusConsoleListener, StatusData, StatusListener, StatusLogger}
import org.apache.logging.log4j.{Level, LogManager}

class LogConfigurator

object LogConfigurator {
  val LOGGER: StatusLogger = StatusLogger.getLogger
  val FQCN: String = classOf[LogConfigurator].getName
  val ERROR_LISTENER: StatusListener = new StatusConsoleListener(Level.ERROR) {
    override def log(data: StatusData): Unit = {
      super.log(data)
    }
  }

  def configureLoggerLevels(): Unit = {
    val logger = LogManager.getRootLogger
    val ctx = LoggerContext.getContext(false)
    val config = ctx.getConfiguration
    //    val loggerConfig = config.getLoggerConfig(logger.getName)
    //    loggerConfig.setLevel(level);
    //    ctx.updateLoggers();
  }

  def registerErrorListener(): Unit = {
    StatusLogger.getLogger.registerListener(ERROR_LISTENER);
  }

  def configureStatusLogger(): Unit = {
    val factory = genFactory()
    val configuration = genConfiguration()
    try {
      if (factory == null) {
        null
      } else {
        factory.getContext(FQCN, null, null, false, configuration)
      }
    } catch {
      case ex: Exception =>
//        LOGGER.error("There was a problem initializing the LoggerContext using configuration {}",
//          configuration.getName, ex);
    }
  }

  def genConfiguration(): Configuration = {
    val builder = ConfigurationBuilderFactory.newConfigurationBuilder()
    builder.setStatusLevel(Level.ERROR);
    builder.build()
  }

  def genFactory(): Log4jContextFactory = {
    val factory = LogManager.getFactory()

    if (factory.isInstanceOf[Log4jContextFactory]) {
      factory.asInstanceOf[Log4jContextFactory];
    } else if (factory != null) {
//      LOGGER.error("LogManager returned an instance of {} which does not implement {}. Unable to initialize Log4j.",
//        factory.getClass.getName, classOf[Log4jContextFactory].getName)
      null;
    } else {
//      LOGGER.fatal("LogManager did not return a LoggerContextFactory. This indicates something has gone terribly wrong!");
      null;
    }
  }
}

object Log4j2Demo {
  def main(args: Array[String]): Unit = {
    LogConfigurator.registerErrorListener()
  }
}
