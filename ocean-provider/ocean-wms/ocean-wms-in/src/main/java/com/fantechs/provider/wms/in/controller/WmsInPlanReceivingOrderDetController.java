package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtPlanReceivingOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInPlanReceivingOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.in.service.WmsInHtPlanReceivingOrderDetService;
import com.fantechs.provider.wms.in.service.WmsInPlanReceivingOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@RestController
@Api(tags = "收货计划明细")
@RequestMapping("/wmsInPlanReceivingOrderDet")
@Validated
@Slf4j
public class WmsInPlanReceivingOrderDetController {

    @Resource
    private WmsInPlanReceivingOrderDetService wmsInPlanReceivingOrderDetService;
    @Resource
    private WmsInHtPlanReceivingOrderDetService wmsInHtPlanReceivingOrderDetService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInPlanReceivingOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInPlanReceivingOrderDet searchWmsInPlanReceivingOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInPlanReceivingOrderDet.getStartPage(),searchWmsInPlanReceivingOrderDet.getPageSize());
        List<WmsInPlanReceivingOrderDetDto> list = wmsInPlanReceivingOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInPlanReceivingOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInHtPlanReceivingOrderDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInPlanReceivingOrderDet searchWmsInPlanReceivingOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInPlanReceivingOrderDet.getStartPage(),searchWmsInPlanReceivingOrderDet.getPageSize());
        List<WmsInHtPlanReceivingOrderDetDto> list = wmsInHtPlanReceivingOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInPlanReceivingOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
