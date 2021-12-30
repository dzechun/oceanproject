package com.fantechs.provider.ews.controller;

import com.fantechs.common.base.general.dto.ews.EwsWarningEventExecutePushLogDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecutePushLog;
import com.fantechs.common.base.general.entity.ews.search.SearchEwsWarningEventExecutePushLog;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.service.EwsWarningEventExecutePushLogService;
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
 * Created by mr.lei on 2021/12/28.
 */
@RestController
@Api(tags = "预警推送日志处理结果")
@RequestMapping("/ewsWarningEventExecutePushLog")
@Validated
public class EwsWarningEventExecutePushLogController {

    @Resource
    private EwsWarningEventExecutePushLogService ewsWarningEventExecutePushLogService;

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ewsWarningEventExecutePushLogService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EwsWarningEventExecutePushLog.update.class) EwsWarningEventExecutePushLog ewsWarningEventExecutePushLog) {
        return ControllerUtil.returnCRUD(ewsWarningEventExecutePushLogService.update(ewsWarningEventExecutePushLog));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EwsWarningEventExecutePushLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EwsWarningEventExecutePushLog  ewsWarningEventExecutePushLog = ewsWarningEventExecutePushLogService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(ewsWarningEventExecutePushLog,StringUtils.isEmpty(ewsWarningEventExecutePushLog)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EwsWarningEventExecutePushLogDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEwsWarningEventExecutePushLog searchEwsWarningEventExecutePushLog) {
        Page<Object> page = PageHelper.startPage(searchEwsWarningEventExecutePushLog.getStartPage(),searchEwsWarningEventExecutePushLog.getPageSize());
        List<EwsWarningEventExecutePushLogDto> list = ewsWarningEventExecutePushLogService.findList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningEventExecutePushLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<EwsWarningEventExecutePushLogDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchEwsWarningEventExecutePushLog searchEwsWarningEventExecutePushLog) {
        List<EwsWarningEventExecutePushLogDto> list = ewsWarningEventExecutePushLogService.findList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningEventExecutePushLog));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }
}
