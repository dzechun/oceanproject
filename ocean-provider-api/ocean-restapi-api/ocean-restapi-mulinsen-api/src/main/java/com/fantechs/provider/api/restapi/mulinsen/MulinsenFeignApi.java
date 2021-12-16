package com.fantechs.provider.api.restapi.mulinsen;

import com.fantechs.common.base.general.dto.mulinsen.HrUserInfoDto;
import com.fantechs.common.base.general.dto.mulinsen.NccBdMaterialDto;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchHrUserInfo;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchNccBdMaterial;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ocean-mulinsen-api")
public interface MulinsenFeignApi {

    @ApiOperation("列表")
    @PostMapping("/nccBdMaterial/findList")
    ResponseEntity<List<NccBdMaterialDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchNccBdMaterial searchNccBdMaterial);

    @ApiOperation("列表")
    @PostMapping("/hrUserInfo/findList")
    ResponseEntity<List<HrUserInfoDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchHrUserInfo searchHrUserInfo);
}
