package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
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
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */
@RestController
@Api(tags = "上架作业")
@RequestMapping("/wmsInnerJobOrder")
@Validated
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
    public ResponseEntity<WmsInnerJobOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerJobOrder wmsInPutawayOrder = wmsInPutawayOrderService.selectByKey(id);
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
    @RequestBody(required = false) SearchWmsInnerJobOrder searchWmsInPutawayOrder){
    List<WmsInnerJobOrderDto> list = wmsInPutawayOrderService.findList(searchWmsInPutawayOrder);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInPutawayOrder信息", WmsInnerJobOrderDto.class, "WmsInPutawayOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiIgnore
    @PostMapping("/addList")
    public ResponseEntity addList(@RequestBody List<WmsInnerJobOrder> list){
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.addList(list));
    }

    @PostMapping("/storageCapacity")
    @ApiOperation("库容入库规则判断入库数量")
    public ResponseEntity storageCapacity(@RequestParam Long materialId, @RequestParam Long storageId, @RequestParam BigDecimal qty){
        return ControllerUtil.returnDataSuccess(wmsInPutawayOrderService.storageCapacity(materialId,storageId,qty),1);
    }

    @PostMapping("/reCreateInnerJobShift")
    @ApiOperation("修改样本数重新处理质检移位单")
    public ResponseEntity reCreateInnerJobShift(@RequestParam Long jobOrderId, @RequestParam BigDecimal qty){
        return ControllerUtil.returnDataSuccess(wmsInPutawayOrderService.reCreateInnerJobShift(jobOrderId,qty),1);
    }

    @PostMapping("/updateShit")
    @ApiOperation("复检重新处理质检移位单")
    public ResponseEntity updateShit(@RequestParam Long jobOrderId, @RequestParam BigDecimal ngQty){
        return ControllerUtil.returnDataSuccess(wmsInPutawayOrderService.updateShit(jobOrderId,ngQty),1);
    }

    @PostMapping("/autoRecheck")
    @ApiOperation("走产线自动复检，拆明细")
    public ResponseEntity autoRecheck(@RequestParam String relatedOrderCode){
        return ControllerUtil.returnCRUD(wmsInPutawayOrderService.autoRecheck(relatedOrderCode));
    }
}
