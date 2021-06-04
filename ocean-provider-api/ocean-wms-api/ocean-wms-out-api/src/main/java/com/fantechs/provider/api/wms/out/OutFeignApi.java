package com.fantechs.provider.api.wms.out;


import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;


/**
 * @Data 2021-01-19 10:31
 */
@FeignClient(name = "ocean-wms-out")
public interface OutFeignApi {
    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/wmsOutDeliveryOrder/add")
    ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutDeliveryOrder wmsOutDeliveryOrder);

    @ApiOperation("修改销售出库明细")
    @PostMapping("/wmsOutDeliveryOrderDet/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutDeliveryOrderDet.update.class) WmsOutDeliveryOrderDet wmsOutDeliveryOrderDet);

    @ApiOperation("获取详情")
    @PostMapping("/wmsOutDeliveryOrderDet/detail")
    ResponseEntity<WmsOutDeliveryOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id);

}
