package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.entity.basic.SmtWarehouseArea;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouseArea;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtWarehouseAreaService;
import com.fantechs.provider.imes.basic.service.SmtWarehouseAreaService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/09/23.
 */
@RestController
@Api(tags = "smtWarehouseArea控制器")
@RequestMapping("/smtWarehouseArea")
public class SmtWarehouseAreaController {

    @Autowired
    private SmtWarehouseAreaService smtWarehouseAreaService;
    @Autowired
    private SmtHtWarehouseAreaService smtHtWarehouseAreaService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody SmtWarehouseArea smtWarehouseArea) {
        return ControllerUtil.returnCRUD(smtWarehouseAreaService.save(smtWarehouseArea));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtWarehouseAreaService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SmtWarehouseArea smtWarehouseArea) {
        if(StringUtils.isEmpty(smtWarehouseArea.getWarehouseAreaId()
        )){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtWarehouseAreaService.update(smtWarehouseArea));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWarehouseArea> detail(@ApiParam(value = "工厂ID",required = true)@RequestParam Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtWarehouseArea  smtWarehouseArea = smtWarehouseAreaService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWarehouseArea,StringUtils.isEmpty(smtWarehouseArea)?0:1);
    }

    @ApiOperation("根据条件查询角色信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWarehouseArea>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWarehouseArea searchSmtWarehouseArea) {
        Page<Object> page = PageHelper.startPage(searchSmtWarehouseArea.getStartPage(),searchSmtWarehouseArea.getPageSize());
        List<SmtWarehouseArea> list = smtWarehouseAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtWarehouseArea));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
