package victor.training.java.cf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ThreadPoolTaskExecutor poolBar() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(20); // core == max
    executor.setMaxPoolSize(20); // how to decide size?
    executor.setQueueCapacity(500);// how to decide size?
    executor.setThreadNamePrefix("bar-");
    executor.initialize();
    return executor;
  }
}
