package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsIpqcInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderDetService;
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
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/04.
 */
@RestController
@Api(tags = "IPQC检验单明细")
@RequestMapping("/qmsIpqcInspectionOrderDet")
@Validated
public class QmsIpqcInspectionOrderDetController {

    @Resource
    private QmsIpqcInspectionOrderDetService qmsIpqcInspectionOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderDetService.save(qmsIpqcInspectionOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsIpqcInspectionOrderDet.update.class) QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderDetService.update(qmsIpqcInspectionOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsIpqcInspectionOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsIpqcInspectionOrderDet  qmsIpqcInspectionOrderDet = qmsIpqcInspectionOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsIpqcInspectionOrderDet,StringUtils.isEmpty(qmsIpqcInspectionOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsIpqcInspectionOrderDet>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrderDet searchQmsIpqcInspectionOrderDet) {
        Page<Object> page = PageHelper.startPage(searchQmsIpqcInspectionOrderDet.getStartPage(),searchQmsIpqcInspectionOrderDet.getPageSize());
        List<QmsIpqcInspectionOrderDet> list = qmsIpqcInspectionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("展示单据明细")
    @PostMapping("/showOrderDet")
    public ResponseEntity<List<QmsIpqcInspectionOrderDetDto>> showOrderDet(@ApiParam(value = "检验标准id",required = true)@RequestParam  @NotNull(message="检验标准id不能为空")Long inspectionStandardId,
                                                                           @ApiParam(value = "数量",required = true)@RequestParam  @NotNull(message="数量不能为空") BigDecimal qty) {
        List<QmsIpqcInspectionOrderDetDto> list = qmsIpqcInspectionOrderDetService.showOrderDet(inspectionStandardId,qty);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

   /* @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsIpqcInspectionOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrderDet searchQmsIpqcInspectionOrderDet) {
        Page<Object> page = PageHelper.startPage(searchQmsIpqcInspectionOrderDet.getStartPage(),searchQmsIpqcInspectionOrderDet.getPageSize());
        List<QmsIpqcInspectionOrderDet> list = qmsIpqcInspectionOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsIpqcInspectionOrderDet searchQmsIpqcInspectionOrderDet){
    List<QmsIpqcInspectionOrderDet> list = qmsIpqcInspectionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "IPQC检验单明细", QmsIpqcInspectionOrderDet.class, "IPQC检验单明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
