package eu.anmore.spike.logback.applicationbased;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Application Sping MVC REST configuration.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "eu.anmore.spike.logback.applicationbased" })
public class RestConfiguration {

}
