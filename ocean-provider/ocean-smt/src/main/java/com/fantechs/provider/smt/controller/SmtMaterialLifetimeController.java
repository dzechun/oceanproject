package com.fantechs.provider.smt.controller;

import com.fantechs.common.base.general.dto.smt.SmtMaterialLifetimeDto;
import com.fantechs.common.base.general.entity.smt.SmtMaterialLifetime;
import com.fantechs.common.base.general.entity.smt.history.SmtHtMaterialLifetime;
import com.fantechs.common.base.general.entity.smt.search.SearchSmtMaterialLifetime;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.smt.service.SmtHtMaterialLifetimeService;
import com.fantechs.provider.smt.service.SmtMaterialLifetimeService;
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
 * Created by leifengzhi on 2021/07/20.
 */
@RestController
@Api(tags = "MSD材料寿命定义")
@RequestMapping("/smtMaterialLifetime")
@Validated
public class SmtMaterialLifetimeController {

    @Resource
    private SmtMaterialLifetimeService smtMaterialLifetimeService;
    @Resource
    private SmtHtMaterialLifetimeService smtHtMaterialLifetimeService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtMaterialLifetime smtMaterialLifetime) {
        return ControllerUtil.returnCRUD(smtMaterialLifetimeService.save(smtMaterialLifetime));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtMaterialLifetimeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtMaterialLifetime.update.class) SmtMaterialLifetime smtMaterialLifetime) {
        return ControllerUtil.returnCRUD(smtMaterialLifetimeService.update(smtMaterialLifetime));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtMaterialLifetime> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtMaterialLifetime  smtMaterialLifetime = smtMaterialLifetimeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtMaterialLifetime,StringUtils.isEmpty(smtMaterialLifetime)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtMaterialLifetimeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtMaterialLifetime searchSmtMaterialLifetime) {
        Page<Object> page = PageHelper.startPage(searchSmtMaterialLifetime.getStartPage(),searchSmtMaterialLifetime.getPageSize());
        List<SmtMaterialLifetimeDto> list = smtMaterialLifetimeService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtMaterialLifetime));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtMaterialLifetime>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtMaterialLifetime searchSmtMaterialLifetime) {
        Page<Object> page = PageHelper.startPage(searchSmtMaterialLifetime.getStartPage(),searchSmtMaterialLifetime.getPageSize());
        List<SmtHtMaterialLifetime> list = smtHtMaterialLifetimeService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtMaterialLifetime));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtMaterialLifetime searchSmtMaterialLifetime){
        List<SmtMaterialLifetimeDto> list = smtMaterialLifetimeService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtMaterialLifetime));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "MSD材料寿命定义", "MSD材料寿命定义.xls", response);
    }
}
