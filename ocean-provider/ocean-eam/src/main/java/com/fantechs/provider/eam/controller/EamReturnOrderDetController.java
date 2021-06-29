package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamReturnOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamReturnOrderDet;
import com.fantechs.common.base.general.entity.eam.history.EamHtReturnOrderDet;
import com.fantechs.common.base.general.entity.eam.search.SearchEamReturnOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtReturnOrderDetService;
import com.fantechs.provider.eam.service.EamReturnOrderDetService;
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
@Api(tags = "设备归还单明细")
@RequestMapping("/eamReturnOrderDet")
@Validated
public class EamReturnOrderDetController {

    @Resource
    private EamReturnOrderDetService eamReturnOrderDetService;
    @Resource
    private EamHtReturnOrderDetService eamHtReturnOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamReturnOrderDet eamReturnOrderDet) {
        return ControllerUtil.returnCRUD(eamReturnOrderDetService.save(eamReturnOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamReturnOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamReturnOrderDet.update.class) EamReturnOrderDet eamReturnOrderDet) {
        return ControllerUtil.returnCRUD(eamReturnOrderDetService.update(eamReturnOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamReturnOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamReturnOrderDet  eamReturnOrderDet = eamReturnOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamReturnOrderDet,StringUtils.isEmpty(eamReturnOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamReturnOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamReturnOrderDet searchEamReturnOrderDet) {
        Page<Object> page = PageHelper.startPage(searchEamReturnOrderDet.getStartPage(),searchEamReturnOrderDet.getPageSize());
        List<EamReturnOrderDetDto> list = eamReturnOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEamReturnOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtReturnOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamReturnOrderDet searchEamReturnOrderDet) {
        Page<Object> page = PageHelper.startPage(searchEamReturnOrderDet.getStartPage(),searchEamReturnOrderDet.getPageSize());
        List<EamHtReturnOrderDet> list = eamHtReturnOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamReturnOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamReturnOrderDet searchEamReturnOrderDet){
    List<EamReturnOrderDetDto> list = eamReturnOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEamReturnOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备归还单明细", EamReturnOrderDetDto.class, "设备归还单明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
