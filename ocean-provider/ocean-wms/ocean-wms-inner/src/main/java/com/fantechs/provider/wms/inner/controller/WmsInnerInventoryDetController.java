package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
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

    @ApiOperation("减库存明细")
    @PostMapping("/subtract")
    public ResponseEntity subtract(@RequestBody List<WmsInnerInventoryDet> wmsInnerInventoryDets){
        return ControllerUtil.returnCRUD(wmsInnerInventoryDetService.subtract(wmsInnerInventoryDets));
    }
}
