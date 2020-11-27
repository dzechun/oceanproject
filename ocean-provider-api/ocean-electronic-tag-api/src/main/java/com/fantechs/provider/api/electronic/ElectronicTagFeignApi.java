package com.fantechs.provider.api.electronic;

import com.fantechs.common.base.electronic.entity.SmtElectronicTagController;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagController;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by lfz on 2020/11/24.
 */
@FeignClient(value = "ocean-electronic-tag")
public interface ElectronicTagFeignApi {

    @PostMapping(value="/smtElectronicTagController/findList",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取所有电子标签控制器",notes = "获取所有电子标签控制器")
    ResponseEntity<List<SmtElectronicTagController>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtElectronicTagController searchSmtElectronicTagController);

}
