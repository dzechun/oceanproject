package com.fantechs.provider.ews.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.ews.EwsHtWarningUserInfoDto;
import com.fantechs.common.base.general.dto.ews.EwsWarningUserInfoDto;
import com.fantechs.common.base.general.dto.ews.imports.EwsWarningUserInfoImport;
import com.fantechs.common.base.general.entity.ews.EwsWarningUserInfo;
import com.fantechs.common.base.general.entity.ews.search.SearchEwsWarningUserInfo;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.ews.service.EwsWarningUserInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
 * Created by mr.lei on 2021/12/27.
 */
@RestController
@Api(tags = "预警人员")
@RequestMapping("/ewsWarningUserInfo")
@Validated
@Slf4j
public class EwsWarningUserInfoController {

    @Resource
    private EwsWarningUserInfoService ewsWarningUserInfoService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EwsWarningUserInfo ewsWarningUserInfo) {
        return ControllerUtil.returnCRUD(ewsWarningUserInfoService.save(ewsWarningUserInfo));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ewsWarningUserInfoService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EwsWarningUserInfo.update.class) EwsWarningUserInfo ewsWarningUserInfo) {
        return ControllerUtil.returnCRUD(ewsWarningUserInfoService.update(ewsWarningUserInfo));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EwsWarningUserInfo> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EwsWarningUserInfo  ewsWarningUserInfo = ewsWarningUserInfoService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(ewsWarningUserInfo,StringUtils.isEmpty(ewsWarningUserInfo)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EwsWarningUserInfoDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEwsWarningUserInfo searchEwsWarningUserInfo) {
        Page<Object> page = PageHelper.startPage(searchEwsWarningUserInfo.getStartPage(),searchEwsWarningUserInfo.getPageSize());
        List<EwsWarningUserInfoDto> list = ewsWarningUserInfoService.findList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningUserInfo));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EwsHtWarningUserInfoDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEwsWarningUserInfo searchEwsWarningUserInfo) {
        Page<Object> page = PageHelper.startPage(searchEwsWarningUserInfo.getStartPage(),searchEwsWarningUserInfo.getPageSize());
        List<EwsHtWarningUserInfoDto> list = ewsWarningUserInfoService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningUserInfo));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEwsWarningUserInfo searchEwsWarningUserInfo){
    List<EwsWarningUserInfoDto> list = ewsWarningUserInfoService.findList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningUserInfo));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "预警人员", "预警人员", "预警人员.xls", response);
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<EwsWarningUserInfoImport> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, EwsWarningUserInfoImport.class);
            Map<String, Object> resultMap = ewsWarningUserInfoService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
