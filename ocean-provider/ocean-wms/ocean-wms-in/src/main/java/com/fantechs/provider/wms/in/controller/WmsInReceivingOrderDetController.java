package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtReceivingOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInReceivingOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInHtReceivingOrderDetService;
import com.fantechs.provider.wms.in.service.WmsInReceivingOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by mr.lei on 2021/12/13.
 */
@RestController
@Api(tags = "收货作业明细")
@RequestMapping("/wmsInReceivingOrderDet")
@Validated
public class WmsInReceivingOrderDetController {

    @Resource
    private WmsInReceivingOrderDetService wmsInReceivingOrderDetService;
    @Resource
    private WmsInHtReceivingOrderDetService wmsInHtReceivingOrderDetService;

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInReceivingOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInReceivingOrderDet  wmsInReceivingOrderDet = wmsInReceivingOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInReceivingOrderDet,StringUtils.isEmpty(wmsInReceivingOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInReceivingOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInReceivingOrderDet searchWmsInReceivingOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInReceivingOrderDet.getStartPage(),searchWmsInReceivingOrderDet.getPageSize());
        List<WmsInReceivingOrderDetDto> list = wmsInReceivingOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInReceivingOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInReceivingOrderDet searchWmsInReceivingOrderDet) {
        List<WmsInReceivingOrderDetDto> list = wmsInReceivingOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrderDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInHtReceivingOrderDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInReceivingOrderDet searchWmsInReceivingOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInReceivingOrderDet.getStartPage(),searchWmsInReceivingOrderDet.getPageSize());
        List<WmsInHtReceivingOrderDetDto> list = wmsInHtReceivingOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
