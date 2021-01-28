package com.fantechs.provider.api.qms;

import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsQualityConfirmation;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ocean-qms")
public interface QmsFeignApi {

    @ApiOperation("列表")
    @PostMapping("/qmsQualityConfirmation/findList")
    ResponseEntity<List<QmsQualityConfirmationDto>> findQualityConfirmationList(@ApiParam(value = "查询对象") @RequestBody SearchQmsQualityConfirmation searchQmsQualityConfirmation);
}