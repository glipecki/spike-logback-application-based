package eu.anmore.spike.logback.applicationbased;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LoggingConfigurerListener implements ServletContextListener {

	private static final String LOGBACK_FILE_PATTERN = "%s/%s-logback.xml";

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingConfigurerListener.class);

	private static final String CONFIGS_DIR = "logback.configurationDir";

	@Override
	public void contextDestroyed(final ServletContextEvent sce) {
		// intentionally left blank.
	}

	@Override
	public void contextInitialized(final ServletContextEvent servletContextEvent) {
		final String contextName = getContextName(servletContextEvent);
		configureLogging(contextName);
		LOGGER.info("Staring application [contextName={}]", contextName);
	}

	private void configureLogger(final String application, final String configurationFile) {
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			final JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			context.reset();
			configurator.doConfigure(configurationFile);
		} catch (final JoranException je) {
			StatusPrinter.printInCaseOfErrorsOrWarnings(context);
		}
	}

	private void configureLogging(final String contextName) {
		final String logbackConfigurationFile = String.format(LOGBACK_FILE_PATTERN,
				withoutTailingSlash(System.getProperty(CONFIGS_DIR)), contextName);
		if (fileExists(logbackConfigurationFile)) {
			System.out.println(String.format("Configuring application logging [contextName=%s, configsFile=%s]",
					contextName, logbackConfigurationFile));
			configureLogger(contextName, logbackConfigurationFile);
		} else {
			System.out.println(String.format("Configuring default application logging [contextName=%s]", contextName));
		}
		StatusPrinter.print((LoggerContext) LoggerFactory.getILoggerFactory());
	}

	private boolean fileExists(final String logbackConfigurationFile) {
		return new File(logbackConfigurationFile).isFile();
	}

	private String getContextName(final ServletContextEvent sce) {
		return sce.getServletContext()
					.getServletContextName()
					.toLowerCase();
	}

	private String withoutTailingSlash(final String configsDir) {
		return configsDir != null && configsDir.endsWith("/") ? configsDir.substring(0, configsDir.length() - 1)
				: configsDir;
	}

}
