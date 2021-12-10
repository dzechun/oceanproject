package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInInPlanOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInHtInPlanOrderDetService;
import com.fantechs.provider.wms.in.service.WmsInInPlanOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@RestController
@Api(tags = "入库计划详情表")
@RequestMapping("/wmsInInPlanOrderDet")
@Validated
@Slf4j
public class WmsInInPlanOrderDetController {

    @Resource
    private WmsInInPlanOrderDetService wmsInInPlanOrderDetService;
    @Resource
    private WmsInHtInPlanOrderDetService wmsInHtInPlanOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInInPlanOrderDet wmsInInPlanOrderDet) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderDetService.save(wmsInInPlanOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInInPlanOrderDet.update.class) WmsInInPlanOrderDet wmsInInPlanOrderDet) {
        return ControllerUtil.returnCRUD(wmsInInPlanOrderDetService.update(wmsInInPlanOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInInPlanOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInInPlanOrderDet  wmsInInPlanOrderDet = wmsInInPlanOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInInPlanOrderDet,StringUtils.isEmpty(wmsInInPlanOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInInPlanOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInInPlanOrderDet searchWmsInInPlanOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInInPlanOrderDet.getStartPage(),searchWmsInInPlanOrderDet.getPageSize());
        List<WmsInInPlanOrderDetDto> list = wmsInInPlanOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInInPlanOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInInPlanOrderDet searchWmsInInPlanOrderDet) {
        List<WmsInInPlanOrderDetDto> list = wmsInInPlanOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrderDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInInPlanOrderDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInInPlanOrderDet searchWmsInInPlanOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInInPlanOrderDet.getStartPage(),searchWmsInInPlanOrderDet.getPageSize());
        List<WmsInInPlanOrderDetDto> list = wmsInHtInPlanOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInInPlanOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
