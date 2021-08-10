package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipment;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentService;
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
 * Created by leifengzhi on 2021/06/25.
 */
@RestController
@Api(tags = "设备信息")
@RequestMapping("/eamEquipment")
@Validated
public class EamEquipmentController {

    @Resource
    private EamEquipmentService eamEquipmentService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipment eamEquipment) {
        return ControllerUtil.returnCRUD(eamEquipmentService.save(eamEquipment));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipment.update.class) EamEquipment eamEquipment) {
        return ControllerUtil.returnCRUD(eamEquipmentService.update(eamEquipment));
    }

    @ApiOperation("批量修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "对象，Id必传",required = true)@RequestBody List<EamEquipment> list) {
        return ControllerUtil.returnCRUD(eamEquipmentService.batchUpdate(list));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipment> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipment  eamEquipment = eamEquipmentService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipment,StringUtils.isEmpty(eamEquipment)?0:1);
    }

    @ApiOperation("通过IP获取详情")
    @GetMapping("/detailByIp")
    public ResponseEntity<EamEquipment> detailByIp(@ApiParam(value = "ip",required = true) @RequestParam  @NotNull(message="id不能为空") String ip) {
        EamEquipment  eamEquipment = eamEquipmentService.detailByIp(ip);
        return  ControllerUtil.returnDataSuccess(eamEquipment,StringUtils.isEmpty(eamEquipment)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipment searchEamEquipment) {
        Page<Object> page = PageHelper.startPage(searchEamEquipment.getStartPage(),searchEamEquipment.getPageSize());
        List<EamEquipmentDto> list = eamEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipment));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipment>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipment searchEamEquipment) {
        Page<Object> page = PageHelper.startPage(searchEamEquipment.getStartPage(),searchEamEquipment.getPageSize());
        List<EamHtEquipment> list = eamEquipmentService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipment));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipment searchEamEquipment){
    List<EamEquipmentDto> list = eamEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipment));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备信息", EamEquipmentDto.class, "设备信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("Mac地址查询")
    @PostMapping("/findByMac")
    public ResponseEntity<List<EamEquipmentDto>> findByMac(@RequestParam(value = "mac") Object mac, @RequestParam(value = "orgId") Long orgId) {
        SearchEamEquipment searchEamEquipment = new SearchEamEquipment();
        searchEamEquipment.setOrgId(orgId);
        searchEamEquipment.setEquipmentMacAddress(mac.toString());
        List<EamEquipmentDto> list = eamEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipment));
        return ControllerUtil.returnDataSuccess(list,1);
    }

    @ApiOperation("未绑定分组查询")
    @PostMapping("/findNoGroup")
    public ResponseEntity<List<EamEquipmentDto>> findNoGroup(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipment searchEamEquipment) {
        Page<Object> page = PageHelper.startPage(searchEamEquipment.getStartPage(),searchEamEquipment.getPageSize());
        List<EamEquipmentDto> list = eamEquipmentService.findNoGroup(ControllerUtil.dynamicConditionByEntity(searchEamEquipment));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
