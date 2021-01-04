package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPurchaseReturnDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturnDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPurchaseReturnDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPurchaseReturnDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutPurchaseReturnDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/24.
 */
@RestController
@Api(tags = "采购退货明细控制器")
@RequestMapping("/wmsOutPurchaseReturnDet")
@Validated
public class WmsOutPurchaseReturnDetController {

    @Autowired
    private WmsOutPurchaseReturnDetService wmsOutPurchaseReturnDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutPurchaseReturnDet wmsOutPurchaseReturnDet) {
        return ControllerUtil.returnCRUD(wmsOutPurchaseReturnDetService.save(wmsOutPurchaseReturnDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutPurchaseReturnDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutPurchaseReturnDet.update.class) WmsOutPurchaseReturnDet wmsOutPurchaseReturnDet) {
        return ControllerUtil.returnCRUD(wmsOutPurchaseReturnDetService.update(wmsOutPurchaseReturnDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutPurchaseReturnDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutPurchaseReturnDet  wmsOutPurchaseReturnDet = wmsOutPurchaseReturnDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutPurchaseReturnDet,StringUtils.isEmpty(wmsOutPurchaseReturnDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutPurchaseReturnDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutPurchaseReturnDet searchWmsOutPurchaseReturnDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutPurchaseReturnDet.getStartPage(),searchWmsOutPurchaseReturnDet.getPageSize());
        List<WmsOutPurchaseReturnDetDto> list = wmsOutPurchaseReturnDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPurchaseReturnDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutHtPurchaseReturnDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutPurchaseReturnDet searchWmsOutPurchaseReturnDet) {
        Page<Object> page = PageHelper.startPage(searchWmsOutPurchaseReturnDet.getStartPage(),searchWmsOutPurchaseReturnDet.getPageSize());
        List<WmsOutHtPurchaseReturnDet> list = wmsOutPurchaseReturnDetService.findHTList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPurchaseReturnDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutPurchaseReturnDet searchWmsOutPurchaseReturnDet){
    List<WmsOutPurchaseReturnDetDto> list = wmsOutPurchaseReturnDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPurchaseReturnDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutPurchaseReturnDet信息", WmsOutPurchaseReturnDet.class, "WmsOutPurchaseReturnDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
