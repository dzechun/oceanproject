package com.fantechs.provider.api.qms;

import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDto;
import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import com.fantechs.common.base.general.entity.qms.search.*;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "ocean-qms")
public interface QmsFeignApi {

    @ApiOperation("列表")
    @PostMapping("/qmsQualityConfirmation/findList")
    ResponseEntity<List<QmsQualityConfirmationDto>> findQualityConfirmationList(@ApiParam(value = "查询对象") @RequestBody SearchQmsQualityConfirmation searchQmsQualityConfirmation);

    @ApiOperation("获取品质数量")
    @PostMapping("/qmsQualityConfirmation/getQualityQuantity")
    ResponseEntity<QmsQualityConfirmation> getQualityQuantity(@ApiParam(value = "workOrderCardPoolId", required = true) @RequestParam Long workOrderCardPoolId, @ApiParam(value = "workOrderCardPoolId", required = true) @RequestParam Long processId);

    @ApiOperation("获取pda质检列表")
    @PostMapping("/qmsPdaInspection/findList")
    ResponseEntity<List<QmsPdaInspectionDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchQmsPdaInspection searchQmsPdaInspection);

    @ApiOperation("条码走产线，自动复检")
    @PostMapping("/qmsInspectionOrder/recheckByBarcode")
    ResponseEntity recheckByBarcode(@ApiParam(value = "条码",required = true) @RequestParam @NotNull(message="条码不能为空") String barcode);
}

