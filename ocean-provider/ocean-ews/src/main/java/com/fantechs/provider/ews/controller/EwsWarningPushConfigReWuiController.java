package com.fantechs.provider.ews.controller;

import com.fantechs.common.base.general.dto.ews.EwsWarningPushConfigReWuiDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfigReWui;
import com.fantechs.common.base.general.entity.ews.search.SearchEwsWarningPushConfigReWui;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.service.EwsWarningPushConfigReWuiService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by mr.lei on 2021/12/27.
 */
@RestController
@Api(tags = "事件推送配置明细")
@RequestMapping("/ewsWarningPushConfigReWui")
@Validated
public class EwsWarningPushConfigReWuiController {

    @Resource
    private EwsWarningPushConfigReWuiService ewsWarningPushConfigReWuiService;

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EwsWarningPushConfigReWui> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EwsWarningPushConfigReWui  ewsWarningPushConfigReWui = ewsWarningPushConfigReWuiService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(ewsWarningPushConfigReWui,StringUtils.isEmpty(ewsWarningPushConfigReWui)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EwsWarningPushConfigReWuiDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEwsWarningPushConfigReWui searchEwsWarningPushConfigReWui) {
        Page<Object> page = PageHelper.startPage(searchEwsWarningPushConfigReWui.getStartPage(),searchEwsWarningPushConfigReWui.getPageSize());
        List<EwsWarningPushConfigReWuiDto> list = ewsWarningPushConfigReWuiService.findList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningPushConfigReWui));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
