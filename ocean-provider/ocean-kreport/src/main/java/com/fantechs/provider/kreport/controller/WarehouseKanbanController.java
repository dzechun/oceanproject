package com.fantechs.provider.kreport.controller;

import com.fantechs.common.base.general.entity.kreport.JobDocumentProgress;
import com.fantechs.common.base.general.entity.kreport.PersonnelPickingRanking;
import com.fantechs.common.base.general.entity.kreport.Warehouse;
import com.fantechs.common.base.general.entity.kreport.WarehouseKanban;
import com.fantechs.common.base.general.entity.kreport.search.SearchWarehouseKanban;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.kreport.service.WarehouseKanbanService;
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

@RestController
@Api(tags = "仓库看板")
@RequestMapping("/warehouseKanban")
@Validated
public class WarehouseKanbanController {

    @Resource
    private WarehouseKanbanService warehouseKanbanService;

    @ApiOperation("列表")
    @PostMapping("/findKanbanData")
    public ResponseEntity<WarehouseKanban> findKanbanData(@ApiParam(value = "查询对象")@RequestBody SearchWarehouseKanban searchWarehouseKanban) {
        Page<Object> page = PageHelper.startPage(searchWarehouseKanban.getStartPage(),searchWarehouseKanban.getPageSize());
        WarehouseKanban warehouseKanban = warehouseKanbanService.findKanbanData(ControllerUtil.dynamicConditionByEntity(searchWarehouseKanban));
        return ControllerUtil.returnDataSuccess(warehouseKanban,1);
    }

    @ApiOperation("仓库列表")
    @PostMapping("/findWarehouse")
    public ResponseEntity<List<Warehouse>> findWarehouse(@ApiParam(value = "查询对象")@RequestBody SearchWarehouseKanban searchWarehouseKanban) {
        Page<Object> page = PageHelper.startPage(searchWarehouseKanban.getStartPage(),9999);
        List<Warehouse> warehouseList = warehouseKanbanService.findWarehouse();
        return ControllerUtil.returnDataSuccess(warehouseList,warehouseList.size());
    }

    @ApiOperation("人员拣货排名统计列表")
    @PostMapping("/findPersonnelPickingRankingList")
    public ResponseEntity<List<PersonnelPickingRanking>> findPersonnelPickingRankingList(@ApiParam(value = "查询对象")@RequestBody SearchWarehouseKanban searchWarehouseKanban) {
        Page<Object> page = PageHelper.startPage(searchWarehouseKanban.getStartPage(),searchWarehouseKanban.getPageSize());
        List<PersonnelPickingRanking> list = warehouseKanbanService.findPersonnelPickingRankingList(ControllerUtil.dynamicConditionByEntity(searchWarehouseKanban));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("作业中单据统计列表")
    @PostMapping("/findJobDocumentProgressList")
    public ResponseEntity<List<JobDocumentProgress>> findJobDocumentProgressList(@ApiParam(value = "查询对象")@RequestBody SearchWarehouseKanban searchWarehouseKanban) {
        Page<Object> page = PageHelper.startPage(searchWarehouseKanban.getStartPage(),searchWarehouseKanban.getPageSize());
        List<JobDocumentProgress>  list = warehouseKanbanService.findJobDocumentProgressList(ControllerUtil.dynamicConditionByEntity(searchWarehouseKanban));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


}
