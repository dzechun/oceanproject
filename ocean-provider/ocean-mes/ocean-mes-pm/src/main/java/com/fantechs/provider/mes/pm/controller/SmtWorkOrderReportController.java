package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderReportDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderReport;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderReport;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderReportService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/21
*/
@RestController
@Api(tags = "工单报工")
@RequestMapping("/smtWorkOrderReport")
@Validated
public class SmtWorkOrderReportController {

    @Autowired
    private SmtWorkOrderReportService smtWorkOrderReportService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtWorkOrderReport smtWorkOrderReport) {
        return ControllerUtil.returnCRUD(smtWorkOrderReportService.save(smtWorkOrderReport));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtWorkOrderReportService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtWorkOrderReport.update.class) SmtWorkOrderReport smtWorkOrderReport) {
        return ControllerUtil.returnCRUD(smtWorkOrderReportService.update(smtWorkOrderReport));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderReportDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderReport searchSmtWorkOrderReport) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderReport.getStartPage(),searchSmtWorkOrderReport.getPageSize());
        List<SmtWorkOrderReportDto> list = smtWorkOrderReportService.findList(searchSmtWorkOrderReport);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchSmtWorkOrderReport searchSmtWorkOrderReport){
//    List<SmtWorkOrderReport> list = smtWorkOrderReportService.findList(searchSmtWorkOrderReport);
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "SmtWorkOrderReport信息", SmtWorkOrderReport.class, "SmtWorkOrderReport.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }
}
