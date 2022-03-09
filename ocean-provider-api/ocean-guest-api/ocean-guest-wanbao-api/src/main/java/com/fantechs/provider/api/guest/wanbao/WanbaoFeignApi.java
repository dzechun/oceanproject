package com.fantechs.provider.api.guest.wanbao;

import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.wanbao.QmsInspectionOrderDetSample;
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

}
