package com.fantechs.provider.daq.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.daq.DaqEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentDataGroupReDcDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentDataGroupReDc;
import com.fantechs.common.base.general.entity.daq.search.SearchDaqEquipmentDataGroupReDc;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.service.DaqEquipmentDataGroupReDcService;
import com.fantechs.provider.daq.service.DaqHtEquipmentDataGroupReDcService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/08/02.
 */
@RestController
@Api(tags = "设备分组")
@RequestMapping("/daqEquipmentDataGroupReDc")
@Validated
public class DaqEquipmentDataGroupReDcController {

    @Resource
    private DaqEquipmentDataGroupReDcService daqEquipmentDataGroupReDcService;
    @Resource
    private DaqHtEquipmentDataGroupReDcService daqHtEquipmentDataGroupReDcService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated DaqEquipmentDataGroupReDc daqEquipmentDataGroupReDc) {
        return ControllerUtil.returnCRUD(daqEquipmentDataGroupReDcService.save(daqEquipmentDataGroupReDc));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(daqEquipmentDataGroupReDcService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=DaqEquipmentDataGroupReDc.update.class) DaqEquipmentDataGroupReDc daqEquipmentDataGroupReDc) {
        return ControllerUtil.returnCRUD(daqEquipmentDataGroupReDcService.update(daqEquipmentDataGroupReDc));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<DaqEquipmentDataGroupReDc> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        DaqEquipmentDataGroupReDc  daqEquipmentDataGroupReDc = daqEquipmentDataGroupReDcService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(daqEquipmentDataGroupReDc,StringUtils.isEmpty(daqEquipmentDataGroupReDc)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<DaqEquipmentDataGroupReDcDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchDaqEquipmentDataGroupReDc searchDaqEquipmentDataGroupReDc) {
        Page<Object> page = PageHelper.startPage(searchDaqEquipmentDataGroupReDc.getStartPage(),searchDaqEquipmentDataGroupReDc.getPageSize());
        List<DaqEquipmentDataGroupReDcDto> list = daqEquipmentDataGroupReDcService.findList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipmentDataGroupReDc));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<DaqHtEquipmentDataGroupReDcDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchDaqEquipmentDataGroupReDc searchDaqEquipmentDataGroupReDc) {
        Page<Object> page = PageHelper.startPage(searchDaqEquipmentDataGroupReDc.getStartPage(),searchDaqEquipmentDataGroupReDc.getPageSize());
        List<DaqHtEquipmentDataGroupReDcDto> list = daqHtEquipmentDataGroupReDcService.findHtList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipmentDataGroupReDc));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchDaqEquipmentDataGroupReDc searchDaqEquipmentDataGroupReDc){
    List<DaqEquipmentDataGroupReDcDto> list = daqEquipmentDataGroupReDcService.findList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipmentDataGroupReDc));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "DaqEquipmentDataGroupReDc信息", DaqEquipmentDataGroupReDcDto.class, "DaqEquipmentDataGroupReDc.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("批量添加")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "对象，equipmentDataGroupId必传",required = true)@RequestBody @Validated List<DaqEquipmentDataGroupReDc> daqEquipmentDataGroupReDcs){
        return  ControllerUtil.returnCRUD(daqEquipmentDataGroupReDcService.batchAdd(daqEquipmentDataGroupReDcs));
    }
}
