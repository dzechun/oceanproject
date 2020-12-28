package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.QmsInspectionItemDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionItemDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsInspectionItemDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/25.
 */
@RestController
@Api(tags = "检验项目明细")
@RequestMapping("/qmsInspectionItemDet")
@Validated
public class QmsInspectionItemDetController {

    @Autowired
    private QmsInspectionItemDetService qmsInspectionItemDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsInspectionItemDet qmsInspectionItemDet) {
        return ControllerUtil.returnCRUD(qmsInspectionItemDetService.save(qmsInspectionItemDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsInspectionItemDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsInspectionItemDet.update.class) QmsInspectionItemDet qmsInspectionItemDet) {
        return ControllerUtil.returnCRUD(qmsInspectionItemDetService.update(qmsInspectionItemDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsInspectionItemDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsInspectionItemDet  qmsInspectionItemDet = qmsInspectionItemDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsInspectionItemDet,StringUtils.isEmpty(qmsInspectionItemDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsInspectionItemDet>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionItemDet searchQmsInspectionItemDet) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionItemDet.getStartPage(),searchQmsInspectionItemDet.getPageSize());
        List<QmsInspectionItemDet> list = qmsInspectionItemDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionItemDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsInspectionItemDet searchQmsInspectionItemDet){
    List<QmsInspectionItemDet> list = qmsInspectionItemDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionItemDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "QmsInspectionItemDet信息", QmsInspectionItemDet.class, "QmsInspectionItemDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
