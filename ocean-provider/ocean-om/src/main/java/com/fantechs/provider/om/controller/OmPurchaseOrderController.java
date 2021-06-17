package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@RestController
@Api(tags = "omPurchaseOrder控制器")
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

/*    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmPurchaseOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseOrder searchOmPurchaseOrder) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseOrder.getStartPage(),searchOmPurchaseOrder.getPageSize());
        List<OmPurchaseOrderDto> list = omPurchaseOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmPurchaseOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseOrder searchOmPurchaseOrder) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseOrder.getStartPage(),searchOmPurchaseOrder.getPageSize());
        List<OmPurchaseOrder> list = omPurchaseOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmPurchaseOrder searchOmPurchaseOrder){
    List<OmPurchaseOrderDto> list = omPurchaseOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "OmPurchaseOrder信息", OmPurchaseOrderDto.class, "OmPurchaseOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }*/
}
