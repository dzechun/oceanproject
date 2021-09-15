package com.fantechs.provider.api.guest.eng;

import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.general.entity.eng.EngPurchaseReqOrder;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;

@FeignClient(name = "ocean-guest-eng")
public interface EngFeignApi {

    @ApiOperation("接口新增或修改合同量单信息")
    @PostMapping("/engContractQtyOrder/saveByApi")
    ResponseEntity saveByApi(@ApiParam(value = "必传：contractCode、dominantTermCode",required = true)@RequestBody @Validated EngContractQtyOrder engContractQtyOrder);

    @ApiOperation("接口新增或修改合同量单信息")
    @PostMapping("/engPurchaseReqOrder/saveByApi")
    ResponseEntity saveByApi(@ApiParam(value = "必传：purchaseReqOrderCode、materialCode",required = true)@RequestBody @Validated EngPurchaseReqOrder engPurchaseReqOrder);

    @ApiOperation("收货作业反写上架数量")
    @PostMapping("/engPackingOrderTask/writeQty")
    ResponseEntity writeQty(@RequestParam Long id, @RequestParam BigDecimal qty);
}
