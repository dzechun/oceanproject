package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmOtherInOrderDto;
import com.fantechs.common.base.general.entity.om.OmOtherInOrder;
import com.fantechs.common.base.general.entity.om.OmOtherInOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmOtherInOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmOtherInOrderService;
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
 *
 * Created by leifengzhi on 2021/06/21.
 */
@RestController
@Api(tags = "其他入库订单")
@RequestMapping("/omOtherInOrder")
@Validated
public class OmOtherInOrderController {

    @Resource
    private OmOtherInOrderService omOtherInOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmOtherInOrder omOtherInOrder) {
        return ControllerUtil.returnCRUD(omOtherInOrderService.save(omOtherInOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omOtherInOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmOtherInOrder.update.class) OmOtherInOrder omOtherInOrder) {
        return ControllerUtil.returnCRUD(omOtherInOrderService.update(omOtherInOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmOtherInOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmOtherInOrder  omOtherInOrder = omOtherInOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omOtherInOrder,StringUtils.isEmpty(omOtherInOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmOtherInOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmOtherInOrder searchOmOtherInOrder) {
        Page<Object> page = PageHelper.startPage(searchOmOtherInOrder.getStartPage(),searchOmOtherInOrder.getPageSize());
        List<OmOtherInOrderDto> list = omOtherInOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmOtherInOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmOtherInOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmOtherInOrder searchOmOtherInOrder) {
        Page<Object> page = PageHelper.startPage(searchOmOtherInOrder.getStartPage(),searchOmOtherInOrder.getPageSize());
        List<OmOtherInOrderDto> list = omOtherInOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmOtherInOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmOtherInOrder searchOmOtherInOrder){
    List<OmOtherInOrderDto> list = omOtherInOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmOtherInOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "OmOtherInOrder信息", OmOtherInOrderDto.class, "OmOtherInOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("下发生成出库单")
    @PostMapping("/packageAutoOutOrder")
    public ResponseEntity packageAutoOutOrder(@RequestBody(required = true) OmOtherInOrder omOtherInOrder){
        return ControllerUtil.returnCRUD(omOtherInOrderService.packageAutoOutOrder(omOtherInOrder));
    }

    @ApiOperation("数量反写")
    @PostMapping("/writeQty")
    public ResponseEntity writeQty(@RequestBody OmOtherInOrderDet omOtherInOrderDet){
        return ControllerUtil.returnCRUD(omOtherInOrderService.writeQty(omOtherInOrderDet));
    }

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "其他入库计划ID列表，多个逗号分隔",required = true)@RequestBody  List<OmOtherInOrderDet> omOtherInOrderDets) {
        return ControllerUtil.returnCRUD(omOtherInOrderService.pushDown(omOtherInOrderDets));
    }
}
