package com.fantechs.provider.baseapi.esop.controller;

import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.restapi.esop.search.SearchEsop;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.baseapi.esop.service.EsopMaterialService;
import com.fantechs.provider.baseapi.esop.service.EsopWorkshopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */
@RestController
@Api(tags = "Esop同步物料信息")
@RequestMapping("/esopMaterial")
@Validated
public class EsopMaterialController {

    @Resource
    private EsopMaterialService esopMaterialService;

    @ApiOperation("同步物料信息")
    @PostMapping("/getMaterial")
    public ResponseEntity<BaseMaterial> getMaterial(@ApiParam(value = "物料编码",required = true) @RequestParam String materialCode) {
        BaseMaterial material = esopMaterialService.getMaterial(materialCode);
        return  ControllerUtil.returnDataSuccess(material, StringUtils.isEmpty(material)?0:1);
    }

}
