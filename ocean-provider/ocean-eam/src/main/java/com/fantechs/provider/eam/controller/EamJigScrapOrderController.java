package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamJigScrapOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigScrapOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigScrapOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigScrapOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.service.EamHtJigScrapOrderService;
import com.fantechs.provider.eam.service.EamJigScrapOrderService;
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
 * Created by leifengzhi on 2021/08/19.
 */
@RestController
@Api(tags = "治具报废单")
@RequestMapping("/eamJigScrapOrder")
@Validated
public class EamJigScrapOrderController {

    @Resource
    private EamJigScrapOrderService eamJigScrapOrderService;
    @Resource
    private EamHtJigScrapOrderService eamHtJigScrapOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "自动生成报废单",notes = "自动生成报废单")
    @PostMapping("/autoCreateOrder")
    public ResponseEntity autoCreateOrder() {
        return ControllerUtil.returnCRUD(eamJigScrapOrderService.autoCreateOrder());
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigScrapOrderDto eamJigScrapOrder) {
        return ControllerUtil.returnCRUD(eamJigScrapOrderService.save(eamJigScrapOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigScrapOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigScrapOrder.update.class) EamJigScrapOrderDto eamJigScrapOrder) {
        return ControllerUtil.returnCRUD(eamJigScrapOrderService.update(eamJigScrapOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigScrapOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigScrapOrder  eamJigScrapOrder = eamJigScrapOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigScrapOrder,StringUtils.isEmpty(eamJigScrapOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigScrapOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigScrapOrder searchEamJigScrapOrder) {
        Page<Object> page = PageHelper.startPage(searchEamJigScrapOrder.getStartPage(),searchEamJigScrapOrder.getPageSize());
        List<EamJigScrapOrderDto> list = eamJigScrapOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigScrapOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigScrapOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigScrapOrder searchEamJigScrapOrder) {
        Page<Object> page = PageHelper.startPage(searchEamJigScrapOrder.getStartPage(),searchEamJigScrapOrder.getPageSize());
        List<EamHtJigScrapOrder> list = eamHtJigScrapOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigScrapOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigScrapOrder searchEamJigScrapOrder){
    List<EamJigScrapOrderDto> list = eamJigScrapOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigScrapOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "治具报废单", "治具报废单.xls", response);
    }
}
