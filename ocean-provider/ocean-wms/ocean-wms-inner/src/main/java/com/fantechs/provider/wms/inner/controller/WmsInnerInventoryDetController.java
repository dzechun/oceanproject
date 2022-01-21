package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/6/2
 */
@RestController
@Api(tags = "库存明细查询")
@RequestMapping("/wmsInnerInventoryDet")
@Validated
public class WmsInnerInventoryDetController {

    @Resource
    private WmsInnerInventoryDetService wmsInnerInventoryDetService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerInventoryDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventoryDet searchWmsInnerInventoryDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInventoryDet.getStartPage(),searchWmsInnerInventoryDet.getPageSize());
        List<WmsInnerInventoryDetDto> list = wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("加库存明细")
    @PostMapping("/add")
    public ResponseEntity add(@RequestBody List<WmsInnerInventoryDet> wmsInnerInventoryDets){
        return ControllerUtil.returnCRUD(wmsInnerInventoryDetService.add(wmsInnerInventoryDets));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= WmsInnerInventoryDet.update.class) WmsInnerInventoryDet wmsInnerInventoryDet) {
        return ControllerUtil.returnCRUD(wmsInnerInventoryDetService.update(wmsInnerInventoryDet));
    }

    @ApiOperation("减库存明细")
    @PostMapping("/subtract")
    public ResponseEntity subtract(@RequestBody WmsInnerInventoryDetDto wmsInnerInventoryDetDto){
        return ControllerUtil.returnCRUD(wmsInnerInventoryDetService.subtract(wmsInnerInventoryDetDto));
    }

    @ApiOperation("按条件查询明细")
    @PostMapping("/findByDet")
    public ResponseEntity<WmsInnerInventoryDet> findByDet(@RequestParam String barCode){
        WmsInnerInventoryDet wmsInnerInventoryDet = wmsInnerInventoryDetService.findByOne(barCode);
        return ControllerUtil.returnDataSuccess(wmsInnerInventoryDet, StringUtils.isEmpty(wmsInnerInventoryDet)?0:1);
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerInventoryDet searchWmsInnerInventoryDet){
        List<WmsInnerInventoryDetDto> list = wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryDet));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "库存明细", "库存明细", WmsInnerInventoryDetDto.class, "库存明细.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }


    @ApiOperation("直接调拨查询库位列表")
    @PostMapping("/findListByBarCode")
    public ResponseEntity<List<WmsInnerInventoryDetDto>> findListByBarCode(@ApiParam(value = "查询对象")@RequestBody List<String> codes) {
        List<WmsInnerInventoryDetDto> list = wmsInnerInventoryDetService.findListByBarCode(codes);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }


    @ApiOperation("查询是否整单发货")
    @PostMapping("/isAllOutInventory")
    public ResponseEntity isAllOutInventory(@RequestBody(required = true) List<WmsInnerInventoryDetDto> list) {
        return ControllerUtil.returnCRUD(wmsInnerInventoryDetService.isAllOutInventory(list));
    }
}
