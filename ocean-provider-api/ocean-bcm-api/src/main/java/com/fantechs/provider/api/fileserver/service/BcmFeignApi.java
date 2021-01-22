package com.fantechs.provider.api.fileserver.service;


import com.fantechs.common.base.general.dto.bcm.BcmBarCodeDto;
import com.fantechs.common.base.general.entity.bcm.BcmBarCode;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmBarCode;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by lfz on 2018/8/22.
 */
@FeignClient(value = "ocean-bcm")
public interface BcmFeignApi {

    @ApiOperation("列表")
    @PostMapping("/bcmBarCode/findList")
    ResponseEntity<List<BcmBarCodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBcmBarCode searchBcmBarCode);

    @ApiOperation("根据工单ID和条码内容修改条码状态")
    @PostMapping("/bcmBarCode/updateByContent")
    ResponseEntity updateByContent(@ApiParam(value = "查询对象")@RequestBody BcmBarCode bcmBarCode);

}
