package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamEquInspectionOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquPointInspectionOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.service.EamEquPointInspectionOrderService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */
@RestController
@Api(tags = "设备点检单")
@RequestMapping("/eamEquPointInspectionOrder")
@Validated
public class EamEquPointInspectionOrderController {

    @Resource
    private EamEquPointInspectionOrderService eamEquPointInspectionOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation("新建点检单")
    @PostMapping("/pdaCreateOrder")
    public ResponseEntity<EamEquPointInspectionOrderDto> pdaCreateOrder(@ApiParam(value = "设备条码",required = true)@RequestParam  @NotBlank(message="设备条码不能为空") String equipmentBarcode) {
        EamEquPointInspectionOrderDto eamEquPointInspectionOrderDto = eamEquPointInspectionOrderService.pdaCreateOrder(equipmentBarcode);
        return  ControllerUtil.returnDataSuccess(eamEquPointInspectionOrderDto,StringUtils.isEmpty(eamEquPointInspectionOrderDto)?0:1);
    }

    @ApiOperation("提交")
    @PostMapping("/pdaSubmit")
    public ResponseEntity pdaSubmit(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquPointInspectionOrder.update.class) EamEquPointInspectionOrder eamEquPointInspectionOrder) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionOrderService.pdaSubmit(eamEquPointInspectionOrder));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquPointInspectionOrder eamEquPointInspectionOrder) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionOrderService.save(eamEquPointInspectionOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquPointInspectionOrder.update.class) EamEquPointInspectionOrder eamEquPointInspectionOrder) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionOrderService.update(eamEquPointInspectionOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquPointInspectionOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquPointInspectionOrder  eamEquPointInspectionOrder = eamEquPointInspectionOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquPointInspectionOrder,StringUtils.isEmpty(eamEquPointInspectionOrder)?0:1);
    }

    @ApiOperation("通过设备条码获取其他信息")
    @GetMapping("/findEamEquOrderDto")
    public ResponseEntity<EamEquInspectionOrderDto> findEamEquOrderDto(@ApiParam(value = "查询对象") @RequestParam String equipmentBarcode){
        HashMap<String, Object> map = new HashMap<>();
        map.put("equipmentBarcode", equipmentBarcode);
        return ControllerUtil.returnDataSuccess(eamEquPointInspectionOrderService.findListForOrder(map), 1);
    }


    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquPointInspectionOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquPointInspectionOrder searchEamEquPointInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchEamEquPointInspectionOrder.getStartPage(),searchEamEquPointInspectionOrder.getPageSize());
        List<EamEquPointInspectionOrderDto> list = eamEquPointInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamEquPointInspectionOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquPointInspectionOrder searchEamEquPointInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchEamEquPointInspectionOrder.getStartPage(),searchEamEquPointInspectionOrder.getPageSize());
        List<EamEquPointInspectionOrderDto> list = eamEquPointInspectionOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquPointInspectionOrder searchEamEquPointInspectionOrder){
    List<EamEquPointInspectionOrderDto> list = eamEquPointInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "设备点检单", "设备点检单.xls", response);
    }
}
