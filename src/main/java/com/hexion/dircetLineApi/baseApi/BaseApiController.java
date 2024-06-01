package com.hexion.dircetLineApi.baseApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : xiaojiajun
 * @date : 2024-06-01 15:53
 * @Description: 基础接口
 */
@Api(tags = "基础接口")
@Slf4j
@RestController
@RequestMapping("/api/base")
public class BaseApiController {


    @ApiOperation(value = "启动会话")
    @PostMapping("/StartSession")
    public Object StartSession(){
        return "启动了";
    }

}
