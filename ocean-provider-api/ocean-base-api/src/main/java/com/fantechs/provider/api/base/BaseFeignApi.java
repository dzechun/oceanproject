package com.fantechs.provider.api.base;

import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTab;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ocean-base")
public interface BaseFeignApi {

    @ApiOperation("页签信息列表")
    @PostMapping("/baseTab/findList")
    ResponseEntity<List<BaseTab>> findTabList(@ApiParam(value = "查询对象")@RequestBody SearchBaseTab searchBaseTab);

    @ApiOperation(value = "新增页签",notes = "新增页签")
    @PostMapping("/baseTab/add")
    ResponseEntity addTab(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseTab baseTab);

    @ApiOperation("删除页签")
    @PostMapping("/baseTab/delete")
    ResponseEntity deleteTab(@ApiParam(value = "页签集合")@RequestBody @Validated List<BaseTab> baseTabs);

    @ApiOperation("修改页签")
    @PostMapping("/baseTab/update")
    ResponseEntity updateTab(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseTab.update.class) BaseTab baseTab);
}
