package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardPool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderCardPoolService;
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
@Api(tags = "工单流转卡任务池")
@RequestMapping("/smtWorkOrderCardPool")
@Validated
public class SmtWorkOrderCardPoolController {

    @Autowired
    private SmtWorkOrderCardPoolService smtWorkOrderCardPoolService;

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkOrderCardPool> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtWorkOrderCardPool  smtWorkOrderCardPool = smtWorkOrderCardPoolService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWorkOrderCardPool,StringUtils.isEmpty(smtWorkOrderCardPool)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderCardPoolDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderCardPool.getStartPage(),searchSmtWorkOrderCardPool.getPageSize());
        List<SmtWorkOrderCardPoolDto> list = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool){
        List<SmtWorkOrderCardPoolDto> list = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "工单流转卡任务池信息", SmtWorkOrderCardPoolDto.class, " SmtWorkOrderCardPool.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
