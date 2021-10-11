package com.fantechs.provider.chinafiveringapi.api.controller;


import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.chinafiveringapi.api.service.ImportDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "中国五环")
@RequestMapping("/webServiceImport")
public class ChinaFiveRingImportController {

    @Resource
    private ImportDataService importDataService;

    @ApiOperation("获取合同量单")
    @PostMapping("/getPoDetails")
    //@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID
    public ResponseEntity<String> getPoDetails() throws Exception {
        String projectID="3919";
        BaseExecuteResultDto result = importDataService.getPoDetails(projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("获取领料单")
    @PostMapping("/getIssueDetails")
    //@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID
    public ResponseEntity<String> getIssueDetails() throws Exception {
        String projectID="3919";
        BaseExecuteResultDto result = importDataService.getIssueDetails(projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
        //return  ControllerUtil.returnSuccess();
    }

    @ApiOperation("获取材料信息")
    @PostMapping("/getPartNoInfo")
    //@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID
    public ResponseEntity<String> getPartNoInfo() throws Exception {
        String projectID="3919";
        BaseExecuteResultDto result = importDataService.getPartNoInfo(projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("获取货架信息")
    @PostMapping("/getShelvesNo")
    //@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID
    public ResponseEntity<String> getShelvesNo() throws Exception {
        String projectID="3919";
        BaseExecuteResultDto result = importDataService.getShelvesNo(projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("获取施工单位信息")
    @PostMapping("/getSubcontractor")
    //@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID
    public ResponseEntity<String> getSubcontractor() throws Exception {
        String projectID="3919";
        BaseExecuteResultDto result = importDataService.getSubcontractor(projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("获取供应商信息")
    @PostMapping("/getVendor")
    public ResponseEntity<String> getVendor() throws Exception {
        BaseExecuteResultDto result = importDataService.getVendor();
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("获取请购单信息")
    @PostMapping("/getReqDetails")
    //@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID
    public ResponseEntity<String> getReqDetails() throws Exception {
        String projectID="3919";
        BaseExecuteResultDto result = importDataService.getReqDetails(projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

}
