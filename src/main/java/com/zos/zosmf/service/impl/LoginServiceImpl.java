package com.zos.zosmf.service.impl;

import com.zos.zosmf.pojo.LoginInformation;
import com.zos.zosmf.service.LoginService;
import com.zos.zosmf.utils.SslUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public String login(LoginInformation loginInformation , HttpSession session){
        try {
            Object ZOSMF_JSESSIONID = session.getAttribute("ZOSMF_JSESSIONID");
            Object ZOSMF_LtpaToken2;
            Object ZOSMF_Address;
            Object ZOSMF_Account;

            //获取zosmf地址
            ZOSMF_Address = "?";
            //获取登录账户名
            ZOSMF_Account = loginInformation.getAccount().toUpperCase();
            //禁用ssl证书校验
            CloseableHttpClient httpClient = SslUtil.SslHttpClientBuild();
            HttpComponentsClientHttpRequestFactory requestFactory
                    = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            //访问zosmf获取jsessionid
            String zosmfUrlOverHttps = "https://" + ZOSMF_Address.toString() + "/zosmf/";
            HttpHeaders httpHeaders = new RestTemplate(requestFactory).headForHeaders(zosmfUrlOverHttps);
            List<String> setCookie = httpHeaders.get("Set-Cookie");
            if (setCookie != null) {
                ZOSMF_JSESSIONID = setCookie.get(0).split(";")[0];
            } else {
                System.out.println("header中没有获取到set-cookie信息");
            }
            //访问zosmf获取token
            String loginUrlOverHttps = zosmfUrlOverHttps + "LoginServlet";
            //设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            if (ZOSMF_JSESSIONID != null) {
                headers.add("Cookie", ZOSMF_JSESSIONID.toString());
            }
            headers.add("Referer", zosmfUrlOverHttps);//欺骗服务器这不是csrf这不是csrf这不是csrf
            //添加表单数据
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("requestType", "Login");
            map.add("username", loginInformation.getAccount());
            map.add("password", loginInformation.getPassword());
            //request
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = new RestTemplate(requestFactory).postForEntity(loginUrlOverHttps, request, String.class);
            if (!response.toString().contains("Set-Cookie:")) {
                return "unauthorized";
            }
            ZOSMF_LtpaToken2 = response.toString().split("Set-Cookie:\"|; Path")[1];

            session.setAttribute("ZOSMF_JSESSIONID", ZOSMF_JSESSIONID);
            session.setAttribute("ZOSMF_LtpaToken2", ZOSMF_LtpaToken2);
            session.setAttribute("ZOSMF_Address", ZOSMF_Address);
            session.setAttribute("ZOSMF_Account", ZOSMF_Account);

            return "successful";
        } catch (Exception e) {
            e.printStackTrace();
            return "time out";
        }
    }
}
