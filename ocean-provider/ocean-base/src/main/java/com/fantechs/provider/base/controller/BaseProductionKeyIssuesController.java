package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.entity.basic.BaseProductionKeyIssues;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductionKeyIssues;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductionKeyIssues;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtProductionKeyIssuesService;
import com.fantechs.provider.base.service.BaseProductionKeyIssuesService;
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
 * Created by leifengzhi on 2021/06/10.
 */
@RestController
@Api(tags = "产品关键事项维护")
@RequestMapping("/baseProductionKeyIssues")
@Validated
public class BaseProductionKeyIssuesController {

    @Resource
    private BaseProductionKeyIssuesService baseProductionKeyIssuesService;
    @Resource
    private BaseHtProductionKeyIssuesService baseHtProductionKeyIssuesService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseProductionKeyIssues baseProductionKeyIssues) {
        return ControllerUtil.returnCRUD(baseProductionKeyIssuesService.save(baseProductionKeyIssues));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProductionKeyIssuesService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseProductionKeyIssues.update.class) BaseProductionKeyIssues baseProductionKeyIssues) {
        return ControllerUtil.returnCRUD(baseProductionKeyIssuesService.update(baseProductionKeyIssues));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProductionKeyIssues> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseProductionKeyIssues  baseProductionKeyIssues = baseProductionKeyIssuesService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProductionKeyIssues,StringUtils.isEmpty(baseProductionKeyIssues)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProductionKeyIssues>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductionKeyIssues searchBaseProductionKeyIssues) {
        Page<Object> page = PageHelper.startPage(searchBaseProductionKeyIssues.getStartPage(),searchBaseProductionKeyIssues.getPageSize());
        List<BaseProductionKeyIssues> list = baseProductionKeyIssuesService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductionKeyIssues));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProductionKeyIssues>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductionKeyIssues searchBaseProductionKeyIssues) {
        Page<Object> page = PageHelper.startPage(searchBaseProductionKeyIssues.getStartPage(),searchBaseProductionKeyIssues.getPageSize());
        List<BaseHtProductionKeyIssues> list = baseHtProductionKeyIssuesService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProductionKeyIssues));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseProductionKeyIssues searchBaseProductionKeyIssues){
        List<BaseProductionKeyIssues> list = baseProductionKeyIssuesService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductionKeyIssues));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "产品关键事项维护", "产品关键事项维护.xls", response);
    }
}
