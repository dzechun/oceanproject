package com.fantechs.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.entity.ProductionLine;
import com.fantechs.entity.search.SearchProductDailyPlan;
import com.fantechs.service.ProductionLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "雷赛-产线智能看板")
@RequestMapping("/productionLine")
@Validated
public class ProductionLineController {

    @Resource
    private ProductionLineService productionLineService;


    @ApiOperation("产线智能看板数据")
    @PostMapping("/findList")
    public ResponseEntity<ProductionLine> findList(@ApiParam(value = "查询对象")@RequestBody SearchProductDailyPlan searchProductDailyPlan) {
        ProductionLine model = productionLineService.findList(searchProductDailyPlan);
        return ControllerUtil.returnDataSuccess(model,1);
    }



}
