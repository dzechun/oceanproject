package com.fantechs.provider.api.wms.in;

import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hpsf.Decimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @Date 2021/1/7 19:28
 */
@FeignClient(name = "ocean-wms-in")
public interface InFeignApi {

    @ApiOperation(value = "入库计划新增",notes = "入库计划新增")
    @PostMapping("/wmsInInPlanOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInInPlanOrder wmsInInPlanOrder);

    @ApiOperation(value = "收货计划新增",notes = "收货计划新增")
    @PostMapping("/wmsInPlanReceivingOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInPlanReceivingOrder wmsInPlanReceivingOrder);

    @ApiOperation(value = "收货作业新增",notes = "收货作业新增")
    @PostMapping("/wmsInReceivingOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInReceivingOrder wmsInReceivingOrder);

    @ApiOperation(value = "更新入库计划上架数量",notes = "更新入库计划上架数量")
    @PostMapping("/wmsInInPlanOrder/updatePutawayQty")
    ResponseEntity updatePutawayQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long inPlanOrderDetId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty);
}