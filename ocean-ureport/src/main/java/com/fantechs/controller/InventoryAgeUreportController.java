package com.fantechs.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.entity.InventoryAge;
import com.fantechs.entity.search.SearchInventoryAgeUreport;
import com.fantechs.service.InventoryAgeUreportService;
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
 * Created by lzw on 2021/09/26.
 */
@RestController
@Api(tags = "库龄报表")
@RequestMapping("/InventoryAgeUreport")
@Validated
public class InventoryAgeUreportController {

    @Resource
    private InventoryAgeUreportService inventoryAgeUreportService;

    @ApiOperation("库龄报表查询")
    @PostMapping("/findList")
    public ResponseEntity<List<InventoryAge>> list(
            @ApiParam(value = "查询条件，请参考Model说明") @RequestBody(required = false) SearchInventoryAgeUreport searchInventoryAgeUreport) {
        Page<Object> page = PageHelper.startPage(searchInventoryAgeUreport.getStartPage(), searchInventoryAgeUreport.getPageSize());
        List<InventoryAge> list = inventoryAgeUreportService.findList(ControllerUtil.dynamicConditionByEntity(searchInventoryAgeUreport));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }
}
