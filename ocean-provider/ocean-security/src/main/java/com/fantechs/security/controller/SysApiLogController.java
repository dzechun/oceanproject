package com.fantechs.security.controller;

import com.fantechs.common.base.dto.security.SysApiLogDto;
import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysApiLog;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysApiLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/09.
 */
@RestController
@Api(tags = "接口日志控制器")
@RequestMapping("/sysApiLog")
@Validated
public class SysApiLogController {

    @Resource
    private SysApiLogService sysApiLogService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody SysApiLog sysApiLog) {
        return ControllerUtil.returnCRUD(sysApiLogService.save(sysApiLog));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody List<SysApiLog> logList) {
        return ControllerUtil.returnCRUD(sysApiLogService.batchAdd(logList));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysApiLogService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysApiLog.update.class) SysApiLog sysApiLog) {
        return ControllerUtil.returnCRUD(sysApiLogService.update(sysApiLog));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SysApiLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SysApiLog  sysApiLog = sysApiLogService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(sysApiLog,StringUtils.isEmpty(sysApiLog)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysApiLogDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSysApiLog searchSysApiLog) {
        Page<Object> page = PageHelper.startPage(searchSysApiLog.getStartPage(),searchSysApiLog.getPageSize());
        List<SysApiLogDto> list = sysApiLogService.findList(ControllerUtil.dynamicConditionByEntity(searchSysApiLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSysApiLog searchSysApiLog){
    List<SysApiLogDto> list = sysApiLogService.findList(ControllerUtil.dynamicConditionByEntity(searchSysApiLog));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SysApiLog信息", SysApiLogDto.class, "SysApiLog.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
