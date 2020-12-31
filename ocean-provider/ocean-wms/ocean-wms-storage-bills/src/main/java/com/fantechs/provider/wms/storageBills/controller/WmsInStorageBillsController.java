package com.fantechs.provider.wms.storageBills.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.ExportBillsDTO;
import com.fantechs.common.base.dto.storage.WmsInStorageBillsDTO;
import com.fantechs.common.base.dto.storage.WmsInStorageBillsDetDTO;
import com.fantechs.common.base.entity.storage.WmsInStorageBills;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.provider.wms.storageBills.service.WmsInStorageBillsDetService;
import com.fantechs.provider.wms.storageBills.service.WmsInStorageBillsService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
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
public class WmsInStorageBillsController {

    @Resource
    private WmsInStorageBillsService wmsStorageBillsService;
    @Resource
    private WmsInStorageBillsDetService wmsInStorageBillsDetService;

    @ApiOperation("查询仓库清单表列表")
    @PostMapping("list")
    public ResponseEntity<List<WmsInStorageBillsDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchWmsStorageBillsListDTO searchWmsStorageBillsListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize
    ){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<WmsInStorageBillsDTO> wmsStorageBillsList = wmsStorageBillsService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchWmsStorageBillsListDTO));
        return ControllerUtil.returnDataSuccess(wmsStorageBillsList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询仓库清单表")
    @GetMapping("one")
    public ResponseEntity<WmsInStorageBills> one(@ApiParam(value = "仓库清单表对象ID",required = true)@RequestParam Long id){
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        WmsInStorageBills wmsStorageBills = wmsStorageBillsService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(wmsStorageBills, StringUtils.isEmpty(wmsStorageBills)?0:1);
    }

    @ApiOperation("增加仓库清单表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "仓库清单表对象",required = true)@RequestBody @Validated WmsInStorageBills wmsStorageBills){
        return ControllerUtil.returnCRUD(wmsStorageBillsService.save(wmsStorageBills));
    }

    @ApiOperation("删除仓库清单表数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "仓库清单表对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(wmsStorageBillsService.deleteByKey(id));
    }

    @ApiOperation("批量删除仓库清单表数据")
    @GetMapping("batchDelete")
    public ResponseEntity batchDelete(@ApiParam(value = "仓库清单表对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(wmsStorageBillsService.batchDelete(ids));
    }

    @ApiOperation("修改仓库清单表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "仓库清单表对象，对象ID必传",required = true)@RequestBody WmsInStorageBills wmsStorageBills){
        return ControllerUtil.returnCRUD(wmsStorageBillsService.update(wmsStorageBills));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchWmsStorageBillsListDTO searchWmsStorageBillsListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<WmsInStorageBillsDTO> wmsStorageBillsList = wmsStorageBillsService.selectFilterAll(null);
        if(StringUtils.isEmpty(wmsStorageBillsList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        List<ExportBillsDTO> exportBillsDTOList=new LinkedList<>();
        for (WmsInStorageBillsDTO wmsInStorageBillsDTO : wmsStorageBillsList) {
            ExportBillsDTO exportBillsDTO = new ExportBillsDTO();
            exportBillsDTO.setWmsInStorageBillsDTO(wmsInStorageBillsDTO);
            List<WmsInStorageBillsDetDTO> wmsInStorageBillsDetDTOList = wmsInStorageBillsDetService.selectFilterAll(wmsInStorageBillsDTO.getStorageBillsId());
            exportBillsDTO.setWmsInStorageBillsDetDTOList(wmsInStorageBillsDetDTOList);
            exportBillsDTOList.add(exportBillsDTO);
        }
        EasyPoiUtils.exportExcel(exportBillsDTOList,"仓库清单表信息","仓库清单表信息", ExportBillsDTO.class, "仓库清单表信息.xls", response);
    }
}
