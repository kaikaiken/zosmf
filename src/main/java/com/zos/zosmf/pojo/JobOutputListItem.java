package com.zos.zosmf.pojo;

import lombok.Data;

@Data
public class JobOutputListItem {
    private String jobName;
    private String jobId;
    private int id;
    private String ddName;
    private String subSystem;
    private String stepName;
    private String output;
}
