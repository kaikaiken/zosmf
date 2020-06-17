package com.zos.zosmf.controller;

import com.zos.zosmf.pojo.LoginInformation;
import com.zos.zosmf.service.LoginService;
import com.zos.zosmf.utils.Result;
import com.zos.zosmf.utils.StatusCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
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
        if(notLogin(session)){
            LoginInformation loginInformation = new LoginInformation();
            loginInformation.setAccount(username);
            loginInformation.setPassword(password);
            return new Result(true, StatusCode.OK,"登陆成功",loginService.login(loginInformation , session));
        }else{
            return new Result(true, StatusCode.OK,"登陆成功","你登陆过了");
        }

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

    public static boolean notLogin(HttpSession session) {
        Object ZOSMF_JSESSIONID = session.getAttribute("ZOSMF_JSESSIONID");
        Object ZOSMF_LtpaToken2 = session.getAttribute("ZOSMF_LtpaToken2");
        Object ZOSMF_Address = session.getAttribute("ZOSMF_Address");
        Object ZOSMF_Account = session.getAttribute("ZOSMF_Account");
        return ZOSMF_JSESSIONID == null || ZOSMF_LtpaToken2 == null || ZOSMF_Address == null || ZOSMF_Account == null;
    }


}
