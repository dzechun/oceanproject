package com.fantechs.provider.chinafiveringapi.api.controller;


import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.chinafiveringapi.api.service.ImportDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@Api(tags = "中国五环")
@RequestMapping("/webService")
public class ChinaFiveRingImportController {

    @Resource
    private ImportDataService importDataService;

    @ApiOperation("获取合同量单")
    @PostMapping("/getPoDetails")
    public ResponseEntity<String> getPoDetails(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        BaseExecuteResultDto result = importDataService.getPoDetails(projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("获取领料单")
    @PostMapping("/getIssueDetails")
    public ResponseEntity<String> getIssueDetails(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        String  result = importDataService.getIssueDetails(projectID);
        return  ControllerUtil.returnDataSuccess(result, StringUtils.isEmpty(result)?0:1);
    }

    @ApiOperation("获取材料信息")
    @PostMapping("/getPartNoInfo")
    public ResponseEntity<String> getPartNoInfo(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        BaseExecuteResultDto result = importDataService.getPartNoInfo(projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("获取货架信息")
    @PostMapping("/getShelvesNo")
    public ResponseEntity<String> getShelvesNo(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        String  result = importDataService.getShelvesNo(projectID);
        return  ControllerUtil.returnDataSuccess(result, StringUtils.isEmpty(result)?0:1);
    }

    @ApiOperation("获取施工单位信息")
    @PostMapping("/getSubcontractor")
    public ResponseEntity<String> getSubcontractor(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        String  result = importDataService.getSubcontractor(projectID);
        return  ControllerUtil.returnDataSuccess(result, StringUtils.isEmpty(result)?0:1);
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
    public ResponseEntity<String> getReqDetails(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        BaseExecuteResultDto result = importDataService.getReqDetails(projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

}
