package com.fantechs.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.entity.ProductDailyPlanModel;
import com.fantechs.entity.search.ProductionBatch;
import com.fantechs.entity.search.SearchProductDailyPlan;
import com.fantechs.service.ProductDailyPlanService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author
 * @Date 2021/12/01
 */
@RestController
@Api(tags = "雷赛-生产计划看板")
@RequestMapping("/productDailyPlan")
@Validated
public class ProductDailyPlanController {
    @Resource
    private ProductDailyPlanService productDailyPlanService;

    @PostMapping("/findList")
    @ApiOperation("今日明日生产计划")
    public ResponseEntity<List<ProductDailyPlanModel>> findList(@RequestBody SearchProductDailyPlan searchProductDailyPlan){
        Page<Object> page = PageHelper.startPage(searchProductDailyPlan.getStartPage(), searchProductDailyPlan.getPageSize());
        return ControllerUtil.returnDataSuccess(productDailyPlanService.findList(ControllerUtil.dynamicConditionByEntity(searchProductDailyPlan)),(int)page.getTotal());
    }

    @PostMapping("/findBatchList")
    @ApiOperation("查询批次达成率")
    public ResponseEntity<List<ProductionBatch>> findBatchList(){
        return ControllerUtil.returnDataSuccess(productDailyPlanService.findBatchList(),0);
    }



}
