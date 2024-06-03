package com.hexion.dircetLineApi.baseApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : xiaojiajun
 * @date : 2024-06-01 15:53
 * @Description: 基础接口
 */
@Api(tags = "测试")
@Slf4j
@RestController
@RequestMapping("/api/base")
public class BaseApiController {

    @ApiOperation(value = "测试")
    @PostMapping("/StartSession")
    public Object StartSession(HttpServletRequest request){
//        conversationsApi.conversationsStartConversationCall();
        String header = request.getHeader("Authorization");
        return "启动了";
    }

}
