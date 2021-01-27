package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.dto.storage.WmsInStorageBillsDetDTO;
import com.fantechs.common.base.entity.storage.WmsInStorageBillsDet;
import com.fantechs.provider.wms.in.service.WmsInStorageBillsDetService;
import com.fantechs.common.base.dto.storage.SearchWmsStorageBillsDetListDTO;
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
 * @Date: 2020年12月17日 17:20
 * @Description: 仓库清单详情表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "仓库清单详情表管理",basePath = "wmsStorageBillsDet")
@RequestMapping("wmsStorageBillsDet")
@Slf4j
public class WmsInStorageBillsDetController {

    @Resource
    private WmsInStorageBillsDetService wmsStorageBillsDetService;

    @ApiOperation("查询仓库清单详情表列表")
    @PostMapping("findList")
    public ResponseEntity<List<WmsInStorageBillsDetDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) @Validated SearchWmsStorageBillsDetListDTO searchWmsStorageBillsDetListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize
    ){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<WmsInStorageBillsDetDTO> wmsStorageBillsDetDTOS = wmsStorageBillsDetService.selectFilterAll(searchWmsStorageBillsDetListDTO.getStorageBillsId());
        return ControllerUtil.returnDataSuccess(wmsStorageBillsDetDTOS,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询仓库清单详情表")
    @GetMapping("detail")
    public ResponseEntity<WmsInStorageBillsDet> one(@ApiParam(value = "仓库清单详情表对象ID",required = true)@RequestParam Long id){
        WmsInStorageBillsDet wmsStorageBillsDet = wmsStorageBillsDetService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(wmsStorageBillsDet, StringUtils.isEmpty(wmsStorageBillsDet)?0:1);
    }

    @ApiOperation("增加仓库清单详情表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "仓库清单详情表对象",required = true)@RequestBody WmsInStorageBillsDet wmsStorageBillsDet){
        return ControllerUtil.returnCRUD(wmsStorageBillsDetService.save(wmsStorageBillsDet));
    }

    @ApiOperation("批量删除仓库清单详情表数据")
    @GetMapping("delete")
    public ResponseEntity batchDelete(@ApiParam(value = "仓库清单详情表对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(wmsStorageBillsDetService.batchDelete(ids));
    }

    @ApiOperation("修改仓库清单详情表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "仓库清单详情表对象，对象ID必传",required = true)@RequestBody WmsInStorageBillsDet wmsStorageBillsDet){
        return ControllerUtil.returnCRUD(wmsStorageBillsDetService.update(wmsStorageBillsDet));
    }

//    导出需要用到easyPOI
//    @PostMapping(value = "export",produces = "application/octet-stream")
//    @ApiOperation(value = "导出EXCEL")
//    public void export(HttpServletResponse response){
//        List<WmsStorageBillsDet> wmsStorageBillsDets = wmsStorageBillsDetService.selectAll(null);
//        EasyPoiUtils.exportExcel(wmsStorageBillsDets,"仓库清单详情表信息","仓库清单详情表信息", WmsStorageBillsDet.class, "仓库清单详情表信息.xls", response);
//    }
}
