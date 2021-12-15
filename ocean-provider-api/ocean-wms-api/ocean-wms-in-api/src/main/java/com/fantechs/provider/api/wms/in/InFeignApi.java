package com.fantechs.provider.api.wms.in;

import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInReceivingOrder;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Date 2021/1/7 19:28
 */
@FeignClient(name = "ocean-wms-in")
public interface InFeignApi {

    @ApiOperation(value = "入库计划新增",notes = "入库计划新增")
    @PostMapping("/wmsInInPlanOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInInPlanOrder wmsInInPlanOrder);

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/wmsInPlanReceivingOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInPlanReceivingOrder wmsInPlanReceivingOrder);

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/wmsInReceivingOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInReceivingOrder wmsInReceivingOrder);
}