package com.fantechs.provider.api.eam;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipment;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ocean-eam")
public interface EamFeignApi {

    @ApiOperation("查询设备列表")
    @PostMapping("/eamEquipment/findList")
    ResponseEntity<List<EamEquipmentDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipment searchEamEquipment);
}
