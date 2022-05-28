package com.fantechs.auth.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.security.SysConfigurationDto;
import com.fantechs.common.base.general.dto.security.SysFieldDto;
import com.fantechs.common.base.general.dto.security.SysTableDto;
import com.fantechs.common.base.general.entity.security.SysConfiguration;
import com.fantechs.common.base.general.entity.security.search.SearchSysConfiguration;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.auth.service.SysConfigurationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/09.
 */
@RestController
@Api(tags = "业务配置控制器")
@RequestMapping("/sysConfiguration")
@Validated
public class SysConfigurationController {

    @Resource
    private SysConfigurationService sysConfigurationService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SysConfiguration sysConfiguration) {
        return ControllerUtil.returnCRUD(sysConfigurationService.save(sysConfiguration));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysConfigurationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysConfiguration.update.class) SysConfiguration sysConfiguration) {
        return ControllerUtil.returnCRUD(sysConfigurationService.update(sysConfiguration));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SysConfiguration> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SysConfiguration  sysConfiguration = sysConfigurationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(sysConfiguration,StringUtils.isEmpty(sysConfiguration)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysConfigurationDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSysConfiguration searchSysConfiguration) {
        Page<Object> page = PageHelper.startPage(searchSysConfiguration.getStartPage(),searchSysConfiguration.getPageSize());
        List<SysConfigurationDto> list = sysConfigurationService.findList(ControllerUtil.dynamicConditionByEntity(searchSysConfiguration));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SysConfigurationDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSysConfiguration searchSysConfiguration) {
        List<SysConfigurationDto> list = sysConfigurationService.findList(ControllerUtil.dynamicConditionByEntity(searchSysConfiguration));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSysConfiguration searchSysConfiguration){
    List<SysConfigurationDto> list = sysConfigurationService.findList(ControllerUtil.dynamicConditionByEntity(searchSysConfiguration));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SysConfiguration信息", SysConfigurationDto.class, "SysConfiguration.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<SysConfiguration> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, SysConfiguration.class);
            Map<String, Object> resultMap = sysConfigurationService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation(value = "查询库中所有对象",notes = "查询库中所有对象")
    @PostMapping("/findTableList")
    public ResponseEntity findTableList(@ApiParam(value = "tableName",required = false)@RequestParam String tableName) {
        List<SysTableDto> list = sysConfigurationService.findTablesByName(StringUtils.isEmpty(tableName)?null:tableName);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation(value = "查询对象所有字段",notes = "查询对象所有字段")
    @PostMapping("/findFieldList")
    public ResponseEntity findFieldList(@ApiParam(value = "tableName",required = true)@RequestParam String tableName) {
        List<SysFieldDto> list = sysConfigurationService.findFieldList(tableName);
        return  ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation(value = "单据下推",notes = "单据下推")
    @PostMapping("/push")
    public ResponseEntity push(@ApiParam(value = "ID",required = true)@RequestParam Long id) {
        return ControllerUtil.returnCRUD(sysConfigurationService.push(id));
    }
}
