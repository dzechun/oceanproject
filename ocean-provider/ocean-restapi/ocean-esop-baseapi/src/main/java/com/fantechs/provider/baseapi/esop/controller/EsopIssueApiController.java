package com.fantechs.provider.baseapi.esop.controller;

import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.baseapi.esop.service.EsopIssueApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */
@RestController
@Api(tags = "Esop同步问题清单")
@RequestMapping("/getIssue")
@Validated
public class EsopIssueApiController {

    @Resource
    private EsopIssueApiService esopIssueApiService;

    @ApiOperation("同步问题清单")
    @PostMapping("/getIssue")
    public ResponseEntity getIssue(@ApiParam(value = "产品(物料)编码",required = true) @RequestParam String materialCode) {
        int i = esopIssueApiService.getIssue(materialCode);
        return ControllerUtil.returnCRUD(i);
    }

    @ApiOperation("同步全部问题清单")
    @PostMapping("/getAllIssue")
    public ResponseEntity getAllWorkOrder(@ApiParam(value = "",required = true)@RequestBody SearchBaseMaterial searchBaseMaterial) {
        int i = esopIssueApiService.getAllIssue(searchBaseMaterial);
        return ControllerUtil.returnCRUD(i);
    }

}
