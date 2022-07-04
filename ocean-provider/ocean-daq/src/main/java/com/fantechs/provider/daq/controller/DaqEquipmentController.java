package com.fantechs.provider.daq.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.daq.DaqEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipment;
import com.fantechs.common.base.general.entity.daq.DaqHtEquipment;
import com.fantechs.common.base.general.entity.daq.search.SearchDaqEquipment;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.service.DaqEquipmentService;
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
 * Created by leifengzhi on 2021/06/25.
 */
@RestController
@Api(tags = "设备信息")
@RequestMapping("/daqEquipment")
@Validated
public class DaqEquipmentController {

    @Resource
    private DaqEquipmentService daqEquipmentService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody @Validated DaqEquipment daqEquipment) {
        return ControllerUtil.returnCRUD(daqEquipmentService.save(daqEquipment));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(daqEquipmentService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = DaqEquipment.update.class) DaqEquipment daqEquipment) {
        return ControllerUtil.returnCRUD(daqEquipmentService.update(daqEquipment));
    }

    @ApiOperation("批量修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "对象，Id必传", required = true) @RequestBody List<DaqEquipment> list) {
        return ControllerUtil.returnCRUD(daqEquipmentService.batchUpdate(list));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<DaqEquipment> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        DaqEquipment daqEquipment = daqEquipmentService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(daqEquipment, StringUtils.isEmpty(daqEquipment) ? 0 : 1);
    }

    @ApiOperation("通过IP获取详情")
    @GetMapping("/detailByIp")
    public ResponseEntity<DaqEquipment> detailByIp(@ApiParam(value = "ip", required = true) @RequestParam @NotNull(message = "id不能为空") String ip) {
        DaqEquipment daqEquipment = daqEquipmentService.detailByIp(ip);
        return ControllerUtil.returnDataSuccess(daqEquipment, StringUtils.isEmpty(daqEquipment) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<DaqEquipmentDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchDaqEquipment searchDaqEquipment) {
        Page<Object> page = PageHelper.startPage(searchDaqEquipment.getStartPage(), searchDaqEquipment.getPageSize());
        List<DaqEquipmentDto> list = daqEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipment));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<DaqHtEquipment>> findHtList(@ApiParam(value = "查询对象") @RequestBody SearchDaqEquipment searchDaqEquipment) {
        Page<Object> page = PageHelper.startPage(searchDaqEquipment.getStartPage(), searchDaqEquipment.getPageSize());
        List<DaqHtEquipment> list = daqEquipmentService.findHtList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipment));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchDaqEquipment searchDaqEquipment) {
        List<DaqEquipmentDto> list = daqEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipment));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "设备信息", EamEquipmentDto.class, "设备信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("Mac地址查询")
    @PostMapping("/findByMac")
    public ResponseEntity<List<DaqEquipmentDto>> findByMac(@RequestParam(value = "mac") Object mac, @RequestParam(value = "orgId") Long orgId) {
        SearchDaqEquipment searchDaqEquipment = new SearchDaqEquipment();
        searchDaqEquipment.setOrgId(orgId);
        searchDaqEquipment.setEquipmentMacAddress(mac.toString());
        List<DaqEquipmentDto> list = daqEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchDaqEquipment));
        return ControllerUtil.returnDataSuccess(list, 1);
    }

    @ApiOperation("未绑定分组查询")
    @PostMapping("/findNoGroup")
    public ResponseEntity<List<DaqEquipmentDto>> findNoGroup(@ApiParam(value = "查询对象") @RequestBody SearchDaqEquipment searchDaqEquipment) {
        Page<Object> page = PageHelper.startPage(searchDaqEquipment.getStartPage(), searchDaqEquipment.getPageSize());
        List<DaqEquipmentDto> list = daqEquipmentService.findNoGroup(ControllerUtil.dynamicConditionByEntity(searchDaqEquipment));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }
}
