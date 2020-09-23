package com.fantechs.controller.warehouse;

import com.fantechs.entity.WarehouseArea;
import com.fantechs.service.WarehouseAreaService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
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
 * Created by leifengzhi on 2020/09/23.
 */
@RestController
@Api(tags = "warehouseArea控制器")
@RequestMapping("/warehouseArea")
public class WarehouseAreaController {

    @Autowired
    private WarehouseAreaService warehouseAreaService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody WarehouseArea warehouseArea) {
        return ControllerUtil.returnCRUD(warehouseAreaService.save(warehouseArea));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(warehouseAreaService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody WarehouseArea warehouseArea) {
        if(StringUtils.isEmpty(warehouseArea.getWarehouseAreaId()
        )){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(warehouseAreaService.update(warehouseArea));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WarehouseArea> detail(@ApiParam(value = "ID",required = true)@RequestParam Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        WarehouseArea  warehouseArea = warehouseAreaService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(warehouseArea,StringUtils.isEmpty(warehouseArea)?0:1);
    }

    @ApiOperation("根据条件查询信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WarehouseArea>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWarehouseArea searchWarehouseArea) {
        Page<Object> page = PageHelper.startPage(searchWarehouseArea.getStartPage(),searchWarehouseArea.getPageSize());
        List<WarehouseArea> list = warehouseAreaService.findList(ControllerUtil.dynamicConditionByEntity(searchWarehouseArea));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
