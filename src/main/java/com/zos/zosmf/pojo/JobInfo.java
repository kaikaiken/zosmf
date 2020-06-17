package com.zos.zosmf.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class JobInfo {
    private String owner;
    @JsonAlias("jobname")
    private String jobName;
    @JsonAlias("jobid")
    private String jobId;
    private String type;
    private String status;
}
