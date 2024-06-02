package com.hexion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author : xiaojiajun
 * @date : 2024-06-01 15:33
 * @Description: 启动类
 */

@SpringBootApplication
public class Application {

    final static String SWAGGER_URL = "http://localhost:8080/swagger-ui.html";

    public static void main(String[] args) {
        System.out.println("启动成功");
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openSwaggerUI() {
        System.out.println("跳转swagger页面");
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(SWAGGER_URL));
            } else {
                System.err.println("Desktop is not supported. Please open the URL manually:"+SWAGGER_URL);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
