package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.SaveWorkOrderAndBom;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.SmtHtWorkOrderService;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderService;
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
    private SmtHtWorkOrderService smtHtWorkOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialId、workOrderQuantity、proLineId、routeId",required = true)@RequestBody @Validated MesPmWorkOrder mesPmWorkOrder) {
        return ControllerUtil.returnCRUD(mesPmWorkOrderService.save(mesPmWorkOrder));
    }

    @ApiOperation(value = "新增及更新工单及BOM",notes = "新增及更新工单及BOM")
    @PostMapping("/save")
    public ResponseEntity save(@ApiParam(value = "保存工单及工单BOM",required = true)@RequestBody SaveWorkOrderAndBom saveWorkOrderAndBom) {
        mesPmWorkOrderService.saveWorkOrderDTO(saveWorkOrderAndBom);
        return ControllerUtil.returnDataSuccess(saveWorkOrderAndBom.getMesPmWorkOrder().getWorkOrderId(),1);
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

    @ApiOperation("工单历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<MesPmHtWorkOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmWorkOrder searchMesPmWorkOrder) {
        Page<Object> page = PageHelper.startPage(searchMesPmWorkOrder.getStartPage(), searchMesPmWorkOrder.getPageSize());
        List<MesPmHtWorkOrder> list = smtHtWorkOrderService.findList(searchMesPmWorkOrder);
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
}
