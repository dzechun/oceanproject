package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.in.service.WmsInAsnOrderDetService;
import com.fantechs.provider.wms.in.service.WmsInAsnOrderService;
import com.fantechs.provider.wms.in.service.WmsInTransferService;
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
 * @Author mr.lei
 * @Date 2021/6/17
 */
@RestController
@Api(tags = "调拨入库")
@RequestMapping("/wmsInTransfer")
@Validated
public class WmsInTransferController {
    @Resource
    private WmsInTransferService wmsInTransferService;
    @Resource
    private WmsInAsnOrderService wmsInAsnOrderService;
    @Resource
    private WmsInAsnOrderDetService wmsInAsnOrderDetService;

    @ApiOperation("调拨入库列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInAsnOrderDto>> findList(@RequestBody(required = false) SearchWmsInAsnOrder searchWmsInAsnOrder){
        Page<Object> page = PageHelper.startPage(searchWmsInAsnOrder.getStartPage(),searchWmsInAsnOrder.getPageSize());
        List<WmsInAsnOrderDto> list = wmsInTransferService.findList(searchWmsInAsnOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("调拨入库明细列表")
    @PostMapping("/findDetList")
    public ResponseEntity<List<WmsInAsnOrderDetDto>> findDetList(@RequestBody(required = false) SearchWmsInAsnOrderDet searchWmsInAsnOrderDet){
        Page<Object> page = PageHelper.startPage(searchWmsInAsnOrderDet.getStartPage(),searchWmsInAsnOrderDet.getPageSize());
        List<WmsInAsnOrderDetDto> list = wmsInAsnOrderDetService.findList(searchWmsInAsnOrderDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("新增调拨单")
    @PostMapping("/save")
    public ResponseEntity save(@RequestBody WmsInAsnOrder wmsInAsnOrder){
        return ControllerUtil.returnCRUD(wmsInTransferService.save(wmsInAsnOrder));
    }

    @ApiOperation("整单收货")
    @PostMapping("/allReceiving")
    public ResponseEntity allReceiving(@ApiParam(value = "对象ID列表，多个逗号分隔")@RequestParam @NotNull(message = "id不能为空")String ids){
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.allReceiving(ids,null));
    }

    @ApiOperation("单一收货")
    @PostMapping("/singleReceiving")
    public ResponseEntity singleReceiving(@RequestBody WmsInAsnOrderDet wmsInAsnOrderDet){
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.singleReceiving(wmsInAsnOrderDet));
    }

    @ApiOperation("创建作业单")
    @PostMapping("/createInnerJobOrder")
    public ResponseEntity createInnerJobOrder(@ApiParam(value = "完工入库id")@RequestParam Long asnOrderId){
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.createInnerJobOrder(asnOrderId));
    }
}
