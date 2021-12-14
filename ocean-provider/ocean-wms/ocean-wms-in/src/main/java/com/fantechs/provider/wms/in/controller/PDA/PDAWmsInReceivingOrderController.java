package com.fantechs.provider.wms.in.controller.PDA;

import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInReceivingOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInReceivingOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.in.service.WmsInReceivingOrderDetService;
import com.fantechs.provider.wms.in.service.WmsInReceivingOrderService;
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
 * @Date 2021/12/14
 */
@RestController
@Api(tags = "PDA收货作业")
@RequestMapping("/PDAwmsInReceivingOrder")
@Validated
public class PDAWmsInReceivingOrderController {

    @Resource
    private WmsInReceivingOrderService wmsInReceivingOrderService;
    @Resource
    private WmsInReceivingOrderDetService wmsInReceivingOrderDetService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInReceivingOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInReceivingOrder searchWmsInReceivingOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInReceivingOrder.getStartPage(),searchWmsInReceivingOrder.getPageSize());
        List<WmsInReceivingOrderDto> list = wmsInReceivingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findDetList")
    public ResponseEntity<List<WmsInReceivingOrderDetDto>> findDetList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInReceivingOrderDet searchWmsInReceivingOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInReceivingOrderDet.getStartPage(),searchWmsInReceivingOrderDet.getPageSize());
        List<WmsInReceivingOrderDetDto> list = wmsInReceivingOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInReceivingOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInReceivingOrder wmsInReceivingOrder) {
        return ControllerUtil.returnCRUD(wmsInReceivingOrderService.save(wmsInReceivingOrder));
    }

    @ApiOperation("提交")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInReceivingOrder.update.class) WmsInReceivingOrder wmsInReceivingOrder) {
        return ControllerUtil.returnCRUD(wmsInReceivingOrderService.update(wmsInReceivingOrder));
    }
}
