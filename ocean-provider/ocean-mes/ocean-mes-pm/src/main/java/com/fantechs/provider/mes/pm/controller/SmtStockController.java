package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.dto.apply.SmtStockDto;
import com.fantechs.common.base.entity.apply.SmtStock;
import com.fantechs.common.base.entity.apply.search.SearchSmtStock;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.SmtStockService;
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
@Api(tags = "备料信息")
@RequestMapping("/smtStock")
@Validated
public class SmtStockController {

    @Autowired
    private SmtStockService smtStockService;

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= SmtStock.update.class) SmtStock smtStock) {
        return ControllerUtil.returnCRUD(smtStockService.update(smtStock));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtStock> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtStock  smtStock = smtStockService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtStock,StringUtils.isEmpty(smtStock)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtStockDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStock searchSmtStock) {
        Page<Object> page = PageHelper.startPage(searchSmtStock.getStartPage(),searchSmtStock.getPageSize());
        List<SmtStockDto> list = smtStockService.findList(searchSmtStock);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export",produces = "application/octet-stream")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtStock searchSmtStock){
    List<SmtStockDto> list = smtStockService.findList(searchSmtStock);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtStock信息", SmtStockDto.class, "SmtStock.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
