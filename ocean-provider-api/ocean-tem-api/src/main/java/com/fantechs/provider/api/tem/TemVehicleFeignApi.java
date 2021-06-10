package com.fantechs.provider.api.tem;

import com.fantechs.common.base.general.dto.tem.TemVehicleDto;
import com.fantechs.common.base.general.entity.tem.TemVehicle;
import com.fantechs.common.base.general.entity.tem.search.SearchTemVehicle;
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

@FeignClient(value = "ocean-tem", contextId = "tem")
public interface TemVehicleFeignApi {

    @ApiOperation("修改")
    @PostMapping("/temVehicle/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= TemVehicle.update.class) TemVehicle temVehicle);

    @ApiOperation("获取详情")
    @PostMapping("/temVehicle/detail")
    ResponseEntity<TemVehicle> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message="id不能为空") Long id);

    @ApiOperation("列表")
    @PostMapping("/temVehicle/findList")
    ResponseEntity<List<TemVehicleDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchTemVehicle searchTemVehicle);
}
