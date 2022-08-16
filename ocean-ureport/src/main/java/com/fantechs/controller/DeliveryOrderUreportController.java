package com.fantechs.controller;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.service.DeliveryOrderUreportService;
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
 * Created by hyc on 2021/03/23.
 */
@RestController
@Api(tags = "成品出库单报表")
@RequestMapping("/deliveryOrderUreport")
@Validated
public class DeliveryOrderUreportController {

    @Resource
    private DeliveryOrderUreportService deliveryOrderUreportService;

    @ApiOperation("成品出库单报表查询")
    @PostMapping("findList")
    public ResponseEntity<List<WmsOutDeliveryOrderDto>> list(
            @ApiParam(value = "查询条件，请参考Model说明") @RequestBody(required = false) SearchWmsOutDeliveryOrder searchWmsOutDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryOrder.getStartPage(), searchWmsOutDeliveryOrder.getPageSize());
        List<WmsOutDeliveryOrderDto> list = deliveryOrderUreportService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }
}
