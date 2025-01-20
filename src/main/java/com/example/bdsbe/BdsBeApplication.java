package com.example.bdsbe;

import com.longnh.annotions.EnableCommon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCommon
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class BdsBeApplication {

  public static void main(String[] args) {
    SpringApplication.run(BdsBeApplication.class, args);
  }
}
