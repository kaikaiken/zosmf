package com.zos.zosmf.controller;

import com.zos.zosmf.pojo.DatasetInfo;
import com.zos.zosmf.service.DatasetService;
import com.zos.zosmf.service.LoginService;
import com.zos.zosmf.utils.Result;
import com.zos.zosmf.utils.StatusCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class DatasetController {

    @Resource
    private DatasetService datasetService;

    @Resource
    private LoginService loginService;

    /**
     * 新建数据集
     */
    @ApiOperation(value = "新建数据集",notes = "新建数据集",tags = {"DatasetController"})
    @PostMapping(value = "/createDataset")
    public Result createDataset(@RequestBody @ApiParam(name = "DatasetInfo对象",value = "传入JSON数据",required = true) DatasetInfo datasetInfo, HttpSession session) {
        if(loginService.notLogin(session)){
            return new Result(true, StatusCode.ERROR,"请先登陆");
        }
        if (datasetService.createDataset(session, datasetInfo)) {
            return new Result(true, StatusCode.OK,"新建成功");
        }
        return new Result(true, StatusCode.ERROR,"新建失败");
    }

    /***
     * 查询数据集内容
     */
    @ApiOperation(value = "查询数据集内容",notes = "查询数据集内容",tags = {"DatasetController"})
    @ApiImplicitParam(paramType = "path", name = "datasetName", value = "数据集名字", required = true, dataType = "String")
    @GetMapping("/dataset/{datasetName}")
    public Result getContent(@PathVariable String datasetName, HttpSession session) {
        if(loginService.notLogin(session)){
            return new Result(true, StatusCode.ERROR,"请先登陆");
        }
        String content = datasetService.getContent(session , datasetName);
        if(content == null){
            return new Result(true, StatusCode.ERROR,"查看失败");
        }
        return new Result(true, StatusCode.OK,"查看成功",content);
    }

    /***
     * 删除数据集
     */
    @ApiOperation(value = "删除数据集",notes = "删除数据集",tags = {"DatasetController"})
    @ApiImplicitParam(paramType = "path", name = "datasetName", value = "数据集名字", required = true, dataType = "String")
    @DeleteMapping(value = "/dataset/{datasetName}" )
    public Result delete(HttpSession session , @PathVariable String datasetName) {
        if(loginService.notLogin(session)){
            return new Result(true, StatusCode.ERROR,"请先登陆");
        }
        if(datasetService.deleteDataset(session , datasetName)){
            return new Result(true,StatusCode.OK,"删除成功");
        }
        return new Result(true, StatusCode.ERROR,"删除失败");
    }

    /***
     * 查询数据集的成员
     */
    @ApiOperation(value = "查询数据集的成员",notes = "查询数据集的成员",tags = {"DatasetController"})
    @ApiImplicitParam(paramType = "path", name = "datasetName", value = "数据集名字", required = true, dataType = "String")
    @GetMapping("/datasetMember/{datasetName}")
    public Result getMemberList(@PathVariable String datasetName, HttpSession session) {
        if(loginService.notLogin(session)){
            return new Result(true, StatusCode.ERROR,"请先登陆");
        }
        List<String> members = datasetService.getMemberList(session , datasetName);
        if(members == null){
            return new Result(true, StatusCode.ERROR,"查看失败");
        }
        return new Result(true, StatusCode.OK,"查看成功",members);
    }

    /***
     * 查询数据集
     */
    @ApiOperation(value = "查询数据集",notes = "查询数据集",tags = {"DatasetController"})
    @ApiImplicitParam(paramType = "path", name = "datasetName", value = "数据集名字", required = true, dataType = "String")
    @GetMapping("/findDataset/{datasetName}")
    public Result getDatasetList(@PathVariable String datasetName, HttpSession session) {
        if(loginService.notLogin(session)){
            return new Result(true, StatusCode.ERROR,"请先登陆");
        }
        List<Map<String,String>> datasetList = datasetService.getDatasetList(session , datasetName);
        if(datasetList == null){
            return new Result(true, StatusCode.ERROR,"查询数据集失败");
        }
        return new Result(true, StatusCode.OK,"查看成功",datasetList);
    }

}
