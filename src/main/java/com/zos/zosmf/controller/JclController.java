package com.zos.zosmf.controller;


import com.zos.zosmf.pojo.JobOutputListItem;
import com.zos.zosmf.pojo.LoginInformation;
import com.zos.zosmf.service.JclService;
import com.zos.zosmf.service.LoginService;
import com.zos.zosmf.utils.Result;
import com.zos.zosmf.utils.StatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin
public class JclController {

    @Resource
    private JclService jclService;

    @Resource
    private LoginService loginService;

    /**
     * 提交jcl
     */
    @ApiOperation(value = "提交jcl",notes = "提交jcl",tags = {"JclController"})
    @PostMapping(value = "/jcl")
    public Result submitJcl(String jcl, HttpSession session) {
        if(loginService.notLogin(session)){
            return new Result(true, StatusCode.ERROR,"请先登陆");
        }
        List<JobOutputListItem> jobOutputListItems = jclService.submitJCL(session , jcl);
        if(jobOutputListItems != null){
            return new Result(true, StatusCode.OK,"jcl提交成功",jobOutputListItems);
        }
        return new Result(true, StatusCode.ERROR,"jcl提交超时");

    }

}
