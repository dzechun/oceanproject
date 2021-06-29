package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamRequisitionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtRequisitionOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamRequisitionOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtRequisitionOrderService;
import com.fantechs.provider.eam.service.EamRequisitionOrderService;
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
 * Created by leifengzhi on 2021/06/29.
 */
@RestController
@Api(tags = "设备领用")
@RequestMapping("/eamRequisitionOrder")
@Validated
public class EamRequisitionOrderController {

    @Resource
    private EamRequisitionOrderService eamRequisitionOrderService;
    @Resource
    private EamHtRequisitionOrderService eamHtRequisitionOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamRequisitionOrder eamRequisitionOrder) {
        return ControllerUtil.returnCRUD(eamRequisitionOrderService.save(eamRequisitionOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamRequisitionOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamRequisitionOrder.update.class) EamRequisitionOrder eamRequisitionOrder) {
        return ControllerUtil.returnCRUD(eamRequisitionOrderService.update(eamRequisitionOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamRequisitionOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamRequisitionOrder  eamRequisitionOrder = eamRequisitionOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamRequisitionOrder,StringUtils.isEmpty(eamRequisitionOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamRequisitionOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamRequisitionOrder searchEamRequisitionOrder) {
        Page<Object> page = PageHelper.startPage(searchEamRequisitionOrder.getStartPage(),searchEamRequisitionOrder.getPageSize());
        List<EamRequisitionOrderDto> list = eamRequisitionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamRequisitionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtRequisitionOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamRequisitionOrder searchEamRequisitionOrder) {
        Page<Object> page = PageHelper.startPage(searchEamRequisitionOrder.getStartPage(),searchEamRequisitionOrder.getPageSize());
        List<EamHtRequisitionOrder> list = eamHtRequisitionOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamRequisitionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamRequisitionOrder searchEamRequisitionOrder){
    List<EamRequisitionOrderDto> list = eamRequisitionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamRequisitionOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备领用", EamRequisitionOrderDto.class, "设备领用.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
