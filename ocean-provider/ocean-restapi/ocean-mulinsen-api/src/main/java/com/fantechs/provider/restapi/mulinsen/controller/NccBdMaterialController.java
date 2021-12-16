package com.fantechs.provider.restapi.mulinsen.controller;

import com.fantechs.common.base.general.dto.mulinsen.NccBdMaterialDto;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchNccBdMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.restapi.mulinsen.service.NccBdMaterialService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "SCM物料控制器")
@RequestMapping("/nccBdMaterial")
@Validated
public class NccBdMaterialController {

    @Resource
    private NccBdMaterialService nccBdMaterialService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<NccBdMaterialDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchNccBdMaterial searchNccBdMaterial) {
        Page<Object> page = PageHelper.startPage(searchNccBdMaterial.getStartPage(), searchNccBdMaterial.getPageSize());
        List<NccBdMaterialDto> list = nccBdMaterialService.findList(searchNccBdMaterial);
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

}
