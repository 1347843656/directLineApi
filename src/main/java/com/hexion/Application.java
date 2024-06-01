package com.hexion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author : xiaojiajun
 * @date : 2024-06-01 15:33
 * @Description: 启动类
 */

@SpringBootApplication
public class Application {

    final static String SWAGGER_URL = "http://localhost:8080/swagger-ui.html";

    public static void main(String[] args) {

        try {
            // 尝试打开默认的浏览器访问指定的URL
            Desktop.getDesktop().browse(new URI(SWAGGER_URL));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            // 在生产环境中，应该更妥善地处理异常
            System.err.println("无法打开浏览器：" + e.getMessage());
        }
        System.out.println("启动成功");
        SpringApplication.run(Application.class, args);
    }
}
