package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/05/17.
 */
@RestController
@Api(tags = "销售出库明细控制器")
@RequestMapping("/wmsOutDeliveryOrderDet")
@Validated
public class WmsOutDeliveryOrderDetController {

    @Resource
    private WmsOutDeliveryOrderDetService wmsOutDeliveryOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderDetService.save(wmsOutDeliveryOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutDeliveryOrderDet.update.class) WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderDetService.update(wmsOutDeliveryOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutDeliveryOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutDeliveryOrderDet  wmsOutDeliveryOrderDet = wmsOutDeliveryOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutDeliveryOrderDet,StringUtils.isEmpty(wmsOutDeliveryOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutDeliveryOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryOrderDet.getStartPage(),searchWmsOutDeliveryOrderDet.getPageSize());
        List<WmsOutDeliveryOrderDetDto> list = wmsOutDeliveryOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /*@ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutDeliveryOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryOrderDet.getStartPage(),searchWmsOutDeliveryOrderDet.getPageSize());
        List<WmsOutDeliveryOrderDet> list = wmsOutDeliveryOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutDeliveryOrderDet searchWmsOutDeliveryOrderDet){
    List<WmsOutDeliveryOrderDetDto> list = wmsOutDeliveryOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "销售出库明细信息", WmsOutDeliveryOrderDetDto.class, "销售出库明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
