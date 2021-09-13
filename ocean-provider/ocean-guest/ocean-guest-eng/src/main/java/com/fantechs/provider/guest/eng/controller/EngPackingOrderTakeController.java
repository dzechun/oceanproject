package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.eng.search.SearchEngPackingOrder;
import com.fantechs.common.base.general.entity.eng.search.SearchEngPackingOrderSummary;
import com.fantechs.common.base.general.entity.eng.search.SearchEngPackingOrderSummaryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.guest.eng.service.EngPackingOrderSummaryDetService;
import com.fantechs.provider.guest.eng.service.EngPackingOrderSummaryService;
import com.fantechs.provider.guest.eng.service.EngPackingOrderTakeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/9/8
 */
@RestController
@Api(tags = "收货入库")
@RequestMapping("/engPackingOrderTask")
@Validated
public class EngPackingOrderTakeController {
    @Resource
    private EngPackingOrderTakeService engPackingOrderTakeService;
    @Resource
    private EngPackingOrderSummaryService engPackingOrderSummaryService;
    @Resource
    private EngPackingOrderSummaryDetService engPackingOrderSummaryDetService;

    @ApiOperation("包装清单")
    @PostMapping("/findList")
    public ResponseEntity<List<EngPackingOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngPackingOrder searchEngPackingOrder) {
        Page<Object> page = PageHelper.startPage(searchEngPackingOrder.getStartPage(),searchEngPackingOrder.getPageSize());
        List<EngPackingOrderDto> list = engPackingOrderTakeService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("包装清单装箱明细")
    @PostMapping("/findListSummary")
    public ResponseEntity<List<EngPackingOrderSummaryDto>> findListSummary(@ApiParam(value = "查询对象")@RequestBody SearchEngPackingOrderSummary searchEngPackingOrderSummary) {
        Page<Object> page = PageHelper.startPage(searchEngPackingOrderSummary.getStartPage(),searchEngPackingOrderSummary.getPageSize());
        List<EngPackingOrderSummaryDto> list = engPackingOrderSummaryService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrderSummary));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("包装清单装箱货品")
    @PostMapping("/findListSummaryDet")
    public ResponseEntity<List<EngPackingOrderSummaryDetDto>> findListSummaryDet(@ApiParam(value = "查询对象")@RequestBody SearchEngPackingOrderSummaryDet searchEngPackingOrderSummaryDet) {
        Page<Object> page = PageHelper.startPage(searchEngPackingOrderSummaryDet.getStartPage(),searchEngPackingOrderSummaryDet.getPageSize());
        List<EngPackingOrderSummaryDetDto> list = engPackingOrderSummaryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrderSummaryDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("登记")
    @PostMapping("/register")
    public ResponseEntity register(@ApiParam("id")@RequestBody List<Long> ids){
        return ControllerUtil.returnCRUD(engPackingOrderTakeService.register(ids));
    }

    @ApiOperation("整单收货")
    @PostMapping("/allTask")
    public ResponseEntity allTask(@RequestBody List<Long> ids){
        return ControllerUtil.returnCRUD(engPackingOrderTakeService.allTask(ids));
    }

    @ApiOperation("按箱收货")
    @PostMapping("/boxTask")
    public ResponseEntity boxTask(@RequestBody List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos){
        return ControllerUtil.returnCRUD(engPackingOrderTakeService.boxTask(engPackingOrderSummaryDtos));
    }

    @ApiOperation("按SKU收货")
    @PostMapping("/onlyTask")
    public ResponseEntity onlyTask(@RequestBody EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto){
        return ControllerUtil.returnCRUD(engPackingOrderTakeService.onlyTask(engPackingOrderSummaryDetDto));
    }

    @ApiOperation("创建上架单")
    @PostMapping("/createInnerJobOrder")
    public ResponseEntity createInnerJobOrder(@RequestBody List<Long> ids){
        return ControllerUtil.returnCRUD(engPackingOrderTakeService.createInnerJobOrder(ids));
    }

    @ApiOperation("整单取消")
    @PostMapping("/cancelAll")
    public ResponseEntity cancelAll(@RequestBody List<Long> ids){
        return ControllerUtil.returnCRUD(engPackingOrderTakeService.cancelAll(ids));
    }

    @ApiOperation("单一取消")
    @PostMapping("/onlyCancel")
    public ResponseEntity onlyCancel(@RequestBody EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto){
        return ControllerUtil.returnCRUD(engPackingOrderTakeService.onlyCancel(engPackingOrderSummaryDetDto));
    }

    @ApiIgnore
    @PostMapping("/writeQty")
    public ResponseEntity writeQty(@RequestParam Long id, @RequestParam BigDecimal qty){
        return ControllerUtil.returnCRUD(engPackingOrderTakeService.writeQty(id, qty));
    }
}
