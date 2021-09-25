package com.fantechs.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.entity.OmInStorage;
import com.fantechs.entity.ProductionInStorage;
import com.fantechs.entity.ProductionInStorageDet;
import com.fantechs.entity.search.SearchOmInStorage;
import com.fantechs.entity.search.SearchProductionInStorage;
import com.fantechs.service.ProductionInStorageService;
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

/**
 * @Author mr.lei
 * @Date 2021/9/24
 */
@RestController
@Api(tags = "生产入库")
@RequestMapping("/productionInStorage")
@Validated
public class ProductionInStorageController {
    @Resource
    private ProductionInStorageService productionInStorageService;

    @ApiOperation("生产汇总")
    @PostMapping("/findProList")
    public ResponseEntity<List<ProductionInStorage>> findProList(@ApiParam(value = "查询对象")@RequestBody SearchProductionInStorage searchProductionInStorage) {
        Page<Object> page = PageHelper.startPage(searchProductionInStorage.getStartPage(),searchProductionInStorage.getPageSize());
        List<ProductionInStorage> list = productionInStorageService.findProList(ControllerUtil.dynamicConditionByEntity(searchProductionInStorage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("生产库存明细")
    @PostMapping("/findProDetList")
    public ResponseEntity<List<ProductionInStorageDet>> findProDetList(@ApiParam(value = "查询对象")@RequestBody SearchProductionInStorage searchProductionInStorage) {
        Page<Object> page = PageHelper.startPage(searchProductionInStorage.getStartPage(),searchProductionInStorage.getPageSize());
        List<ProductionInStorageDet> list = productionInStorageService.findProDetList(ControllerUtil.dynamicConditionByEntity(searchProductionInStorage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("销售库存明细")
    @PostMapping("/findOmList")
    public ResponseEntity<List<OmInStorage>> findOmList(@ApiParam(value = "查询对象")@RequestBody SearchOmInStorage searchOmInStorage) {
        Page<Object> page = PageHelper.startPage(searchOmInStorage.getStartPage(),searchOmInStorage.getPageSize());
        List<OmInStorage> list = productionInStorageService.findOmList(ControllerUtil.dynamicConditionByEntity(searchOmInStorage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
