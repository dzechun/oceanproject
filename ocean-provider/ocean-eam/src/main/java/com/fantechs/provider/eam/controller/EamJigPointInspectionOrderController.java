package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamJigPointInspectionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigPointInspectionOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigPointInspectionOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigPointInspectionOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.service.EamHtJigPointInspectionOrderService;
import com.fantechs.provider.eam.service.EamJigPointInspectionOrderService;
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
 * Created by leifengzhi on 2021/08/16.
 */
@RestController
@Api(tags = "治具点检单")
@RequestMapping("/eamJigPointInspectionOrder")
@Validated
public class EamJigPointInspectionOrderController {

    @Resource
    private EamJigPointInspectionOrderService eamJigPointInspectionOrderService;
    @Resource
    private EamHtJigPointInspectionOrderService eamHtJigPointInspectionOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation("新建点检单")
    @PostMapping("/pdaCreateOrder")
    public ResponseEntity<EamJigPointInspectionOrderDto> pdaCreateOrder(@ApiParam(value = "治具条码",required = true)@RequestParam  @NotBlank(message="治具条码不能为空") String jigBarcode) {
        EamJigPointInspectionOrderDto  eamJigMaintainOrderDto = eamJigPointInspectionOrderService.pdaCreateOrder(jigBarcode);
        return  ControllerUtil.returnDataSuccess(eamJigMaintainOrderDto,StringUtils.isEmpty(eamJigMaintainOrderDto)?0:1);
    }

    @ApiOperation("提交")
    @PostMapping("/pdaSubmit")
    public ResponseEntity pdaSubmit(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigPointInspectionOrder.update.class) EamJigPointInspectionOrder eamJigPointInspectionOrder) {
        return ControllerUtil.returnCRUD(eamJigPointInspectionOrderService.pdaSubmit(eamJigPointInspectionOrder));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigPointInspectionOrder eamJigPointInspectionOrder) {
        return ControllerUtil.returnCRUD(eamJigPointInspectionOrderService.save(eamJigPointInspectionOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigPointInspectionOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigPointInspectionOrder.update.class) EamJigPointInspectionOrder eamJigPointInspectionOrder) {
        return ControllerUtil.returnCRUD(eamJigPointInspectionOrderService.update(eamJigPointInspectionOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigPointInspectionOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigPointInspectionOrder  eamJigPointInspectionOrder = eamJigPointInspectionOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigPointInspectionOrder,StringUtils.isEmpty(eamJigPointInspectionOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigPointInspectionOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigPointInspectionOrder searchEamJigPointInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchEamJigPointInspectionOrder.getStartPage(),searchEamJigPointInspectionOrder.getPageSize());
        List<EamJigPointInspectionOrderDto> list = eamJigPointInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigPointInspectionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigPointInspectionOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigPointInspectionOrder searchEamJigPointInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchEamJigPointInspectionOrder.getStartPage(),searchEamJigPointInspectionOrder.getPageSize());
        List<EamHtJigPointInspectionOrder> list = eamHtJigPointInspectionOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigPointInspectionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigPointInspectionOrder searchEamJigPointInspectionOrder){
    List<EamJigPointInspectionOrderDto> list = eamJigPointInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigPointInspectionOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "治具点检单", "治具点检单.xls", response);
    }
}
