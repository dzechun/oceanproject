package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryReqOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutDeliveryReqOrderImport;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryReqOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryReqOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryReqOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryReqOrderService;
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
 * Created by leifengzhi on 2021/12/21.
 */
@RestController
@Api(tags = "出库通知单")
@RequestMapping("/wmsOutDeliveryReqOrder")
@Validated
@Slf4j
public class WmsOutDeliveryReqOrderController {

    @Resource
    private WmsOutDeliveryReqOrderService wmsOutDeliveryReqOrderService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<WmsOutDeliveryReqOrderDetDto> wmsOutDeliveryReqOrderDetDtos) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryReqOrderService.pushDown(wmsOutDeliveryReqOrderDetDtos));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutDeliveryReqOrderDto wmsOutDeliveryReqOrderDto) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryReqOrderService.save(wmsOutDeliveryReqOrderDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryReqOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutDeliveryReqOrder.update.class) WmsOutDeliveryReqOrderDto wmsOutDeliveryReqOrderDto) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryReqOrderService.update(wmsOutDeliveryReqOrderDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutDeliveryReqOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutDeliveryReqOrder  wmsOutDeliveryReqOrder = wmsOutDeliveryReqOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutDeliveryReqOrder,StringUtils.isEmpty(wmsOutDeliveryReqOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutDeliveryReqOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDeliveryReqOrder searchWmsOutDeliveryReqOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryReqOrder.getStartPage(),searchWmsOutDeliveryReqOrder.getPageSize());
        List<WmsOutDeliveryReqOrderDto> list = wmsOutDeliveryReqOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryReqOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsOutDeliveryReqOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsOutDeliveryReqOrder searchWmsOutDeliveryReqOrder) {
        List<WmsOutDeliveryReqOrderDto> list = wmsOutDeliveryReqOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryReqOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutHtDeliveryReqOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDeliveryReqOrder searchWmsOutDeliveryReqOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryReqOrder.getStartPage(),searchWmsOutDeliveryReqOrder.getPageSize());
        List<WmsOutHtDeliveryReqOrder> list = wmsOutDeliveryReqOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryReqOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutDeliveryReqOrder searchWmsOutDeliveryReqOrder){
        List<WmsOutDeliveryReqOrderDto> list = wmsOutDeliveryReqOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryReqOrder));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "出库通知单", "出库通知单.xls", response);
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<WmsOutDeliveryReqOrderImport> wmsOutDeliveryReqOrderImports = EasyPoiUtils.importExcel(file, 2, 1, WmsOutDeliveryReqOrderImport.class);
            Map<String, Object> resultMap = wmsOutDeliveryReqOrderService.importExcel(wmsOutDeliveryReqOrderImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation(value = "更新出库通知单上架数量",notes = "更新出库通知单上架数量")
    @PostMapping("/updatePutawayQty")
    public ResponseEntity updateDeliveryReqOrderPutawayQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long deliveryReqOrderDetId,
                                           @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryReqOrderService.updatePutawayQty(deliveryReqOrderDetId,putawayQty));
    }
}
