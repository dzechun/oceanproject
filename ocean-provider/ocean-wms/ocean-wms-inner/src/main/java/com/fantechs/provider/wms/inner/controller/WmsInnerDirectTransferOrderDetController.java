package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerDirectTransferOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerDirectTransferOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerDirectTransferOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */
@RestController
@Api(tags = "直接调拨单详情")
@RequestMapping("/wmsInnerDirectTransferOrderDet")
@Validated
public class WmsInnerDirectTransferOrderDetController {

    @Resource
    private WmsInnerDirectTransferOrderDetService wmsInnerDirectTransferOrderDetService;

/*    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerDirectTransferOrderDet wmsInnerDirectTransferOrderDet) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderDetService.save(wmsInnerDirectTransferOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerDirectTransferOrderDet.update.class) WmsInnerDirectTransferOrderDet wmsInnerDirectTransferOrderDet) {
        return ControllerUtil.returnCRUD(wmsInnerDirectTransferOrderDetService.update(wmsInnerDirectTransferOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerDirectTransferOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerDirectTransferOrderDet  wmsInnerDirectTransferOrderDet = wmsInnerDirectTransferOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerDirectTransferOrderDet,StringUtils.isEmpty(wmsInnerDirectTransferOrderDet)?0:1);
    }*/

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerDirectTransferOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerDirectTransferOrderDet searchWmsInnerDirectTransferOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerDirectTransferOrderDet.getStartPage(),searchWmsInnerDirectTransferOrderDet.getPageSize());
        List<WmsInnerDirectTransferOrderDetDto> list = wmsInnerDirectTransferOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerDirectTransferOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInnerDirectTransferOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerDirectTransferOrderDet searchWmsInnerDirectTransferOrderDet) {
        List<WmsInnerDirectTransferOrderDetDto> list = wmsInnerDirectTransferOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerDirectTransferOrderDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

/*    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInnerDirectTransferOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerDirectTransferOrderDet searchWmsInnerDirectTransferOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerDirectTransferOrderDet.getStartPage(),searchWmsInnerDirectTransferOrderDet.getPageSize());
        List<WmsInnerDirectTransferOrderDet> list = wmsInnerDirectTransferOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerDirectTransferOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerDirectTransferOrderDet searchWmsInnerDirectTransferOrderDet){
    List<WmsInnerDirectTransferOrderDetDto> list = wmsInnerDirectTransferOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerDirectTransferOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerDirectTransferOrderDet信息", WmsInnerDirectTransferOrderDetDto.class, "WmsInnerDirectTransferOrderDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }*/

}
