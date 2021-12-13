package com.fantechs.provider.api.qms;

import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@FeignClient(name = "ocean-qms")
public interface QmsFeignApi {

    @ApiOperation("修改下推标识")
    @PostMapping("/qmsIncomingInspectionOrder/updateIfAllIssued")
    ResponseEntity updateIfAllIssued(@ApiParam(value = "对象，Id必传", required = true) @RequestBody QmsIncomingInspectionOrder qmsIncomingInspectionOrder);

    @ApiOperation(value = "来料检验单批量新增",notes = "来料检验单批量新增")
    @PostMapping("/qmsIncomingInspectionOrder/batchAdd")
    ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<QmsIncomingInspectionOrderDto> list);
}

