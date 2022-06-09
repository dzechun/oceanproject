package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamJigRepairOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigRepairOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRepairOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigRepairOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.service.EamHtJigRepairOrderService;
import com.fantechs.provider.eam.service.EamJigRepairOrderService;
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
@Api(tags = "治具维修单")
@RequestMapping("/eamJigRepairOrder")
@Validated
public class EamJigRepairOrderController {

    @Resource
    private EamJigRepairOrderService eamJigRepairOrderService;
    @Resource
    private EamHtJigRepairOrderService eamHtJigRepairOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation("新建维修单")
    @PostMapping("/pdaCreateOrder")
    public ResponseEntity<EamJigRepairOrderDto> pdaCreateOrder(@ApiParam(value = "治具条码",required = true)@RequestParam  @NotBlank(message="治具条码不能为空") String jigBarcode) {
        EamJigRepairOrderDto eamJigRepairOrderDto = eamJigRepairOrderService.pdaCreateOrder(jigBarcode);
        return  ControllerUtil.returnDataSuccess(eamJigRepairOrderDto,StringUtils.isEmpty(eamJigRepairOrderDto)?0:1);
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigRepairOrder eamJigRepairOrder) {
        return ControllerUtil.returnCRUD(eamJigRepairOrderService.save(eamJigRepairOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigRepairOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigRepairOrder.update.class) EamJigRepairOrder eamJigRepairOrder) {
        return ControllerUtil.returnCRUD(eamJigRepairOrderService.update(eamJigRepairOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigRepairOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigRepairOrder  eamJigRepairOrder = eamJigRepairOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigRepairOrder,StringUtils.isEmpty(eamJigRepairOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigRepairOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigRepairOrder searchEamJigRepairOrder) {
        Page<Object> page = PageHelper.startPage(searchEamJigRepairOrder.getStartPage(),searchEamJigRepairOrder.getPageSize());
        List<EamJigRepairOrderDto> list = eamJigRepairOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigRepairOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigRepairOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigRepairOrder searchEamJigRepairOrder) {
        Page<Object> page = PageHelper.startPage(searchEamJigRepairOrder.getStartPage(),searchEamJigRepairOrder.getPageSize());
        List<EamHtJigRepairOrder> list = eamHtJigRepairOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigRepairOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigRepairOrder searchEamJigRepairOrder){
    List<EamJigRepairOrderDto> list = eamJigRepairOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigRepairOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "治具维修单", "治具维修单.xls", response);
    }
}
