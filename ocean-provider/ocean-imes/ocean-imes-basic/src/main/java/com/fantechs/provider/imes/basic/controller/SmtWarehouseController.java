package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.entity.basic.SmtWarehouse;
import com.fantechs.common.base.entity.basic.history.SmtHtWarehouse;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtWarehouseService;
import com.fantechs.provider.imes.basic.service.SmtWarehouseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * Created by wcz on 2020/09/23.
 */
@RestController
@Api(tags = "warehouse控制器")
@RequestMapping("/smtWarehouse")
public class SmtWarehouseController {

    @Autowired
    private SmtWarehouseService smtWarehouseService;

    @Autowired
    private SmtHtWarehouseService smtHtWarehouseService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody SmtWarehouse smtWarehouse) {
        return ControllerUtil.returnCRUD(smtWarehouseService.insert(smtWarehouse));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtWarehouseService.batchDel(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SmtWarehouse smtWarehouse) {
        if(StringUtils.isEmpty(smtWarehouse.getWarehouseId()
        )){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtWarehouseService.updateById(smtWarehouse));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWarehouse> detail(@ApiParam(value = "ID",required = true)@RequestParam Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtWarehouse  warehouse = smtWarehouseService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(warehouse,StringUtils.isEmpty(warehouse)?0:1);
    }

    @ApiOperation("根据条件查询信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWarehouse>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWarehouse searchSmtWarehouse) {
        Page<Object> page = PageHelper.startPage(searchSmtWarehouse.getStartPage(),searchSmtWarehouse.getPageSize());
        List<SmtWarehouse> list = smtWarehouseService.findList(searchSmtWarehouse);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("根据条件查询信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtWarehouse>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWarehouse searchSmtWarehouse) {
        Page<Object> page = PageHelper.startPage(searchSmtWarehouse.getStartPage(),searchSmtWarehouse.getPageSize());
        List<SmtHtWarehouse> list = smtHtWarehouseService.findHtList(searchSmtWarehouse);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
