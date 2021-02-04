package com.fantechs.provider.api.base;

import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.entity.basic.BaseStaffProcess;
import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ocean-base")
public interface BaseFeignApi {

    @ApiOperation("页签信息列表")
    @PostMapping("/baseTab/findList")
    ResponseEntity<List<BaseTabDto>> findTabList(@ApiParam(value = "查询对象") @RequestBody SearchBaseTab searchBaseTab);

    @ApiOperation(value = "新增页签", notes = "新增页签")
    @PostMapping("/baseTab/add")
    ResponseEntity addTab(@ApiParam(value = "必传：", required = true) @RequestBody @Validated BaseTab baseTab);

    @ApiOperation("删除页签")
    @PostMapping("/baseTab/delete")
    ResponseEntity deleteTab(@ApiParam(value = "页签集合") @RequestBody @Validated List<BaseTab> baseTabs);

    @ApiOperation("修改页签")
    @PostMapping("/baseTab/update")
    ResponseEntity updateTab(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BaseTab.update.class) BaseTab baseTab);

    @ApiOperation("列表")
    @PostMapping("/baseTeam/findList")
    ResponseEntity<List<BaseTeamDto>> findTeamList(@ApiParam(value = "查询对象") @RequestBody SearchBaseTeam searchBaseTeam);

    @ApiOperation("列表")
    @PostMapping("/basePlateParts/findList")
    ResponseEntity<List<BasePlatePartsDto>> findPlatePartsList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlateParts searchBasePlateParts);

    @ApiOperation("列表")
    @PostMapping("/basePlatePartsDet/findList")
    ResponseEntity<List<BasePlatePartsDetDto>> findPlatePartsDetList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlatePartsDet searchBasePlatePartsDet);

    @ApiOperation("列表")
    @PostMapping("/baseUnitPrice/findList")
    ResponseEntity<List<BaseUnitPriceDto>> findUnitPriceList(@ApiParam(value = "查询对象")@RequestBody SearchBaseUnitPrice searchBaseUnitPrice);

    @ApiOperation("查询产品族信息列表")
    @PostMapping("/baseProductFamily/findList")
    ResponseEntity<List<BaseProductFamilyDto>> findProductFamilyList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductFamily searchBaseProductFamily);

    @ApiOperation("查询员工工种关系列表")
    @PostMapping("/baseStaffProcess/findList")
    ResponseEntity<List<BaseStaffProcess>> findStaffProcessList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStaffProcess searchBaseStaffProcess);
}
