package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.dto.apply.SmtStockDetDto;
import com.fantechs.common.base.entity.apply.SmtStockDet;
import com.fantechs.common.base.entity.apply.search.SearchSmtStockDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtStockDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
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

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtStockDet searchSmtStockDet){
    List<SmtStockDetDto> list = smtStockDetService.findList(searchSmtStockDet);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtStockDet信息", SmtStockDet.class, "SmtStockDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
