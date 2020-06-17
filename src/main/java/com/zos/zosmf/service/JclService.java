package com.zos.zosmf.service;

import com.zos.zosmf.pojo.JobOutputListItem;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface JclService {
    List<JobOutputListItem> submitJCL(HttpSession session, String jcl);
}
