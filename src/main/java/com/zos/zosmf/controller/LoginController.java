package com.zos.zosmf.controller;

import com.zos.zosmf.pojo.LoginInformation;
import com.zos.zosmf.service.LoginService;
import com.zos.zosmf.utils.Result;
import com.zos.zosmf.utils.StatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation(value = "",notes = "登陆",tags = {"UserInformationController"})
    @PostMapping(value = "/login")
    public Result login(String username, String password, HttpSession session) {
        LoginInformation loginInformation = new LoginInformation();
        loginInformation.setAccount(username);
        loginInformation.setPassword(password);
        loginService.login(loginInformation , session);
        // 登录成功会返回Token给用户
        return new Result(true, StatusCode.OK,"登陆成功");
    }
}
