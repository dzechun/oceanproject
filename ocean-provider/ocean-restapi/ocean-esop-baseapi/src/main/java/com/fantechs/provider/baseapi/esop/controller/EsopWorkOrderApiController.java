package com.fantechs.provider.baseapi.esop.controller;

import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.baseapi.esop.service.EsopWorkOrderApiService;
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
@Api(tags = "Esop同步工单信息")
@RequestMapping("/getWorkOrder")
@Validated
public class EsopWorkOrderApiController {

    @Resource
    private EsopWorkOrderApiService esopWorkOrderApiService;

    @ApiOperation("同步单个工单信息")
    @PostMapping("/getWorkOrder")
    public ResponseEntity getWorkOrder(@ApiParam(value = "产线编码",required = true) @RequestParam String proCode) {
        MesPmWorkOrder workOrder = esopWorkOrderApiService.getWorkOrder(proCode);
        return ControllerUtil.returnDataSuccess(workOrder, StringUtils.isEmpty(workOrder)?0:1);
    }

    @ApiOperation("同步全部工单信息")
    @PostMapping("/getAllWorkOrder")
    public ResponseEntity getAllWorkOrder() {
        int i = esopWorkOrderApiService.getAllWorkOrder();
        return ControllerUtil.returnCRUD(i);
    }

}
