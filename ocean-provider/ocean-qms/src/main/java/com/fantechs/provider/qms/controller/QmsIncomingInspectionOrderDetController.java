package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIncomingInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */
@RestController
@Api(tags = "来料检验单明细")
@RequestMapping("/qmsIncomingInspectionOrderDet")
@Validated
@Slf4j
public class QmsIncomingInspectionOrderDetController {

    @Resource
    private QmsIncomingInspectionOrderDetService qmsIncomingInspectionOrderDetService;

    @ApiOperation("获取来料检验单明细信息")
    @PostMapping("/showOrderDet")
    public ResponseEntity<List<QmsIncomingInspectionOrderDet>> showOrderDet(@ApiParam(value = "检验标准ID",required = true)@RequestParam  @NotNull(message="检验标准ID不能为空") Long inspectionStandardId,
                                                                               @ApiParam(value = "总数量",required = true)@RequestParam  @NotNull(message="总数量不能为空") BigDecimal qty) {
        List<QmsIncomingInspectionOrderDet> list = qmsIncomingInspectionOrderDetService.showOrderDet(inspectionStandardId,qty);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderDetService.save(qmsIncomingInspectionOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsIncomingInspectionOrderDet.update.class) QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderDetService.update(qmsIncomingInspectionOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsIncomingInspectionOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsIncomingInspectionOrderDet  qmsIncomingInspectionOrderDet = qmsIncomingInspectionOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsIncomingInspectionOrderDet,StringUtils.isEmpty(qmsIncomingInspectionOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsIncomingInspectionOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIncomingInspectionOrderDet searchQmsIncomingInspectionOrderDet) {
        Page<Object> page = PageHelper.startPage(searchQmsIncomingInspectionOrderDet.getStartPage(),searchQmsIncomingInspectionOrderDet.getPageSize());
        List<QmsIncomingInspectionOrderDetDto> list = qmsIncomingInspectionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<QmsIncomingInspectionOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchQmsIncomingInspectionOrderDet searchQmsIncomingInspectionOrderDet) {
        List<QmsIncomingInspectionOrderDetDto> list = qmsIncomingInspectionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrderDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsHtIncomingInspectionOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIncomingInspectionOrderDet searchQmsIncomingInspectionOrderDet) {
        Page<Object> page = PageHelper.startPage(searchQmsIncomingInspectionOrderDet.getStartPage(),searchQmsIncomingInspectionOrderDet.getPageSize());
        List<QmsHtIncomingInspectionOrderDet> list = qmsIncomingInspectionOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsIncomingInspectionOrderDet searchQmsIncomingInspectionOrderDet){
    List<QmsIncomingInspectionOrderDetDto> list = qmsIncomingInspectionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "来料检验单明细", QmsIncomingInspectionOrderDetDto.class, "来料检验单明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
