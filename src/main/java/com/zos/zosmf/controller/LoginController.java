package com.zos.zosmf.controller;

import com.zos.zosmf.pojo.LoginInformation;
import com.zos.zosmf.service.LoginService;
import com.zos.zosmf.utils.Result;
import com.zos.zosmf.utils.StatusCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class LoginController {

    @Resource
    private LoginService loginService;

    /**
     * 登录
     */
    @ApiOperation(value = "",notes = "登陆",tags = {"LoginController"})
    @PostMapping(value = "/login")
    public Result login(String username, String password, HttpSession session) {
        if(loginService.notLogin(session)){
            LoginInformation loginInformation = new LoginInformation();
            loginInformation.setAccount(username);
            loginInformation.setPassword(password);
            String request = loginService.login(loginInformation , session);
            if(request.equals("successful")){
                return new Result(true, StatusCode.OK,"登陆成功");
            }
            return new Result(true, StatusCode.ERROR,"登陆失败",request);
        }
        return new Result(true, StatusCode.OK,"登陆失败","你已经登陆过了");
    }

    /***
     * 注销
     */
    @ApiOperation(value = "注销",notes = "注销",tags = {"LoginController"})
    @DeleteMapping(value = "/logoff" )
    public Result delete(HttpSession session) {
        loginService.logoff(session);
        return new Result(true,StatusCode.OK,"注销成功");
    }




}
