package com.fantechs.provider.om.controller.report;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtOrder;
import com.fantechs.common.base.general.dto.om.SearchSmtOrderReportDto;
import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.dto.om.SmtOrderReportDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.provider.om.service.SmtOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import java.util.List;

/**
 * @author Mr.Lei
 * @create 2021/3/25
 */
@RestController
@Api(tags = "订单报表")
@RequestMapping("/smtOrder/report")
@Validated
public class SmtOrderRepoortController {
    @Autowired
    private SmtOrderService smtOrderService;

    @ApiOperation("销售情况报表")
    @PostMapping("/orderReport")
    public ResponseEntity<List<SmtOrderReportDto>> orderReport(@RequestBody(required = false) SearchSmtOrderReportDto searchSmtOrderReportDto){
        Page<Object> page = PageHelper.startPage(searchSmtOrderReportDto.getStartPage(),searchSmtOrderReportDto.getPageSize());
        List<SmtOrderReportDto> list = smtOrderService.orderReport(searchSmtOrderReportDto);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtOrderReportDto searchSmtOrderReportDto){
        List<SmtOrderReportDto> list = smtOrderService.orderReport(searchSmtOrderReportDto);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "销售情况报表", SmtOrderReportDto.class, "销售情况报表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
