package com.fantechs.provider.chinafiveringapi.api.controller;


import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.chinafiveringapi.api.service.ExportDataService;
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
@RequestMapping("/webServiceExport")
public class ChinaFiveRingExportController {

    @Resource
    private ExportDataService exportDataService;

    @ApiOperation("入库单回传接口")
    @PostMapping("/writeDeliveryDetails")
    public ResponseEntity<String> writeDeliveryDetails(@ApiParam(value = "jsonVoiceArray",required = true)@RequestParam @NotNull(message="jsonVoiceArray不能为空") String jsonVoiceArray,@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        BaseExecuteResultDto result = exportDataService.writeDeliveryDetails(jsonVoiceArray,projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("盘点单回传接口")
    @PostMapping("/writeMakeInventoryDetails")
    public ResponseEntity<String> writeMakeInventoryDetails(@ApiParam(value = "jsonVoiceArray",required = true)@RequestParam @NotNull(message="jsonVoiceArray不能为空") String jsonVoiceArray,@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        BaseExecuteResultDto result = exportDataService.writeMakeInventoryDetails(jsonVoiceArray,projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("出库单回传接口")
    @PostMapping("/writeIssueDetails")
    public ResponseEntity<String> writeIssueDetails(@ApiParam(value = "jsonVoiceArray",required = true)@RequestParam @NotNull(message="jsonVoiceArray不能为空") String jsonVoiceArray,@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        BaseExecuteResultDto result = exportDataService.writeIssueDetails(jsonVoiceArray,projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("调拨单回传接口")
    @PostMapping("/writeMoveInventoryDetails")
    public ResponseEntity<String> writeMoveInventoryDetails(@ApiParam(value = "jsonVoiceArray",required = true)@RequestParam @NotNull(message="jsonVoiceArray不能为空") String jsonVoiceArray,@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        BaseExecuteResultDto result = exportDataService.writeMoveInventoryDetails(jsonVoiceArray,projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

    @ApiOperation("箱单回传接口")
    @PostMapping("/writePackingLists")
    public ResponseEntity<String> writePackingLists(@ApiParam(value = "jsonVoiceArray",required = true)@RequestParam @NotNull(message="jsonVoiceArray不能为空") String jsonVoiceArray,@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID) throws Exception {
        BaseExecuteResultDto result = exportDataService.writePackingLists(jsonVoiceArray,projectID);
        String strResult= JsonUtils.objectToJson(result);
        return  ControllerUtil.returnDataSuccess(strResult, StringUtils.isEmpty(strResult)?0:1);
    }

}
