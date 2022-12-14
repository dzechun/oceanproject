package com.fantechs.provider.api.guest.wanbao;

import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStacking;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.general.entity.wanbao.search.SearchQmsInspectionOrder;
import com.fantechs.common.base.general.entity.wanbao.search.SearchQmsInspectionOrderDetSample;
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

@FeignClient(name = "ocean-guest-wanbao")
public interface WanbaoFeignApi {

    @ApiOperation("页签信息列表")
    @PostMapping("/wanbaoStackingDet/batchAdd")
    ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<WanbaoStackingDet> list);

    @ApiOperation("页签信息列表")
    @PostMapping("/qmsInspectionOrderDetSample/findList")
    ResponseEntity<List<QmsInspectionOrderDetSample>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionOrderDetSample searchQmsInspectionOrderDetSample);

    @ApiOperation("成品检验单列表")
    @PostMapping("/qmsInspectionOrder/findList")
    ResponseEntity<List<QmsInspectionOrder>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionOrder searchQmsInspectionOrder);

    @ApiOperation("堆垛修改并清空明细表条码")
    @PostMapping("/wanbaoStacking/updateAndClearBarcode")
    ResponseEntity updateAndClearBarcode(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= WanbaoStacking.update.class) WanbaoStacking wanbaoStacking);

    @ApiOperation("获取堆垛详情")
    @PostMapping("/wanbaoStacking/detail")
    ResponseEntity<WanbaoStacking> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id);

    @ApiOperation("条码走产线，自动复检")
    @PostMapping("/qmsInspectionOrder/recheckByBarcode")
    ResponseEntity recheckByBarcode(@ApiParam(value = "条码",required = true) @RequestParam @NotNull(message="条码不能为空") String barcode);

}
