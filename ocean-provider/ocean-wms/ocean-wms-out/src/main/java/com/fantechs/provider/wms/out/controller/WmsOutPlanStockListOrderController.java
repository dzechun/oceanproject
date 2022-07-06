package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutPlanStockListOrderImport;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanStockListOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanStockListOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.wms.out.service.WmsOutPlanStockListOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */
@RestController
@Api(tags = "备料计划控制器")
@RequestMapping("/wmsOutPlanStockListOrder")
@Validated
@Slf4j
public class WmsOutPlanStockListOrderController {

    @Resource
    private WmsOutPlanStockListOrderService wmsOutPlanStockListOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderService.save(wmsOutPlanStockListOrderDto));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/updateActualQty")
    public ResponseEntity updateActualQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long planStockListOrderDetId,
                                          @ApiParam(value = "必传拣货架数量",required = true)@RequestParam BigDecimal actualQty) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderService.updateActualQty(planStockListOrderDetId,actualQty));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutPlanStockListOrderDto.update.class) WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderService.update(wmsOutPlanStockListOrderDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutPlanStockListOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutPlanStockListOrder  wmsOutPlanStockListOrder = wmsOutPlanStockListOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutPlanStockListOrder,StringUtils.isEmpty(wmsOutPlanStockListOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutPlanStockListOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutPlanStockListOrder.getStartPage(),searchWmsOutPlanStockListOrder.getPageSize());
        List<WmsOutPlanStockListOrderDto> list = wmsOutPlanStockListOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanStockListOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("履历")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutHtPlanStockListOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutPlanStockListOrder.getStartPage(),searchWmsOutPlanStockListOrder.getPageSize());
        List<WmsOutHtPlanStockListOrder> list = wmsOutPlanStockListOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanStockListOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsOutPlanStockListOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder) {
        List<WmsOutPlanStockListOrderDto> list = wmsOutPlanStockListOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanStockListOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<WmsOutPlanStockListOrderDetDto> wmsOutPlanStockListOrderDetDtos) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderService.pushDown(wmsOutPlanStockListOrderDetDtos));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder){
    List<WmsOutPlanStockListOrderDto> list = wmsOutPlanStockListOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanStockListOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "WmsOutPlanStockListOrder信息", "WmsOutPlanStockListOrder.xls", response);
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsOutPlanStockListOrderImport> wmsOutPlanStockListOrderImports = EasyPoiUtils.importExcel(file, 2, 1, WmsOutPlanStockListOrderImport.class);
            Map<String, Object> resultMap = wmsOutPlanStockListOrderService.importExcel(wmsOutPlanStockListOrderImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}