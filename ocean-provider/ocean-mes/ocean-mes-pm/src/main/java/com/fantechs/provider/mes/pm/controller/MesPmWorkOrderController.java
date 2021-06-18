package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderService;
import com.fantechs.provider.mes.pm.service.MesPmHtWorkOrderService;
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

/**
 * @author hyc
 * @version 2.0
 * @updateTime 2021/04/09 14:55
 * 原smtWorkOrder改名而来
 */
@RestController
@Api(tags = "生产管理-工单管理")
@RequestMapping("/mesPmWorkOrder")
@Validated
public class MesPmWorkOrderController {

    /*修改内容*/


    /*修改内容*/


    /*原内容*/
    @Resource
    private MesPmWorkOrderService mesPmWorkOrderService;

    @Resource
    private MesPmHtWorkOrderService mesPmHtWorkOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialId、workOrderQuantity、proLineId、routeId",required = true)@RequestBody @Validated MesPmWorkOrder mesPmWorkOrder) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderService.save(mesPmWorkOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody MesPmWorkOrder mesPmWorkOrder) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderService.update(mesPmWorkOrder));
    }

    @ApiOperation("批量修改完工数量")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(List<MesPmWorkOrder> mesPmWorkOrders){
        return ControllerUtil.returnCRUD(mesPmWorkOrderService.batchUpdate(mesPmWorkOrders));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmWorkOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmWorkOrder mesPmWorkOrder = mesPmWorkOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmWorkOrder,StringUtils.isEmpty(mesPmWorkOrder)?0:1);
    }

    @ApiOperation("工单列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmWorkOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmWorkOrder searchMesPmWorkOrder) {
        Page<Object> page = PageHelper.startPage(searchMesPmWorkOrder.getStartPage(), searchMesPmWorkOrder.getPageSize());
        List<MesPmWorkOrderDto> list = mesPmWorkOrderService.findList(searchMesPmWorkOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("工单列表")
    @PostMapping("/getWorkOrderList")
    public ResponseEntity<List<MesPmWorkOrder>> getWorkOrderList(@ApiParam(value = "查询对象") @RequestBody List<String> workOrderIds){
        List<MesPmWorkOrder> workOrderList = mesPmWorkOrderService.getWorkOrderList(workOrderIds);
        return ControllerUtil.returnDataSuccess(workOrderList, workOrderList.size());
    }

    @ApiOperation("工单历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesPmHtWorkOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmWorkOrder searchMesPmWorkOrder) {
        Page<Object> page = PageHelper.startPage(searchMesPmWorkOrder.getStartPage(), searchMesPmWorkOrder.getPageSize());
        List<MesPmHtWorkOrder> list = mesPmHtWorkOrderService.findList(searchMesPmWorkOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchMesPmWorkOrder searchMesPmWorkOrder){
    List<MesPmWorkOrderDto> list = mesPmWorkOrderService.findList(searchMesPmWorkOrder);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出工单信息", "工单信息", MesPmWorkOrderDto.class, "工单信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping("/updateInventory")
    public ResponseEntity updateInventory(@RequestBody MesPmWorkOrder mesPmWorkOrder){
        return ControllerUtil.returnCRUD(mesPmWorkOrderService.updateInventoryQty(mesPmWorkOrder));
    }
    /*原内容*/

    @ApiOperation(value = "接口更新",notes = "接口更新")
    @PostMapping("/updateById")
    public ResponseEntity<MesPmWorkOrder> updateById(@RequestBody MesPmWorkOrder mesPmWorkOrder) {
        mesPmWorkOrder = mesPmWorkOrderService.updateById(mesPmWorkOrder);
        return ControllerUtil.returnDataSuccess(mesPmWorkOrder,StringUtils.isEmpty(mesPmWorkOrder)?0:1);
    }
}
