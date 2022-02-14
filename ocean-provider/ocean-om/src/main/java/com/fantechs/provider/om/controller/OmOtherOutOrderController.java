package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtOtherOutOrderDto;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDto;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrder;
import com.fantechs.common.base.general.entity.om.search.SearchOmOtherOutOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmOtherOutOrderService;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/06/23.
 */
@RestController
@Api(tags = "其他出库订单")
@RequestMapping("/omOtherOutOrder")
@Validated
public class OmOtherOutOrderController {

    @Resource
    private OmOtherOutOrderService omOtherOutOrderService;

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<OmOtherOutOrderDetDto> omOtherOutOrderDets) {
        return ControllerUtil.returnCRUD(omOtherOutOrderService.pushDown(omOtherOutOrderDets));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmOtherOutOrder omOtherOutOrder) {
        return ControllerUtil.returnCRUD(omOtherOutOrderService.save(omOtherOutOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omOtherOutOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmOtherOutOrder.update.class) OmOtherOutOrder omOtherOutOrder) {
        return ControllerUtil.returnCRUD(omOtherOutOrderService.update(omOtherOutOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmOtherOutOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmOtherOutOrder  omOtherOutOrder = omOtherOutOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omOtherOutOrder,StringUtils.isEmpty(omOtherOutOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmOtherOutOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmOtherOutOrder searchOmOtherOutOrder) {
        Page<Object> page = PageHelper.startPage(searchOmOtherOutOrder.getStartPage(),searchOmOtherOutOrder.getPageSize());
        List<OmOtherOutOrderDto> list = omOtherOutOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmOtherOutOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmHtOtherOutOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmOtherOutOrder searchOmOtherOutOrder) {
        Page<Object> page = PageHelper.startPage(searchOmOtherOutOrder.getStartPage(),searchOmOtherOutOrder.getPageSize());
        List<OmHtOtherOutOrderDto> list = omOtherOutOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmOtherOutOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("拣货数量反写")
    @PostMapping("/otherUpdatePickingQty")
    ResponseEntity otherUpdatePickingQty(@ApiParam(value = "必传明细ID",required = true)@RequestParam Long otherOutOrderDetId,
                                    @ApiParam(value = "必传拣货架数量",required = true)@RequestParam BigDecimal actualQty){
        return ControllerUtil.returnCRUD(omOtherOutOrderService.otherUpdatePickingQty(otherOutOrderDetId,actualQty));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmOtherOutOrder searchOmOtherOutOrder){
    List<OmOtherOutOrderDto> list = omOtherOutOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmOtherOutOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "OmOtherOutOrder信息", OmOtherOutOrderDto.class, "OmOtherOutOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
