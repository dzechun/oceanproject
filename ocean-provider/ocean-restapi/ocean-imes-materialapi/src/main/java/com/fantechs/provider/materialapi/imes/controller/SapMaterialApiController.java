package com.fantechs.provider.materialapi.imes.controller;

import com.fantechs.common.base.general.dto.restapi.SearchSapMaterialApi;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.materialapi.imes.service.GetTestService;
import com.fantechs.provider.materialapi.imes.service.SapMaterialApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 *
 * Created by leifengzhi on 2021/05/31.
 */
@RestController
@Api(tags = "请求物料接口")
@RequestMapping("/sapMaterialApi")
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
