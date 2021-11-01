package com.fantechs.provider.kreport.controller;

import com.fantechs.common.base.general.entity.kreport.CarrierProcessingOrder;
import com.fantechs.common.base.general.entity.kreport.LogisticsKanban;
import com.fantechs.common.base.general.entity.kreport.TransportInformation;
import com.fantechs.common.base.general.entity.kreport.search.SearchLogisticsKanban;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.kreport.service.LogisticsKanbanService;
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

@RestController
@Api(tags = "物流看板")
@RequestMapping("/logisticsKanban")
@Validated
public class LogisticsKanbanController {

    @Resource
    private LogisticsKanbanService logisticsKanbanService;

    @ApiOperation("列表")
    @PostMapping("/findKanbanData")
    public ResponseEntity<LogisticsKanban> findKanbanData(@ApiParam(value = "查询对象")@RequestBody SearchLogisticsKanban searchLogisticsKanban) {
        Page<Object> page = PageHelper.startPage(searchLogisticsKanban.getStartPage(),searchLogisticsKanban.getPageSize());
        LogisticsKanban logisticsKanban = logisticsKanbanService.findKanbanData(ControllerUtil.dynamicConditionByEntity(searchLogisticsKanban));
        return ControllerUtil.returnDataSuccess(logisticsKanban,1);
    }

    @ApiOperation("获取运输地列表数据")
    @PostMapping("/findTransportInformationList")
    public ResponseEntity<List<TransportInformation>> findTransportInformationList(@ApiParam(value = "查询对象")@RequestBody SearchLogisticsKanban searchLogisticsKanban) {
        Page<Object> page = PageHelper.startPage(searchLogisticsKanban.getStartPage(),searchLogisticsKanban.getPageSize());
        List<TransportInformation> list = logisticsKanbanService.findTransportInformationList(ControllerUtil.dynamicConditionByEntity(searchLogisticsKanban));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("获取承运商列表数据")
    @PostMapping("/findLineChart")
    public ResponseEntity<List<CarrierProcessingOrder>> findLineChart(@ApiParam(value = "查询对象")@RequestBody SearchLogisticsKanban searchLogisticsKanban) {
        Page<Object> page = PageHelper.startPage(searchLogisticsKanban.getStartPage(),searchLogisticsKanban.getPageSize());
        List<CarrierProcessingOrder> list = logisticsKanbanService.findCarrierProcessingOrderList(ControllerUtil.dynamicConditionByEntity(searchLogisticsKanban));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
