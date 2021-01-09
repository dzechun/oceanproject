package com.fantechs.provider.wms.storageBills.controller.history;

import com.fantechs.common.base.dto.basic.history.WmsInHtStorageBillsDetDTO;
import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBillsDet;
import com.fantechs.provider.wms.storageBills.service.history.WmsInHtStorageBillsDetService;
import com.fantechs.common.base.dto.apply.history.SearchWmsInHtStorageBillsDetListDTO;
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
 * @Date: 2020年12月25日 14:01
 * @Description: 履历仓库清单详情表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "履历仓库清单详情表管理",basePath = "wmsHtStorageBillsDet")
@RequestMapping("wmsHtStorageBillsDet")
@Slf4j
public class WmsInHtStorageBillsDetController {

    @Resource
    private WmsInHtStorageBillsDetService wmsHtStorageBillsDetService;

    @ApiOperation("查询履历仓库清单详情表列表")
    @PostMapping("list")
    public ResponseEntity<List<WmsInHtStorageBillsDetDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchWmsInHtStorageBillsDetListDTO searchWmsHtStorageBillsDetListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize
    ){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<WmsInHtStorageBillsDetDTO> wmsInHtStorageBillsDetDTOList = wmsHtStorageBillsDetService.selectFilterAll(searchWmsHtStorageBillsDetListDTO.getStorageBillsId());
        return ControllerUtil.returnDataSuccess(wmsInHtStorageBillsDetDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询履历仓库清单详情表")
    @GetMapping("one")
    public ResponseEntity<WmsInHtStorageBillsDet> one(@ApiParam(value = "履历仓库清单详情表对象ID",required = true)@RequestParam Long id){
        WmsInHtStorageBillsDet wmsHtStorageBillsDet = wmsHtStorageBillsDetService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(wmsHtStorageBillsDet, StringUtils.isEmpty(wmsHtStorageBillsDet)?0:1);
    }

    @ApiOperation("增加履历仓库清单详情表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "履历仓库清单详情表对象",required = true)@RequestBody WmsInHtStorageBillsDet wmsHtStorageBillsDet){
        return ControllerUtil.returnCRUD(wmsHtStorageBillsDetService.save(wmsHtStorageBillsDet));
    }

    @ApiOperation("删除履历仓库清单详情表数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "履历仓库清单详情表对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(wmsHtStorageBillsDetService.deleteByKey(id));
    }

    @ApiOperation("修改履历仓库清单详情表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "履历仓库清单详情表对象，对象ID必传",required = true)@RequestBody WmsInHtStorageBillsDet wmsHtStorageBillsDet){
        return ControllerUtil.returnCRUD(wmsHtStorageBillsDetService.update(wmsHtStorageBillsDet));
    }

//    导出需要用到easyPOI
//    @PostMapping(value = "export",produces = "application/octet-stream")
//    @ApiOperation(value = "导出EXCEL")
//    public void export(HttpServletResponse response){
//        List<WmsHtStorageBillsDet> wmsHtStorageBillsDets = wmsHtStorageBillsDetService.selectAll(null);
//        EasyPoiUtils.exportExcel(wmsHtStorageBillsDets,"履历仓库清单详情表信息","履历仓库清单详情表信息", WmsHtStorageBillsDet.class, "履历仓库清单详情表信息.xls", response);
//    }
}
