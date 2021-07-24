package com.fantechs.provider.baseapi.esop.controller;

import com.fantechs.common.base.general.dto.restapi.SearchSapMaterialApi;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.baseapi.esop.service.SapMaterialApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/05/31.
 */
@RestController
@Api(tags = "物料接口")
@RequestMapping("/materialApi")
@Validated
public class SapMaterialApiController {

    @Resource
    private SapMaterialApiService sapMaterialApiService;

    @ApiOperation(value = "请求雷赛物料信息",notes = "新增或更新")
    @PostMapping("/getMaterial")
    public ResponseEntity getSapMaterial(@ApiParam(value = "查询对象")@RequestBody SearchSapMaterialApi searchSapMaterialApi) {
        return ControllerUtil.returnCRUD(sapMaterialApiService.getMaterial(searchSapMaterialApi));
    }
}
