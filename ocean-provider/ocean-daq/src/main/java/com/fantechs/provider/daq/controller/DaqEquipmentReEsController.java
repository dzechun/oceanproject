package com.fantechs.provider.daq.controller;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentReEsDto;
import com.fantechs.common.base.general.dto.daq.DaqHtEquipmentReEsDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipmentReEs;
import com.fantechs.common.base.general.entity.daq.search.SearchDaqEquipmentReEs;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.service.DaqEquipmentReEsService;
import com.fantechs.provider.daq.service.DaqHtEquipmentReEsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/08/09.
 */
@RestController
@Api(tags = "设备工作站关联信息")
@RequestMapping("/daqEquipmentReEs")
@Validated
public class DaqEquipmentReEsController {

    @Resource
    private DaqEquipmentReEsService daqEquipmentReEsService;
    @Resource
    private DaqHtEquipmentReEsService daqHtEquipmentReEsService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated DaqEquipmentReEs daqEquipmentReEs) {
        return ControllerUtil.returnCRUD(daqEquipmentReEsService.save(daqEquipmentReEs));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(daqEquipmentReEsService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=DaqEquipmentReEs.update.class) DaqEquipmentReEs daqEquipmentReEs) {
        return ControllerUtil.returnCRUD(daqEquipmentReEsService.update(daqEquipmentReEs));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<DaqEquipmentReEs> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        DaqEquipmentReEs  daqEquipmentReEs = daqEquipmentReEsService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(daqEquipmentReEs,StringUtils.isEmpty(daqEquipmentReEs)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<DaqEquipmentReEsDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchDaqEquipmentReEs searchDaqEquipmentReEs) {
        Page<Object> page = PageHelper.startPage(searchDaqEquipmentReEs.getStartPage(),searchDaqEquipmentReEs.getPageSize());
        List<DaqEquipmentReEsDto> list = daqEquipmentReEsService.findList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipmentReEs));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<DaqHtEquipmentReEsDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchDaqEquipmentReEs searchDaqEquipmentReEs) {
        Page<Object> page = PageHelper.startPage(searchDaqEquipmentReEs.getStartPage(),searchDaqEquipmentReEs.getPageSize());
        List<DaqHtEquipmentReEsDto> list = daqHtEquipmentReEsService.findHtList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipmentReEs));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
