package com.fantechs.auth.controller;

import com.fantechs.auth.service.SysImportAndExportLogService;
import com.fantechs.common.base.dto.security.SysImportAndExportLogDto;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.search.SearchSysImportAndExportLog;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/07.
 */
@RestController
@Api(tags = "导入日志信息表")
@RequestMapping("/sysImportAndExportLog")
@Validated
public class SysImportAndExportLogController {

    @Resource
    private SysImportAndExportLogService sysImportAndExportLogService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SysImportAndExportLog sysImportAndExportLog) {
        return ControllerUtil.returnCRUD(sysImportAndExportLogService.save(sysImportAndExportLog));
    }

/*    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysImportAndExportLogService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysImportAndExportLog.update.class) SysImportAndExportLog sysImportAndExportLog) {
        return ControllerUtil.returnCRUD(sysImportAndExportLogService.update(sysImportAndExportLog));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SysImportAndExportLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SysImportAndExportLog  sysImportAndExportLog = sysImportAndExportLogService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(sysImportAndExportLog,StringUtils.isEmpty(sysImportAndExportLog)?0:1);
    }*/

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysImportAndExportLogDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSysImportAndExportLog searchSysImportAndExportLog) {
        Page<Object> page = PageHelper.startPage(searchSysImportAndExportLog.getStartPage(),searchSysImportAndExportLog.getPageSize());
        List<SysImportAndExportLogDto> list = sysImportAndExportLogService.findList(ControllerUtil.dynamicConditionByEntity(searchSysImportAndExportLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SysImportAndExportLogDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSysImportAndExportLog searchSysImportAndExportLog) {
        List<SysImportAndExportLogDto> list = sysImportAndExportLogService.findList(ControllerUtil.dynamicConditionByEntity(searchSysImportAndExportLog));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

}
