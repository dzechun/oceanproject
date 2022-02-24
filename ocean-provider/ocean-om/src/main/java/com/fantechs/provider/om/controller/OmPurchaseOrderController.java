package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtPurchaseOrderDto;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmPurchaseOrderService;
import com.fantechs.provider.om.service.ht.OmHtPurchaseOrderService;
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
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@RestController
@Api(tags = "采购订单")
@RequestMapping("/omPurchaseOrder")
@Validated
public class OmPurchaseOrderController {

    @Resource
    private OmPurchaseOrderService omPurchaseOrderService;
    @Resource
    private OmHtPurchaseOrderService omHtPurchaseOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmPurchaseOrder omPurchaseOrder) {
        return ControllerUtil.returnCRUD(omPurchaseOrderService.save(omPurchaseOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omPurchaseOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmPurchaseOrder.update.class) OmPurchaseOrder omPurchaseOrder) {
        return ControllerUtil.returnCRUD(omPurchaseOrderService.update(omPurchaseOrder));
    }

    @ApiOperation("批量更新累计下发数量")
    @PostMapping("/batchUpdateIssueQty")
    public ResponseEntity updateIssueQty(@ApiParam(value = "对象，Id必传",required = true)@RequestBody List<OmPurchaseOrderDet> list) {
        return ControllerUtil.returnCRUD(omPurchaseOrderService.batchUpdateIssueQty(list));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmPurchaseOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmPurchaseOrder  omPurchaseOrder = omPurchaseOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omPurchaseOrder,StringUtils.isEmpty(omPurchaseOrder)?0:1);
    }

   @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmPurchaseOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseOrder searchOmPurchaseOrder) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseOrder.getStartPage(),searchOmPurchaseOrder.getPageSize());
        List<OmPurchaseOrderDto> list = omPurchaseOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity<OmPurchaseOrder> addOrUpdate(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmPurchaseOrder omPurchaseOrder) {
        OmPurchaseOrder omPurchaseOrders = omPurchaseOrderService.saveByApi(omPurchaseOrder);
        return ControllerUtil.returnDataSuccess(omPurchaseOrders, StringUtils.isEmpty(omPurchaseOrders) ? 0 : 1);
    }

    @ApiOperation("获取采购单明细物料ID")
    @PostMapping("/findPurchaseMaterial")
    public ResponseEntity<String> findPurchaseMaterial(@ApiParam(value = "purchaseOrderCode",required = true)@RequestParam  @NotNull(message="purchaseOrderCode不能为空") String purchaseOrderCode) {
        String  materialId = omPurchaseOrderService.findPurchaseMaterial(purchaseOrderCode);
        return  ControllerUtil.returnDataSuccess(materialId,1);
    }


    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmHtPurchaseOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseOrder searchOmPurchaseOrder) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseOrder.getStartPage(),searchOmPurchaseOrder.getPageSize());
        List<OmHtPurchaseOrderDto> list = omHtPurchaseOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmPurchaseOrder searchOmPurchaseOrder){
        List<OmPurchaseOrderDto> list = omPurchaseOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrder));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "采购订单信息", "OmPurchaseOrder信息", OmPurchaseOrderDto.class, "OmPurchaseOrder.xls", response);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "采购订单明细ID列表，多个逗号分隔",required = true)@RequestBody List<OmPurchaseOrderDet> omPurchaseOrderDets) {
        return ControllerUtil.returnCRUD(omPurchaseOrderService.pushDown(omPurchaseOrderDets));
    }

    @ApiOperation(value = "更新采购订单上架数量",notes = "更新采购订单上架数量")
    @PostMapping("/updatePutawayQty")
    public ResponseEntity updatePutawayQty(@ApiParam(value = "必传操作类型 1 确认 2 删除",required = true)@RequestParam Byte opType,@ApiParam(value = "必传明细ID",required = true)@RequestParam Long purchaseOrderDetId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty) {
        return ControllerUtil.returnCRUD(omPurchaseOrderService.updatePutawayQty(opType,purchaseOrderDetId,putawayQty));
    }

    @ApiOperation(value = "更新采购订单下推数量",notes = "更新采购订单下推数量")
    @PostMapping("/updatePutDownQty")
    public ResponseEntity updatePutDownQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long purchaseOrderDetId, @ApiParam(value = "必传上架数量",required = true)@RequestParam BigDecimal putawayQty) {
        return ControllerUtil.returnCRUD(omPurchaseOrderService.updatePutDownQty(purchaseOrderDetId,putawayQty));
    }
}
