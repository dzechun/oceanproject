package com.fantechs.provider.api.qms;

import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ocean-qms")
public interface QmsFeignApi {

    @ApiOperation("修改下推标识")
    @PostMapping("/qmsIncomingInspectionOrder/updateIfAllIssued")
    ResponseEntity updateIfAllIssued(@ApiParam(value = "对象，Id必传", required = true) @RequestBody QmsIncomingInspectionOrder qmsIncomingInspectionOrder);

}

