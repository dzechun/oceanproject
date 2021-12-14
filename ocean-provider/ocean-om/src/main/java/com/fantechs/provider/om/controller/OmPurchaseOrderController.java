package com.fantechs.provider.om.controller;

import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmPurchaseOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "采购订单明细ID列表，多个逗号分隔",required = true)@RequestParam  @NotBlank(message="采购订单明细ID不能为空") String ids) {
        return ControllerUtil.returnCRUD(omPurchaseOrderService.pushDown(ids));
    }
}
