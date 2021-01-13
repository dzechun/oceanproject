package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.dto.apply.SmtWorkOrderBarcodePoolDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBarcodePool;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderBarcodePoolService;
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
 *
 * Created by wcz on 2020/11/25.
 */
@RestController
@Api(tags = "工单任务池表")
@RequestMapping("/smtWorkOrderBarcodePool")
@Validated
public class SmtWorkOrderBarcodePoolController {

    @Autowired
    private SmtWorkOrderBarcodePoolService smtWorkOrderBarcodePoolService;


    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkOrderBarcodePool> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtWorkOrderBarcodePool  smtWorkOrderBarcodePool = smtWorkOrderBarcodePoolService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWorkOrderBarcodePool,StringUtils.isEmpty(smtWorkOrderBarcodePool)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderBarcodePoolDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderBarcodePool.getStartPage(),searchSmtWorkOrderBarcodePool.getPageSize());
        List<SmtWorkOrderBarcodePoolDto> list = smtWorkOrderBarcodePoolService.findList(searchSmtWorkOrderBarcodePool);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool){
        List<SmtWorkOrderBarcodePoolDto> list = smtWorkOrderBarcodePoolService.findList(searchSmtWorkOrderBarcodePool);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "工单任务池信息", SmtWorkOrderBarcodePoolDto.class, "SmtWorkOrderBarcodePool.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
