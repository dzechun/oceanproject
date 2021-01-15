package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardCollocationDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderCardCollocationService;
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
 * Created by wcz on 2020/11/20.
 */
@RestController
@Api(tags = "工单流转卡配置")
@RequestMapping("/smtWorkOrderCardCollocation")
@Validated
public class SmtWorkOrderCardCollocationController {

    @Autowired
    private SmtWorkOrderCardCollocationService smtWorkOrderCardCollocationService;

    /**
     * 产生工单流转卡
     * @param smtWorkOrderCardCollocation
     * @return
     */
    @ApiOperation(value = "产生工单流转卡",notes = "产生工单流转卡")
    @PostMapping("/generateWorkOrderCardCollocation")
    public ResponseEntity generateWorkOrderCardCollocation(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtWorkOrderCardCollocation smtWorkOrderCardCollocation) {
        return ControllerUtil.returnCRUD(smtWorkOrderCardCollocationService.save(smtWorkOrderCardCollocation));
    }


    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkOrderCardCollocation> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtWorkOrderCardCollocation  smtWorkOrderCardCollocation = smtWorkOrderCardCollocationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWorkOrderCardCollocation,StringUtils.isEmpty(smtWorkOrderCardCollocation)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderCardCollocationDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderCardCollocation.getStartPage(),searchSmtWorkOrderCardCollocation.getPageSize());
        List<SmtWorkOrderCardCollocationDto> list = smtWorkOrderCardCollocationService.findList(searchSmtWorkOrderCardCollocation);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation){
        List<SmtWorkOrderCardCollocationDto> list = smtWorkOrderCardCollocationService.findList(searchSmtWorkOrderCardCollocation);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "工单流转卡信息", SmtWorkOrderCardCollocationDto.class, "SmtWorkOrderCardCollocation.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
