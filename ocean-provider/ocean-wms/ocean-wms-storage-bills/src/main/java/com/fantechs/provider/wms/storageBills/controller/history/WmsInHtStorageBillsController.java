package com.fantechs.provider.wms.storageBills.controller.history;

import com.fantechs.common.base.dto.apply.history.WmsInHtStorageBillsDTO;
import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBills;
import com.fantechs.provider.wms.storageBills.service.history.WmsInHtStorageBillsService;
import com.fantechs.common.base.dto.apply.history.SearchWmsInHtStorageBillsListDTO;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
/**
 * @Auther: bingo.ren
 * @Date: 2020年12月25日 9:35
 * @Description: 履历仓库清单表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "履历仓库清单表管理",basePath = "wmsHtStorageBills")
@RequestMapping("wmsHtStorageBills")
@Slf4j
public class WmsInHtStorageBillsController {

    @Resource
    private WmsInHtStorageBillsService wmsHtStorageBillsService;

    @ApiOperation("查询履历仓库清单表列表")
    @PostMapping("list")
    public ResponseEntity<List<WmsInHtStorageBillsDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchWmsInHtStorageBillsListDTO searchWmsHtStorageBillsListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize
    ){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<WmsInHtStorageBillsDTO> wmsHtStorageBillsDTOList = wmsHtStorageBillsService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchWmsHtStorageBillsListDTO));
        return ControllerUtil.returnDataSuccess(wmsHtStorageBillsDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询履历仓库清单表")
    @GetMapping("one")
    public ResponseEntity<WmsInHtStorageBills> one(@ApiParam(value = "履历仓库清单表对象ID",required = true)@RequestParam Long id){
        WmsInHtStorageBills wmsHtStorageBills = wmsHtStorageBillsService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(wmsHtStorageBills, StringUtils.isEmpty(wmsHtStorageBills)?0:1);
    }

    @ApiOperation("增加履历仓库清单表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "履历仓库清单表对象",required = true)@RequestBody WmsInHtStorageBills wmsHtStorageBills){
        return ControllerUtil.returnCRUD(wmsHtStorageBillsService.save(wmsHtStorageBills));
    }

    @ApiOperation("删除履历仓库清单表数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "履历仓库清单表对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(wmsHtStorageBillsService.deleteByKey(id));
    }

    @ApiOperation("修改履历仓库清单表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "履历仓库清单表对象，对象ID必传",required = true)@RequestBody WmsInHtStorageBills wmsHtStorageBills){
        return ControllerUtil.returnCRUD(wmsHtStorageBillsService.update(wmsHtStorageBills));
    }

//    导出需要用到easyPOI
//    @PostMapping(value = "export",produces = "application/octet-stream")
//    @ApiOperation(value = "导出EXCEL")
//    public void export(HttpServletResponse response){
//        List<WmsHtStorageBills> wmsHtStorageBillss = wmsHtStorageBillsService.selectAll(null);
//        EasyPoiUtils.exportExcel(wmsHtStorageBillss,"履历仓库清单表信息","履历仓库清单表信息", WmsHtStorageBills.class, "履历仓库清单表信息.xls", response);
//    }
}
