package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsInspectionOrderDetService;
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
 * Created by leifengzhi on 2021/05/27.
 */
@RestController
@Api(tags = "成品检验单明细")
@RequestMapping("/qmsInspectionOrderDet")
@Validated
public class QmsInspectionOrderDetController {

    @Resource
    private QmsInspectionOrderDetService qmsInspectionOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsInspectionOrderDet qmsInspectionOrderDet) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderDetService.save(qmsInspectionOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsInspectionOrderDet.update.class) QmsInspectionOrderDet qmsInspectionOrderDet) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderDetService.update(qmsInspectionOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsInspectionOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsInspectionOrderDet  qmsInspectionOrderDet = qmsInspectionOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsInspectionOrderDet,StringUtils.isEmpty(qmsInspectionOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsInspectionOrderDet>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionOrderDet searchQmsInspectionOrderDet) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionOrderDet.getStartPage(),searchQmsInspectionOrderDet.getPageSize());
        List<QmsInspectionOrderDet> list = qmsInspectionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /*@ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsInspectionOrderDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionOrderDet searchQmsInspectionOrderDet) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionOrderDet.getStartPage(),searchQmsInspectionOrderDet.getPageSize());
        List<QmsInspectionOrderDet> list = qmsInspectionOrderDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsInspectionOrderDet searchQmsInspectionOrderDet){
    List<QmsInspectionOrderDet> list = qmsInspectionOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "成品检验单明细信息", QmsInspectionOrderDet.class, "成品检验单明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
