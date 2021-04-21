package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDto;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmSalesOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/04/19.
 */
@RestController
@Api(tags = "销售订单管理")
@RequestMapping("/omSalesOrder")
@Validated
public class OmSalesOrderController {

    @Resource
    private OmSalesOrderService omSalesOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmSalesOrder omSalesOrder) {
        return ControllerUtil.returnCRUD(omSalesOrderService.save(omSalesOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omSalesOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmSalesOrder.update.class) OmSalesOrder omSalesOrder) {
        return ControllerUtil.returnCRUD(omSalesOrderService.update(omSalesOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmSalesOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmSalesOrder  omSalesOrder = omSalesOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omSalesOrder,StringUtils.isEmpty(omSalesOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmSalesOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesOrderDto searchOmSalesOrderDto) {
        Page<Object> page = PageHelper.startPage(searchOmSalesOrderDto.getStartPage(),searchOmSalesOrderDto.getPageSize());
        List<OmSalesOrderDto> list = omSalesOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrderDto));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<OmSalesOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmSalesOrderDto searchOmSalesOrder) {
//        Page<Object> page = PageHelper.startPage(searchOmSalesOrder.getStartPage(),searchOmSalesOrder.getPageSize());
//        List<OmSalesOrderDto> list = omSalesOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrder));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchOmSalesOrderDto searchOmSalesOrder){
//    List<OmSalesOrderDto> list = omSalesOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmSalesOrder));
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "OmSalesOrder信息", OmSalesOrderDto.class, "OmSalesOrder.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }
}
