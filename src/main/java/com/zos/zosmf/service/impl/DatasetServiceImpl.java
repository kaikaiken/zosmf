package com.zos.zosmf.service.impl;

import com.zos.zosmf.pojo.DatasetInfo;
import com.zos.zosmf.service.DatasetService;
import com.zos.zosmf.utils.ZosmfUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DatasetServiceImpl implements DatasetService {

    private static final String datasetApiPath = "/zosmf/restfiles/ds";

    //创建数据集
    @Override
    public boolean createDataset(HttpSession session, DatasetInfo datasetInfo) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ZosmfUtil.go(session, datasetApiPath + "/" + datasetInfo.getDsname(),
                    HttpMethod.POST,
                    datasetInfo,
                    headers,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //获取数据集内容
    @Override
    public String getContent(HttpSession session, String datasetName) {
        String content = "";
        try {
            content = ZosmfUtil.go(session, datasetApiPath + "/" + datasetName,
                    HttpMethod.GET,
                    null,
                    null,
                    String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return content;
    }

    //删除数据集
    @Override
    public boolean deleteDataset(HttpSession session, String datasetName) {
        try {
            ZosmfUtil.go(session,
                    datasetApiPath + "/" + datasetName,
                    HttpMethod.DELETE,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //获取分区数据集中的成员列表
    @Override
    @SuppressWarnings("unchecked")
    public List<String> getMemberList(HttpSession session, String datasetName) {
        List<String> names = new ArrayList<>();
        try {
            List<Map<String, String>> list = (List<Map<String, String>>) ZosmfUtil.go(session,
                    datasetApiPath + "/" + datasetName + "/member",
                    HttpMethod.GET,
                    null,
                    null,
                    Map.class).get("items");
            for (Map<String, String> m : list) {
                names.add(m.get("member"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return names;
    }

    //查询数据集
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getDatasetList(HttpSession session, String datasetName) {
        List<Map<String, String>> datasetList = new ArrayList<>();
        try {
            HttpHeaders headers = new HttpHeaders();
            // set header to query more info about dataset
            headers.add("X-IBM-Attributes", "base");
            datasetList = (List<Map<String, String>>) ZosmfUtil.go(
                    session,
                    datasetApiPath + "?dslevel=" + datasetName,
                    HttpMethod.GET,
                    null,
                    headers,
                    Map.class).get("items");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return datasetList;
    }
}
