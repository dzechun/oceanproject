package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutPlanDeliveryOrderImport;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanDeliveryOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.wms.out.service.WmsOutPlanDeliveryOrderService;
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
@Api(tags = "出库计划")
@RequestMapping("/wmsOutPlanDeliveryOrder")
@Validated
@Slf4j
public class WmsOutPlanDeliveryOrderController {

    @Resource
    private WmsOutPlanDeliveryOrderService wmsOutPlanDeliveryOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<WmsOutPlanDeliveryOrderDetDto> wmsOutPlanDeliveryOrderDetDtos) {
        return ControllerUtil.returnCRUD(wmsOutPlanDeliveryOrderService.pushDown(wmsOutPlanDeliveryOrderDetDtos));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutPlanDeliveryOrderDto wmsOutPlanDeliveryOrderDto) {
        return ControllerUtil.returnCRUD(wmsOutPlanDeliveryOrderService.save(wmsOutPlanDeliveryOrderDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutPlanDeliveryOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutPlanDeliveryOrder.update.class) WmsOutPlanDeliveryOrderDto wmsOutPlanDeliveryOrderDto) {
        return ControllerUtil.returnCRUD(wmsOutPlanDeliveryOrderService.update(wmsOutPlanDeliveryOrderDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutPlanDeliveryOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutPlanDeliveryOrder  wmsOutPlanDeliveryOrder = wmsOutPlanDeliveryOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutPlanDeliveryOrder,StringUtils.isEmpty(wmsOutPlanDeliveryOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutPlanDeliveryOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutPlanDeliveryOrder searchWmsOutPlanDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutPlanDeliveryOrder.getStartPage(),searchWmsOutPlanDeliveryOrder.getPageSize());
        List<WmsOutPlanDeliveryOrderDto> list = wmsOutPlanDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsOutPlanDeliveryOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsOutPlanDeliveryOrder searchWmsOutPlanDeliveryOrder) {
        List<WmsOutPlanDeliveryOrderDto> list = wmsOutPlanDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutHtPlanDeliveryOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutPlanDeliveryOrder searchWmsOutPlanDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutPlanDeliveryOrder.getStartPage(),searchWmsOutPlanDeliveryOrder.getPageSize());
        List<WmsOutHtPlanDeliveryOrder> list = wmsOutPlanDeliveryOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutPlanDeliveryOrder searchWmsOutPlanDeliveryOrder){
    List<WmsOutPlanDeliveryOrderDto> list = wmsOutPlanDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutPlanDeliveryOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "WmsOutPlanDeliveryOrder信息", "WmsOutPlanDeliveryOrder.xls", response);
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsOutPlanDeliveryOrderImport> wmsOutPlanDeliveryOrderImports = EasyPoiUtils.importExcel(file, 2, 1, WmsOutPlanDeliveryOrderImport.class);
            Map<String, Object> resultMap = wmsOutPlanDeliveryOrderService.importExcel(wmsOutPlanDeliveryOrderImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation(value = "更新出库计划上架数量",notes = "更新出库计划上架数量")
    @PostMapping("/updatePutawayQty")
    public ResponseEntity updatePlanDeliveryOrderPutawayQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long planDeliveryOrderDetId,
                                           @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty) {
        return ControllerUtil.returnCRUD(wmsOutPlanDeliveryOrderService.updatePutawayQty(planDeliveryOrderDetId,putawayQty));
    }
}
