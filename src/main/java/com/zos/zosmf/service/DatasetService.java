package com.zos.zosmf.service;

import com.zos.zosmf.pojo.DatasetInfo;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface DatasetService {

    boolean createDataset(HttpSession session, DatasetInfo datasetInfo);

    String getContent(HttpSession session, String datasetName);

    boolean deleteDataset(HttpSession session, String datasetName);

    List<String> getMemberList(HttpSession session, String datasetName);

    List<Map<String, String>> getDatasetList(HttpSession session, String datasetName);
}
