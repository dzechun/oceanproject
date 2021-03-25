package com.fantechs.provider.api.qms;

import com.fantechs.common.base.general.dto.qms.QmsInspectionItemDto;
import com.fantechs.common.base.general.dto.qms.QmsInspectionTypeDto;
import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDto;
import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionItem;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionType;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsPdaInspection;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsQualityConfirmation;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ocean-qms")
public interface QmsFeignApi {

    @ApiOperation("列表")
    @PostMapping("/qmsQualityConfirmation/findList")
    ResponseEntity<List<QmsQualityConfirmationDto>> findQualityConfirmationList(@ApiParam(value = "查询对象") @RequestBody SearchQmsQualityConfirmation searchQmsQualityConfirmation);

    @ApiOperation("获取品质数量")
    @PostMapping("/qmsQualityConfirmation/getQualityQuantity")
    ResponseEntity<QmsQualityConfirmation> getQualityQuantity(@ApiParam(value = "workOrderCardPoolId", required = true) @RequestParam Long workOrderCardPoolId, @ApiParam(value = "workOrderCardPoolId", required = true) @RequestParam Long processId);

    @ApiOperation("获取检验项目列表")
    @PostMapping("/qmsInspectionItem/findList")
    ResponseEntity<List<QmsInspectionItemDto>> findInspectionItemList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionItem searchQmsInspectionItem);

    @ApiOperation("获取检验类型列表")
    @PostMapping("/qmsInspectionType/findList")
    ResponseEntity<List<QmsInspectionTypeDto>> findInspectionTypeList(@ApiParam(value = "查询对象") @RequestBody SearchQmsInspectionType searchQmsInspectionType);

    @ApiOperation("获取pda质检列表")
    @PostMapping("/qmsPdaInspection/findList")
    ResponseEntity<List<QmsPdaInspectionDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchQmsPdaInspection searchQmsPdaInspection);

}

