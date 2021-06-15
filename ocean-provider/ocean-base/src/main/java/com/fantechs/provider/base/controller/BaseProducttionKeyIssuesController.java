package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseProducttionKeyIssues;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProducttionKeyIssues;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProducttionKeyIssues;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtProducttionKeyIssuesService;
import com.fantechs.provider.base.service.BaseProducttionKeyIssuesService;
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

/**
 *
 * Created by leifengzhi on 2021/06/10.
 */
@RestController
@Api(tags = "产品关键事项维护")
@RequestMapping("/baseProducttionKeyIssues")
@Validated
public class BaseProducttionKeyIssuesController {

    @Resource
    private BaseProducttionKeyIssuesService baseProducttionKeyIssuesService;
    @Resource
    private BaseHtProducttionKeyIssuesService baseHtProducttionKeyIssuesService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseProducttionKeyIssues baseProducttionKeyIssues) {
        return ControllerUtil.returnCRUD(baseProducttionKeyIssuesService.save(baseProducttionKeyIssues));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProducttionKeyIssuesService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseProducttionKeyIssues.update.class) BaseProducttionKeyIssues baseProducttionKeyIssues) {
        return ControllerUtil.returnCRUD(baseProducttionKeyIssuesService.update(baseProducttionKeyIssues));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProducttionKeyIssues> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseProducttionKeyIssues  baseProducttionKeyIssues = baseProducttionKeyIssuesService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProducttionKeyIssues,StringUtils.isEmpty(baseProducttionKeyIssues)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProducttionKeyIssues>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProducttionKeyIssues searchBaseProducttionKeyIssues) {
        Page<Object> page = PageHelper.startPage(searchBaseProducttionKeyIssues.getStartPage(),searchBaseProducttionKeyIssues.getPageSize());
        List<BaseProducttionKeyIssues> list = baseProducttionKeyIssuesService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProducttionKeyIssues));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProducttionKeyIssues>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProducttionKeyIssues searchBaseProducttionKeyIssues) {
        Page<Object> page = PageHelper.startPage(searchBaseProducttionKeyIssues.getStartPage(),searchBaseProducttionKeyIssues.getPageSize());
        List<BaseHtProducttionKeyIssues> list = baseHtProducttionKeyIssuesService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProducttionKeyIssues));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseProducttionKeyIssues searchBaseProducttionKeyIssues){
    List<BaseProducttionKeyIssues> list = baseProducttionKeyIssuesService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProducttionKeyIssues));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "产品关键事项维护", BaseProducttionKeyIssues.class, "产品关键事项维护.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
