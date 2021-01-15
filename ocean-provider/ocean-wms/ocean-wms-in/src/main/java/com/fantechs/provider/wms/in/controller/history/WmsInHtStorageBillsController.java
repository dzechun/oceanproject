package com.fantechs.provider.wms.in.controller.history;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.history.ExportHtBillsDTO;
import com.fantechs.common.base.dto.basic.history.WmsInHtStorageBillsDTO;
import com.fantechs.common.base.dto.basic.history.WmsInHtStorageBillsDetDTO;
import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBills;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.provider.wms.in.service.history.WmsInHtStorageBillsDetService;
import com.fantechs.provider.wms.in.service.history.WmsInHtStorageBillsService;
import com.fantechs.common.base.general.dto.mes.pm.history.SearchWmsInHtStorageBillsListDTO;
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
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
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
    @Resource
    private WmsInHtStorageBillsDetService wmsInHtStorageBillsDetService;

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

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchWmsInHtStorageBillsListDTO searchWmsHtStorageBillsListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<WmsInHtStorageBillsDTO> wmsHtStorageBillsDTOList = wmsHtStorageBillsService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchWmsHtStorageBillsListDTO));
        if(StringUtils.isEmpty(wmsHtStorageBillsDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        List<ExportHtBillsDTO> exportBillsDTOList=new LinkedList<>();
        for (WmsInHtStorageBillsDTO wmsInHtStorageBillsDTO : wmsHtStorageBillsDTOList) {
            ExportHtBillsDTO exportHtBillsDTO=new ExportHtBillsDTO();
            exportHtBillsDTO.setWmsInHtStorageBillsDTO(wmsInHtStorageBillsDTO);
            List<WmsInHtStorageBillsDetDTO> wmsInHtStorageBillsDetDTOList = wmsInHtStorageBillsDetService.selectFilterAll(wmsInHtStorageBillsDTO.getStorageBillsId());
            exportHtBillsDTO.setWmsInHtStorageBillsDetDTOList(wmsInHtStorageBillsDetDTOList);
            exportBillsDTOList.add(exportHtBillsDTO);
        }
        EasyPoiUtils.exportExcel(exportBillsDTOList,"仓库清单履历表信息","仓库清单履历表信息", ExportHtBillsDTO.class, "仓库清单履历表信息.xls", response);
    }
}
