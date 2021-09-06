package com.fantechs.provider.api.guest.fivering;

import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

@FeignClient(name = "ocean-chinafivering-api")
public interface FiveringFeignApi {

    @ApiOperation("获取合同量单")
    @PostMapping("/webServiceImport/getPoDetails")
    ResponseEntity<String> getPoDetails(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

    @ApiOperation("获取领料单")
    @PostMapping("/webServiceImport/getIssueDetails")
    ResponseEntity<String> getIssueDetails(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

    @ApiOperation("获取材料信息")
    @PostMapping("/webServiceImport/getPartNoInfo")
    ResponseEntity<String> getPartNoInfo(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

    @ApiOperation("获取货架信息")
    @PostMapping("/webServiceImport/getShelvesNo")
    ResponseEntity<String> getShelvesNo(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

    @ApiOperation("获取客户信息")
    @PostMapping("/webServiceImport/getSubcontractor")
    ResponseEntity<String> getSubcontractor(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

    @ApiOperation("获取供应商信息")
    @PostMapping("/webServiceImport/getVendor")
    ResponseEntity<String> getVendor();

    @ApiOperation("获取请购单信息")
    @PostMapping("/webServiceImport/getReqDetails")
    ResponseEntity<String> getReqDetails(@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

    @ApiOperation("入库单回传接口")
    @PostMapping("/webServiceExport/writeDeliveryDetails")
    ResponseEntity<String> writeDeliveryDetails(@ApiParam(value = "jsonVoiceArray",required = true)@RequestParam @NotNull(message="jsonVoiceArray不能为空") String jsonVoiceArray,@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

    @ApiOperation("盘点单回传接口")
    @PostMapping("/webServiceExport/writeMakeInventoryDetails")
    ResponseEntity<String> writeMakeInventoryDetails(@ApiParam(value = "jsonVoiceArray",required = true)@RequestParam @NotNull(message="jsonVoiceArray不能为空") String jsonVoiceArray,@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

    @ApiOperation("出库单回传接口")
    @PostMapping("/webServiceExport/writeIssueDetails")
    ResponseEntity<String> writeIssueDetails(@ApiParam(value = "jsonVoiceArray",required = true)@RequestParam @NotNull(message="jsonVoiceArray不能为空") String jsonVoiceArray,@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

    @ApiOperation("调拨单回传接口")
    @PostMapping("/webServiceExport/writeMoveInventoryDetails")
    ResponseEntity<String> writeMoveInventoryDetails(@ApiParam(value = "jsonVoiceArray",required = true)@RequestParam @NotNull(message="jsonVoiceArray不能为空") String jsonVoiceArray,@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

    @ApiOperation("箱单回传接口")
    @PostMapping("/webServiceExport/writePackingLists")
    ResponseEntity<String> writePackingLists(@ApiParam(value = "jsonVoiceArray",required = true)@RequestParam @NotNull(message="jsonVoiceArray不能为空") String jsonVoiceArray,@ApiParam(value = "projectID",required = true)@RequestParam @NotNull(message="projectID不能为空") String projectID);

}
