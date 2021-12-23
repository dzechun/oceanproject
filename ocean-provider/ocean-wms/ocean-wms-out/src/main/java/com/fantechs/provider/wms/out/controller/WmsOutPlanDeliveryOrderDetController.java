package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanDeliveryOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutPlanDeliveryOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */
@RestController
@Api(tags = "出库计划明细")
@RequestMapping("/wmsOutPlanDeliveryOrderDet")
@Validated
public class WmsOutPlanDeliveryOrderDetController {

    @Resource
    private WmsOutPlanDeliveryOrderDetService wmsOutPlanDeliveryOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutPlanDeliveryOrderDet wmsOutPlanDeliveryOrderDet) {
        return ControllerUtil.returnCRUD(wmsOutPlanDeliveryOrderDetService.save(wmsOutPlanDeliveryOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutPlanDeliveryOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutPlanDeliveryOrderDet.update.class) WmsOutPlanDeliveryOrderDet wmsOutPlanDeliveryOrderDet) {
        return ControllerUtil.returnCRUD(wmsOutPlanDeliveryOrderDetService.update(wmsOutPlanDeliveryOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutPlanDeliveryOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutPlanDeliveryOrderDet  wmsOutPlanDeliveryOrderDet = wmsOutPlanDeliveryOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutPlanDeliveryOrderDet,StringUtils.isEmpty(wmsOutPlanDeliveryOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutPlanDeliveryOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutPlanDeliveryOrderDet searchWmsOutPlanDeliveryOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutPlanDeliveryOrderDet.getStartPage(),searchWmsOutPlanDeliveryOrderDet.getPageSize());
        List<WmsOutPlanDeliveryOrderDetDto> list = wmsOutPlanDeliveryOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanDeliveryOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsOutPlanDeliveryOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsOutPlanDeliveryOrderDet searchWmsOutPlanDeliveryOrderDet) {
        List<WmsOutPlanDeliveryOrderDetDto> list = wmsOutPlanDeliveryOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanDeliveryOrderDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutHtPlanDeliveryOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutPlanDeliveryOrderDet searchWmsOutPlanDeliveryOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutPlanDeliveryOrderDet.getStartPage(),searchWmsOutPlanDeliveryOrderDet.getPageSize());
        List<WmsOutHtPlanDeliveryOrderDet> list = wmsOutPlanDeliveryOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanDeliveryOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutPlanDeliveryOrderDet searchWmsOutPlanDeliveryOrderDet){
    List<WmsOutPlanDeliveryOrderDetDto> list = wmsOutPlanDeliveryOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanDeliveryOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutPlanDeliveryOrderDet信息", WmsOutPlanDeliveryOrderDetDto.class, "WmsOutPlanDeliveryOrderDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
