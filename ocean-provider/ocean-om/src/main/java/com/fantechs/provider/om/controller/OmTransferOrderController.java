package com.fantechs.provider.om.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmHtTransferOrderDto;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDto;
import com.fantechs.common.base.general.entity.om.OmTransferOrder;
import com.fantechs.common.base.general.entity.om.search.SearchOmTransferOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmTransferOrderService;
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
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/06/15.
 */
@RestController
@Api(tags = "调拨订单")
@RequestMapping("/omTransferOrder")
@Validated
public class OmTransferOrderController {

    @Resource
    private OmTransferOrderService omTransferOrderService;

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<OmTransferOrderDetDto> omTransferOrderDetDtos) {
        return ControllerUtil.returnCRUD(omTransferOrderService.pushDown(omTransferOrderDetDtos));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmTransferOrder omTransferOrder) {
        return ControllerUtil.returnCRUD(omTransferOrderService.save(omTransferOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omTransferOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmTransferOrder.update.class) OmTransferOrder omTransferOrder) {
        return ControllerUtil.returnCRUD(omTransferOrderService.update(omTransferOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmTransferOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmTransferOrder  omTransferOrder = omTransferOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omTransferOrder,StringUtils.isEmpty(omTransferOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmTransferOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmTransferOrder searchOmTransferOrder) {
        Page<Object> page = PageHelper.startPage(searchOmTransferOrder.getStartPage(),searchOmTransferOrder.getPageSize());
        List<OmTransferOrderDto> list = omTransferOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmTransferOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmTransferOrder searchOmTransferOrder){
    List<OmTransferOrderDto> list = omTransferOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchOmTransferOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "OmTransferOrder信息", OmTransferOrderDto.class, "OmTransferOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("修改单据状态")
    @PostMapping("/updateStatus")
    public ResponseEntity updateStatus(@RequestBody OmTransferOrder omTransferOrder){
        return ControllerUtil.returnCRUD(omTransferOrderService.updateStatus(omTransferOrder));
    }

    @ApiOperation("履历")
    @PostMapping("/findHtList")
    public ResponseEntity<List<OmHtTransferOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchOmTransferOrder searchOmTransferOrder) {
        Page<Object> page = PageHelper.startPage(searchOmTransferOrder.getStartPage(),searchOmTransferOrder.getPageSize());
        List<OmHtTransferOrderDto> list = omTransferOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchOmTransferOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
