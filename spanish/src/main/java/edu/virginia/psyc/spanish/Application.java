package edu.virginia.psyc.spanish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This is a Spring based application, and will auto discover the 
 * controllers in this code base and make them available
 *  as web endpoints.
 *
 * If you are using this as a template, be sure to repace "org.mindtrails.basic" in the scan
 * paths below with the package or your own code base.
 */
@ComponentScan ({"edu.virginia.psyc.spanish", "org.mindtrails"})
@Configuration
@EnableJpaRepositories(basePackages = {"edu.virginia.psyc.spanish", "org.mindtrails"})
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EntityScan(basePackages = {"edu.virginia.psyc.spanish", "org.mindtrails"})
@EnableScheduling
public class Application {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        InputStream in = Application.class.getClassLoader().getResourceAsStream("default.properties");
        properties.load(in);
        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(properties);
        app.run(args);
    }
}