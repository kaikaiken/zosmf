package com.zos.zosmf.service;

import com.zos.zosmf.pojo.LoginInformation;

import javax.servlet.http.HttpSession;

public interface LoginService {
    //登陆
    String login(LoginInformation loginInformation , HttpSession session);
}
