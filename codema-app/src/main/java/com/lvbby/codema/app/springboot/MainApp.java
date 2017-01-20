package com.lvbby.codema.app.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lipeng on 2017/1/3.
 */
@SpringBootApplication
@ComponentScan
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
        System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) + " ======== server started! =========");
    }
}
