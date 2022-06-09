package com.fantechs.provider.esop.controller;

import com.fantechs.common.base.general.dto.esop.EsopEquipmentDto;
import com.fantechs.common.base.general.entity.esop.EsopEquipment;
import com.fantechs.common.base.general.entity.esop.history.EsopHtEquipment;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopEquipment;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.esop.service.EsopEquipmentService;
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
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */
@RestController
@Api(tags = "设备信息")
@RequestMapping("/esopEquipment")
@Validated
public class EsopEquipmentController {

    @Resource
    private EsopEquipmentService esopEquipmentService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EsopEquipment esopEquipment) {
        return ControllerUtil.returnCRUD(esopEquipmentService.save(esopEquipment));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(esopEquipmentService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EsopEquipment.update.class) EsopEquipment esopEquipment) {
        return ControllerUtil.returnCRUD(esopEquipmentService.update(esopEquipment));
    }

    @ApiOperation("批量修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "对象，Id必传",required = true)@RequestBody List<EsopEquipment> list) {
        return ControllerUtil.returnCRUD(esopEquipmentService.batchUpdate(list));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EsopEquipment> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EsopEquipment  esopEquipment = esopEquipmentService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(esopEquipment,StringUtils.isEmpty(esopEquipment)?0:1);
    }

/*    @ApiOperation("通过IP获取详情")
    @GetMapping("/detailByIp")
    public ResponseEntity<EsopEquipment> detailByIp(@ApiParam(value = "ip",required = true) @RequestParam  @NotNull(message="id不能为空") String ip) {
        EsopEquipment  esopEquipment = esopEquipmentService.detailByIp(ip);
        return  ControllerUtil.returnDataSuccess(esopEquipment,StringUtils.isEmpty(esopEquipment)?0:1);
    }*/

    @ApiOperation("通过IP获取详情")
    @GetMapping("/detailByMacAddress")
    public ResponseEntity<EsopEquipment> detailByMacAddress(@ApiParam(value = "MacAddress",required = true) @RequestParam  @NotNull(message="id不能为空") String macAddress) {
        EsopEquipment  esopEquipment = esopEquipmentService.detailByMacAddress(macAddress);
        return  ControllerUtil.returnDataSuccess(esopEquipment,StringUtils.isEmpty(esopEquipment)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EsopEquipmentDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEsopEquipment searchEsopEquipment) {
        Page<Object> page = PageHelper.startPage(searchEsopEquipment.getStartPage(),searchEsopEquipment.getPageSize());
        List<EsopEquipmentDto> list = esopEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchEsopEquipment));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EsopHtEquipment>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEsopEquipment searchEsopEquipment) {
        Page<Object> page = PageHelper.startPage(searchEsopEquipment.getStartPage(),searchEsopEquipment.getPageSize());
        List<EsopHtEquipment> list = esopEquipmentService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEsopEquipment));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stresop")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEsopEquipment searchEsopEquipment){
    List<EsopEquipmentDto> list = esopEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchEsopEquipment));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "设备信息", "设备信息.xls", response);
    }

    @ApiOperation("Mac地址查询")
    @PostMapping("/findByMac")
    public ResponseEntity<List<EsopEquipmentDto>> findByMac(@RequestParam(value = "mac") Object mac, @RequestParam(value = "orgId") Long orgId) {
        SearchEsopEquipment searchEsopEquipment = new SearchEsopEquipment();
        searchEsopEquipment.setOrgId(orgId);
        searchEsopEquipment.setEquipmentMacAddress(mac.toString());
        List<EsopEquipmentDto> list = esopEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchEsopEquipment));
        return ControllerUtil.returnDataSuccess(list,1);
    }

    @ApiOperation("未绑定分组查询")
    @PostMapping("/findNoGroup")
    public ResponseEntity<List<EsopEquipmentDto>> findNoGroup(@ApiParam(value = "查询对象")@RequestBody SearchEsopEquipment searchEsopEquipment) {
        Page<Object> page = PageHelper.startPage(searchEsopEquipment.getStartPage(),searchEsopEquipment.getPageSize());
        List<EsopEquipmentDto> list = esopEquipmentService.findNoGroup(ControllerUtil.dynamicConditionByEntity(searchEsopEquipment));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
