package hello;

import hello.storage.StorageProperties;
import hello.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {
  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    LOG.info("main(args: {})", Arrays.toString(args));

    SpringApplication.run(Application.class, args);
  }

/*
  // This is how you change from Spring's MultipartResolver to CommonsMultipartResolver.
  // Note that you have to include commons-fileupload in your build.gradle for this to work.
  @Bean
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver resolver=new CommonsMultipartResolver();
    resolver.setDefaultEncoding("utf-8");
    return resolver;
  }
*/
  @Bean
  CommandLineRunner init(StorageService storageService) {
    return (args) -> {
      storageService.deleteAll();
      storageService.init();
    };
  }
}
