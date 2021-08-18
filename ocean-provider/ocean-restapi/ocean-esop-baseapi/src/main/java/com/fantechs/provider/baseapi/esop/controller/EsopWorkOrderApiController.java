package com.fantechs.provider.baseapi.esop.controller;

import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.restapi.esop.search.SearchEsop;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.baseapi.esop.service.EsopWorkOrderApiService;
import com.fantechs.provider.baseapi.esop.service.EsopWorkshopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */
@RestController
@Api(tags = "Esop同步工单信息")
@RequestMapping("/getWorkOrder")
@Validated
public class EsopWorkOrderApiController {

    @Resource
    private EsopWorkOrderApiService esopWorkOrderApiService;

    @ApiOperation("同步工单信息")
    @PostMapping("/getWorkOrder")
    public ResponseEntity getWorkOrder(@ApiParam(value = "产线id",required = true) @RequestParam String prolineId) {
        int i = esopWorkOrderApiService.getWorkOrder(prolineId);
        return ControllerUtil.returnCRUD(i);
    }

}
