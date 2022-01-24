package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderTakeCancel;
import com.fantechs.common.base.general.dto.wms.inner.SaveHaveInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.export.WmsInnerJobOrderExport;
import com.fantechs.common.base.general.dto.wms.inner.imports.WmsInnerJobOrderImport;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */
@RestController
@Api(tags = "上架作业")
@RequestMapping("/wmsInnerJobOrder")
@Validated
@Slf4j
public class WmsInnerJobOrderController {

    @Resource
    private WmsInnerJobOrderService wmsInPutawayOrderService;

    @ApiOperation("自动分配")
    @PostMapping("/autoDistribution")
    public ResponseEntity autoDistribution(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.autoDistribution(ids));
    }

    @ApiOperation("手动分配")
    @PostMapping("/handDistribution")
    public ResponseEntity handDistribution(@RequestBody List<WmsInnerJobOrderDet> list){
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.handDistribution(list));
    }

    @ApiOperation("取消分配")
    @PostMapping("/cancelDistribution")
    public ResponseEntity cancelDistribution(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.cancelDistribution(ids));
    }

    @ApiOperation(" 指定工作人员")
    @PostMapping("/distributionWorker")
    public ResponseEntity distributionWorker(@ApiParam(value = "对象ID",required = true) @RequestParam Long jobOrderId,@ApiParam(value = "人员ID",required = true) @RequestParam Long workerId){
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.distributionWorker(jobOrderId,workerId));
    }

    @ApiOperation("更新条码扫描状态")
    @PostMapping("/updateBarcodeStatus")
    public ResponseEntity updateBarcodeStatus(@ApiParam(value = "条码ID",required = true) @RequestParam @NotBlank(message="materialBarcodeId 不能为空") Long materialBarcodeId) {
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.updateBarcodeStatus(materialBarcodeId));
    }

    @ApiOperation("整单确认")
    @PostMapping("/allReceiving")
    public ResponseEntity allReceiving(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.allReceiving(ids));
    }

    @ApiOperation("单一确认")
    @PostMapping("/singleReceiving")
    public ResponseEntity singleReceiving(@RequestBody(required = true) WmsInnerJobOrderDet wmsInnerJobOrderDet){
        List<WmsInnerJobOrderDet> list = new ArrayList<>();
        list.add(wmsInnerJobOrderDet);
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.singleReceiving(list));
    }

    @ApiOperation("按条码单一确认")
    @PostMapping("/singleReceivingByBarcode")
    public ResponseEntity singleReceivingByBarcode(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerJobOrderDet wmsInPutawayOrderDet,
                                                   @ApiParam(value = "条码ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids,
                                                    @ApiParam(value = "必传，单据类型，1-上架作业, 3-移位作业",required = true) @RequestParam  Byte orderType){
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.singleReceivingByBarcode(wmsInPutawayOrderDet,ids,orderType));
    }

    @ApiOperation("Web端单一确认作业 扫描条码")
    @PostMapping("/checkBarcodeOrderWeb")
    public ResponseEntity<WmsInnerMaterialBarcodeDto> checkBarcodeOrderWeb(@ApiParam(value = "是否系统条码(0 否 1 是)")@RequestParam @NotBlank(message = "是否系统条码不能为空") String ifSysBarcode,
                                                                    @ApiParam(value = "作业单主表id")@RequestParam Long orderId,
                                                                    @ApiParam(value = "明细ID")@RequestParam Long orderDetId,
                                                                    @ApiParam(value = "条码")@RequestParam String barCode){
        WmsInnerMaterialBarcodeDto materialBarcodeDto = wmsInPutawayOrderService.checkBarcodeOrderWeb(ifSysBarcode,orderId,orderDetId,barCode);
        return ControllerUtil.returnDataSuccess(materialBarcodeDto,StringUtils.isEmpty(materialBarcodeDto)?0:1);
    }

    @ApiOperation("上架作业Web端扫描条码提交")
    @PostMapping("/saveHaveInnerJobOrder")
    public ResponseEntity<WmsInnerJobOrderDet> saveHaveInnerJobOrder(@RequestBody(required = true) List<SaveHaveInnerJobOrderDto> list){
        WmsInnerJobOrderDet wmsInnerJobOrderDet=wmsInPutawayOrderService.saveHaveInnerJobOrder(list);
        return ControllerUtil.returnDataSuccess(wmsInnerJobOrderDet,StringUtils.isEmpty(wmsInnerJobOrderDet)?0:1);
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerJobOrder wmsInPutawayOrder) {
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.save(wmsInPutawayOrder));
    }

    @ApiOperation(value = "栈板作业新增上架作业",notes = "栈板作业新增上架作业")
    @PostMapping("/packageAutoAdd")
    public ResponseEntity<WmsInnerJobOrder> packageAutoAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerJobOrder wmsInnerJobOrder) {
        return ControllerUtil.returnDataSuccess(wmsInPutawayOrderService.packageAutoAdd(wmsInnerJobOrder),1);
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.batchDelete(ids));
    }

    @ApiOperation("关闭单据")
    @PostMapping("/closeWmsInnerJobOrder")
    public ResponseEntity closeWmsInnerJobOrder(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.closeWmsInnerJobOrder(ids));
    }

    @ApiOperation("移位单批量删除")
    @PostMapping("/batchDeleteByShiftWork")
    public ResponseEntity batchDeleteByShiftWork(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.batchDeleteByShiftWork(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= WmsInnerJobOrder.update.class) WmsInnerJobOrder wmsInPutawayOrder) {
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.update(wmsInPutawayOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerJobOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id,
                                                   @ApiParam(value = "sourceSysOrderTypeCode",required = true)@RequestParam  @NotNull(message="id不能为空") String sourceSysOrderTypeCode) {
        WmsInnerJobOrder wmsInPutawayOrder = wmsInPutawayOrderService.detail(id,sourceSysOrderTypeCode);
        return  ControllerUtil.returnDataSuccess(wmsInPutawayOrder,StringUtils.isEmpty(wmsInPutawayOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerJobOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerJobOrder searchWmsInPutawayOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInPutawayOrder.getStartPage(),searchWmsInPutawayOrder.getPageSize());
        List<WmsInnerJobOrderDto> list = wmsInPutawayOrderService.findList(searchWmsInPutawayOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerJobOrder searchWmsInPutawayOrder,@ApiParam(value = "类型（1，上架作业 2，拣货作业）",required = true)@RequestParam  @NotNull(message="id不能为空") Byte type){
        String title = "上架作业单信息导出";
        if (type == 1) {
            searchWmsInPutawayOrder.setOrderTypeCode("IN-IWK");
        }else {
            searchWmsInPutawayOrder.setOrderTypeCode("OUT-IWK");
            title = "拣货作业单信息导出";
        }
        List<WmsInnerJobOrderExport> list = wmsInPutawayOrderService.findExportList(ControllerUtil.dynamicConditionByEntity(searchWmsInPutawayOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, title, title, WmsInnerJobOrderExport.class, title+".xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入信息",notes = "从excel导入信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file,
                                      @RequestParam Long stockOrderId){
        try {
            // 导入操作
            List<WmsInnerJobOrderImport> wmsInnerJobOrderImports = EasyPoiUtils.importExcel(file, 0, 1, WmsInnerJobOrderImport.class);
            Map<String, Object> resultMap = wmsInPutawayOrderService.importExcel(wmsInnerJobOrderImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        }catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiIgnore
    @PostMapping("/addList")
    public ResponseEntity addList(@RequestBody List<WmsInnerJobOrder> list){
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.addList(list));
    }

    @ApiIgnore
    @PostMapping("/cancelJobOrder")
    public ResponseEntity cancelJobOrder(@RequestBody List<EngPackingOrderTakeCancel> engPackingOrderTakeCancels){
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.cancelJobOrder(engPackingOrderTakeCancels));
    }

    @PostMapping("/storageCapacity")
    @ApiOperation("库容入库规则判断入库数量")
    public ResponseEntity storageCapacity(@RequestParam Long materialId, @RequestParam Long storageId, @RequestParam BigDecimal qty){
        return ControllerUtil.returnDataSuccess(wmsInPutawayOrderService.storageCapacity(materialId,storageId,qty),1);
    }
}
