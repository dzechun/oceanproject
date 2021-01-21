package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.SmtStockDetDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtStockDet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtStockDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.SmtStockDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/24
*/
@RestController
@Api(tags = "备料明细")
@RequestMapping("/smtStockDet")
@Validated
public class SmtStockDetController {

    @Autowired
    private SmtStockDetService smtStockDetService;

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated SmtStockDet smtStockDet) {
        return ControllerUtil.returnCRUD(smtStockDetService.update(smtStockDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtStockDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtStockDet  smtStockDet = smtStockDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtStockDet,StringUtils.isEmpty(smtStockDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtStockDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStockDet searchSmtStockDet) {
        Page<Object> page = PageHelper.startPage(searchSmtStockDet.getStartPage(),searchSmtStockDet.getPageSize());
        List<SmtStockDetDto> list = smtStockDetService.findList(searchSmtStockDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export",produces = "application/octet-stream")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtStockDet searchSmtStockDet){
    List<SmtStockDetDto> list = smtStockDetService.findList(searchSmtStockDet);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtStockDet信息", SmtStockDetDto.class, "SmtStockDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
