package com.fantechs.provider.smt.controller;

import com.fantechs.common.base.general.dto.smt.SmtSolderPasteDto;
import com.fantechs.common.base.general.entity.smt.SmtSolderPaste;
import com.fantechs.common.base.general.entity.smt.search.SearchSmtSolderPaste;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.smt.service.SmtSolderPasteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/07/22.
 */
@RestController
@Api(tags = "锡膏管理")
@RequestMapping("/smtSolderPaste")
@Validated
public class SmtSolderPasteController {

    @Resource
    private SmtSolderPasteService smtSolderPasteService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtSolderPaste smtSolderPaste) {
        return ControllerUtil.returnCRUD(smtSolderPasteService.save(smtSolderPaste));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtSolderPasteService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtSolderPaste.update.class) SmtSolderPaste smtSolderPaste) {
        return ControllerUtil.returnCRUD(smtSolderPasteService.update(smtSolderPaste));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtSolderPaste> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtSolderPaste  smtSolderPaste = smtSolderPasteService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtSolderPaste,StringUtils.isEmpty(smtSolderPaste)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtSolderPasteDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtSolderPaste searchSmtSolderPaste) {
        Page<Object> page = PageHelper.startPage(searchSmtSolderPaste.getStartPage(),searchSmtSolderPaste.getPageSize());
        List<SmtSolderPasteDto> list = smtSolderPasteService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtSolderPaste));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<SmtSolderPaste>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtSolderPaste searchSmtSolderPaste) {
//        Page<Object> page = PageHelper.startPage(searchSmtSolderPaste.getStartPage(),searchSmtSolderPaste.getPageSize());
//        List<SmtSolderPaste> list = smtSolderPasteService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtSolderPaste));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtSolderPaste searchSmtSolderPaste){
    List<SmtSolderPasteDto> list = smtSolderPasteService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtSolderPaste));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "SmtSolderPaste信息", "SmtSolderPaste.xls", response);
    }
}
