package com.fantechs.provider.api.qms;

import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: bingo.ren
 * @Date: 2021/1/9 16:27
 * @Description:
 * @Version: 1.0
 */
@FeignClient(name = "ocean-om")
public interface OMFeignApi {

//    @ApiOperation("订单列表")
//    @PostMapping("/smtOrder/findList")
//    ResponseEntity<List<SmtOrderDto>> findOrderList(@ApiParam(value = "查询对象") @RequestBody SearchSmtOrder searchSmtOrder);

    @ApiOperation("修改订单")
    @PostMapping("/smtOrder/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody SmtOrder smtOrder);

    @ApiOperation("获取订单详情")
    @PostMapping("/smtOrder/detail")
    ResponseEntity<SmtOrder> detailSmtOrder(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id);

    @ApiOperation("修改订单明细")
    @PostMapping("/omSalesOrderDet/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= OmSalesOrderDet.update.class) OmSalesOrderDet omSalesOrderDet);
}
