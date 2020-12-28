package com.fantechs.provider.wms.storageBills.controller;

import com.fantechs.common.base.dto.storage.SaveBilssDet;
import com.fantechs.common.base.dto.storage.WmsStorageBillsDTO;
import com.fantechs.common.base.entity.storage.WmsStorageBills;
import com.fantechs.common.base.exception.SQLExecuteException;
import com.fantechs.provider.wms.storageBills.service.WmsStorageBillsService;
import com.fantechs.common.base.dto.storage.SearchWmsStorageBillsListDTO;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
/**
 * @Auther: bingo.ren
 * @Date: 2020年12月17日 14:52
 * @Description: 仓库清单表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "仓库清单表管理",basePath = "wmsStorageBills")
@RequestMapping("wmsStorageBills")
@Slf4j
public class WmsStorageBillsController {

    @Resource
    private WmsStorageBillsService wmsStorageBillsService;

    @ApiOperation("查询仓库清单表列表")
    @PostMapping("list")
    public ResponseEntity<List<WmsStorageBillsDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchWmsStorageBillsListDTO searchWmsStorageBillsListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize
    ){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<WmsStorageBillsDTO> wmsStorageBillsList = wmsStorageBillsService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchWmsStorageBillsListDTO));
        return ControllerUtil.returnDataSuccess(wmsStorageBillsList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询仓库清单表")
    @GetMapping("one")
    public ResponseEntity<WmsStorageBills> one(@ApiParam(value = "仓库清单表对象ID",required = true)@RequestParam Long id){
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        WmsStorageBills wmsStorageBills = wmsStorageBillsService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(wmsStorageBills, StringUtils.isEmpty(wmsStorageBills)?0:1);
    }

    @ApiOperation("增加仓库清单表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "仓库清单表对象",required = true)@RequestBody @Validated WmsStorageBills wmsStorageBills){
        return ControllerUtil.returnCRUD(wmsStorageBillsService.save(wmsStorageBills));
    }

    @ApiOperation("删除仓库清单表数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "仓库清单表对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(wmsStorageBillsService.deleteByKey(id));
    }

    @ApiOperation("修改仓库清单表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "仓库清单表对象，对象ID必传",required = true)@RequestBody WmsStorageBills wmsStorageBills){
        return ControllerUtil.returnCRUD(wmsStorageBillsService.update(wmsStorageBills));
    }
//    导出需要用到easyPOI
//    @PostMapping(value = "export",produces = "application/octet-stream")
//    @ApiOperation(value = "导出EXCEL")
//    public void export(HttpServletResponse response){
//        List<WmsStorageBills> wmsStorageBillss = wmsStorageBillsService.selectAll(null);
//        EasyPoiUtils.exportExcel(wmsStorageBillss,"仓库清单表信息","仓库清单表信息", WmsStorageBills.class, "仓库清单表信息.xls", response);
//    }
}
