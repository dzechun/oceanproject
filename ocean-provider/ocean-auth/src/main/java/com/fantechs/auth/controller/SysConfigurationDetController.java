package com.fantechs.auth.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.security.SysConfigurationDetDto;
import com.fantechs.common.base.general.entity.security.SysConfigurationDet;
import com.fantechs.common.base.general.entity.security.search.SearchSysConfigurationDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.auth.service.SysConfigurationDetService;
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
@Api(tags = "业务配置明细控制器")
@RequestMapping("/sysConfigurationDet")
@Validated
public class SysConfigurationDetController {

    @Resource
    private SysConfigurationDetService sysConfigurationDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SysConfigurationDet sysConfigurationDet) {
        return ControllerUtil.returnCRUD(sysConfigurationDetService.save(sysConfigurationDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysConfigurationDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysConfigurationDet.update.class) SysConfigurationDet sysConfigurationDet) {
        return ControllerUtil.returnCRUD(sysConfigurationDetService.update(sysConfigurationDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SysConfigurationDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SysConfigurationDet  sysConfigurationDet = sysConfigurationDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(sysConfigurationDet,StringUtils.isEmpty(sysConfigurationDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysConfigurationDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSysConfigurationDet searchSysConfigurationDet) {
        Page<Object> page = PageHelper.startPage(searchSysConfigurationDet.getStartPage(),searchSysConfigurationDet.getPageSize());
        List<SysConfigurationDetDto> list = sysConfigurationDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSysConfigurationDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SysConfigurationDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSysConfigurationDet searchSysConfigurationDet) {
        List<SysConfigurationDetDto> list = sysConfigurationDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSysConfigurationDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSysConfigurationDet searchSysConfigurationDet){
    List<SysConfigurationDetDto> list = sysConfigurationDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSysConfigurationDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SysConfigurationDet信息", SysConfigurationDetDto.class, "SysConfigurationDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<SysConfigurationDet> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, SysConfigurationDet.class);
            Map<String, Object> resultMap = sysConfigurationDetService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation("批量更新")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysConfigurationDet.update.class) List<SysConfigurationDet> sysConfigurationDets) {
        return ControllerUtil.returnCRUD(sysConfigurationDetService.batchUpdate(sysConfigurationDets));
    }

}
