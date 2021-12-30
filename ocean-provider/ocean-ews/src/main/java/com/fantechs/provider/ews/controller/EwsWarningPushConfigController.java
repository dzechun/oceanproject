package com.fantechs.provider.ews.controller;

import com.fantechs.common.base.general.dto.ews.EwsHtWarningPushConfigDto;
import com.fantechs.common.base.general.dto.ews.EwsWarningPushConfigDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfig;
import com.fantechs.common.base.general.entity.ews.search.SearchEwsWarningPushConfig;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.service.EwsWarningPushConfigService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by mr.lei on 2021/12/27.
 */
@RestController
@Api(tags = "事件推送配置")
@RequestMapping("/ewsWarningPushConfig")
@Validated
public class EwsWarningPushConfigController {

    @Resource
    private EwsWarningPushConfigService ewsWarningPushConfigService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EwsWarningPushConfig ewsWarningPushConfig) {
        return ControllerUtil.returnCRUD(ewsWarningPushConfigService.save(ewsWarningPushConfig));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ewsWarningPushConfigService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EwsWarningPushConfig.update.class) EwsWarningPushConfig ewsWarningPushConfig) {
        return ControllerUtil.returnCRUD(ewsWarningPushConfigService.update(ewsWarningPushConfig));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EwsWarningPushConfig> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EwsWarningPushConfig  ewsWarningPushConfig = ewsWarningPushConfigService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(ewsWarningPushConfig,StringUtils.isEmpty(ewsWarningPushConfig)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EwsWarningPushConfigDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEwsWarningPushConfig searchEwsWarningPushConfig) {
        Page<Object> page = PageHelper.startPage(searchEwsWarningPushConfig.getStartPage(),searchEwsWarningPushConfig.getPageSize());
        List<EwsWarningPushConfigDto> list = ewsWarningPushConfigService.findList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningPushConfig));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EwsHtWarningPushConfigDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEwsWarningPushConfig searchEwsWarningPushConfig) {
        Page<Object> page = PageHelper.startPage(searchEwsWarningPushConfig.getStartPage(),searchEwsWarningPushConfig.getPageSize());
        List<EwsHtWarningPushConfigDto> list = ewsWarningPushConfigService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningPushConfig));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
