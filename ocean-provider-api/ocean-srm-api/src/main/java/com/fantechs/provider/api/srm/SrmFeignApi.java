package com.fantechs.provider.api.srm;

import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmInAsnOrder;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ocean-srm")
public interface SrmFeignApi {

    @ApiOperation("预收货通知单列表")
    @PostMapping("/srmInAsnOrder/findList")
    ResponseEntity<List<SrmInAsnOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmInAsnOrder searchSrmInAsnOrder);

}
