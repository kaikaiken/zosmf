package com.zos.zosmf.service.impl;

import com.zos.zosmf.pojo.JobInfo;
import com.zos.zosmf.pojo.JobOutputListItem;
import com.zos.zosmf.service.JclService;
import com.zos.zosmf.utils.ZosmfUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JclServiceImpl implements JclService {

    @Override
    public List<JobOutputListItem> submitJCL(HttpSession session, String jcl) {
        // submit job
        JobInfo jobInfo = ZosmfUtil.go(session, "/zosmf/restjobs/jobs", HttpMethod.PUT, jcl, null, JobInfo.class);
        if (jobInfo != null && ZosmfUtil.isReady(session, "/zosmf/restjobs/jobs/" + jobInfo.getJobName() + "/" + jobInfo.getJobId(), 20)) {
            // get output list
            String outputListPath = "/zosmf/restjobs/jobs/" + jobInfo.getJobName() + "/" + jobInfo.getJobId() + "/files";
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> jobOutput = ZosmfUtil.go(session, outputListPath, HttpMethod.GET, null, null, List.class);
            List<JobOutputListItem> result = new ArrayList<>();

            for (Map<String, Object> map : jobOutput) {
                // resolve list item
                JobOutputListItem item = new JobOutputListItem();
                item.setId((int) map.get("id"));
                item.setDdName(map.get("ddname").toString());
                item.setJobId(map.get("jobid").toString());
                item.setJobName(map.get("jobname").toString());
                item.setStepName(map.get("stepname").toString());
                item.setSubSystem(map.get("subsystem").toString());
                String outputPath = outputListPath + "/" + item.getId() + "/records";
                // get output of every list item
                try {
                    item.setOutput(ZosmfUtil.go(session, outputPath, HttpMethod.GET, null, null, String.class));
                } catch (Exception e) {
                    item.setOutput("");
                    e.printStackTrace();
                }
                result.add(item);
            }
            return result;
        }
        return null;
    }
}
