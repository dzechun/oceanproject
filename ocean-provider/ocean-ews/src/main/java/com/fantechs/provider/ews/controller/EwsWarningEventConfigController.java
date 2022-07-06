package com.fantechs.provider.ews.controller;

import com.fantechs.common.base.general.dto.ews.EwsWarningEventConfigDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventConfig;
import com.fantechs.common.base.general.entity.ews.search.SearchEwsWarningEventConfig;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.service.EwsWarningEventConfigService;
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
@Api(tags = "事件配置")
@RequestMapping("/ewsWarningEventConfig")
@Validated
public class EwsWarningEventConfigController {

    @Resource
    private EwsWarningEventConfigService ewsWarningEventConfigService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EwsWarningEventConfig ewsWarningEventConfig) {
        return ControllerUtil.returnCRUD(ewsWarningEventConfigService.save(ewsWarningEventConfig));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ewsWarningEventConfigService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EwsWarningEventConfig.update.class) EwsWarningEventConfig ewsWarningEventConfig) {
        return ControllerUtil.returnCRUD(ewsWarningEventConfigService.update(ewsWarningEventConfig));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EwsWarningEventConfig> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EwsWarningEventConfig  ewsWarningEventConfig = ewsWarningEventConfigService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(ewsWarningEventConfig,StringUtils.isEmpty(ewsWarningEventConfig)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EwsWarningEventConfigDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEwsWarningEventConfig searchEwsWarningEventConfig) {
        Page<Object> page = PageHelper.startPage(searchEwsWarningEventConfig.getStartPage(),searchEwsWarningEventConfig.getPageSize());
        List<EwsWarningEventConfigDto> list = ewsWarningEventConfigService.findList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningEventConfig));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("执行状态开始")
    @GetMapping("/start")
    public ResponseEntity start(@ApiParam(value = "对象，Id必传",required = true)@RequestParam Long Id) {
        return ControllerUtil.returnCRUD(ewsWarningEventConfigService.start(Id));
    }

    @ApiOperation("执行状态关闭")
    @GetMapping("/stop")
    public ResponseEntity stop(@ApiParam(value = "对象，Id必传",required = true)@RequestParam Long Id) {
        return ControllerUtil.returnCRUD(ewsWarningEventConfigService.stop(Id));
    }

    @ApiOperation("立即执行")
    @GetMapping("/immediately")
    public ResponseEntity immediately(@ApiParam(value = "对象，Id必传",required = true)@RequestParam Long Id) {
        return ControllerUtil.returnCRUD(ewsWarningEventConfigService.immediately(Id));
    }
}