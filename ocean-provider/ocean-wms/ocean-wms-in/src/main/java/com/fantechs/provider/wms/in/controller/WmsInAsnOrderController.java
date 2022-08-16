package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInHtAsnOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrder;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.provider.wms.in.service.WmsInAsnOrderService;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
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
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/04/29.
 */
@RestController
@Api(tags = "完工入库")
@RequestMapping("/wmsInAsnOrder")
@Validated
public class WmsInAsnOrderController {

    @Resource
    private WmsInAsnOrderService wmsInAsnOrderService;
    @Resource
    private RedisUtil redisUtil;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInAsnOrder wmsInAsnOrder) {
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.save(wmsInAsnOrder));
    }

    @ApiOperation(value = "展板作业新增完工入库-上架作业",notes = "新增")
    @ApiIgnore
    @PostMapping("/packageAutoAdd")
    public ResponseEntity<WmsInAsnOrder> packageAutoAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInAsnOrder wmsInAsnOrder) {
        return ControllerUtil.returnDataSuccess(wmsInAsnOrderService.packageAutoAdd(wmsInAsnOrder),1);
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInAsnOrder.update.class) WmsInAsnOrder wmsInAsnOrder) {
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.update(wmsInAsnOrder));
    }

    @PostMapping("/writeQty")
    public ResponseEntity writeQty(@RequestBody WmsInAsnOrderDet wmsInAsnOrderDet){
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.writeQty(wmsInAsnOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInAsnOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInAsnOrder  wmsInAsnOrder = wmsInAsnOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInAsnOrder,StringUtils.isEmpty(wmsInAsnOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInAsnOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInAsnOrder searchWmsInAsnOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInAsnOrder.getStartPage(),searchWmsInAsnOrder.getPageSize());
        List<WmsInAsnOrderDto> list = wmsInAsnOrderService.findList(searchWmsInAsnOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("整单收货")
    @PostMapping("/allReceiving")
    public ResponseEntity allReceiving(@ApiParam(value = "对象ID列表，多个逗号分隔")@RequestParam @NotNull(message = "id不能为空")String ids){
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.allReceiving(ids,null));
    }

    @ApiOperation("单一收货")
    @PostMapping("/singleReceiving")
    public ResponseEntity singleReceiving(@RequestBody WmsInAsnOrderDet wmsInAsnOrderDet){
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.singleReceiving(wmsInAsnOrderDet));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInAsnOrder searchWmsInAsnOrder){
    List<WmsInAsnOrderDto> list = wmsInAsnOrderService.findList(searchWmsInAsnOrder);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "完工入库单信息", WmsInAsnOrderDto.class, "WmsInAsnOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("创建作业单")
    @PostMapping("/createInnerJobOrder")
    public ResponseEntity createInnerJobOrder(@ApiParam(value = "完工入库id")@RequestParam Long asnOrderId){
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.createInnerJobOrder(asnOrderId));
    }

    @ApiOperation("栈板作业生成完工入库单")
    @PostMapping("/palletAutoAsnOrder")
    public ResponseEntity palletAutoAsnOrder(@RequestBody PalletAutoAsnDto palletAutoAsnDto){
        return ControllerUtil.returnCRUD(wmsInAsnOrderService.palletAutoAsnOrder(palletAutoAsnDto));
    }

    @ApiOperation("履历")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInHtAsnOrderDto>> findHtList(@RequestBody SearchWmsInAsnOrder searchWmsInAsnOrder){
        Page<Object> page = PageHelper.startPage(searchWmsInAsnOrder.getStartPage(),searchWmsInAsnOrder.getPageSize());
        List<WmsInHtAsnOrderDto> list = wmsInAsnOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInAsnOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
