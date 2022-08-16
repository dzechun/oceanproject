package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRule;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInAndOutRule;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtInAndOutRuleService;
import com.fantechs.provider.base.service.BaseInAndOutRuleService;
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
 * Created by leifengzhi on 2021/05/14.
 */
@RestController
@Api(tags = "出入库规则控制器")
@RequestMapping("/baseInAndOutRule")
@Validated
public class BaseInAndOutRuleController {

    @Resource
    private BaseInAndOutRuleService baseInAndOutRuleService;
    @Resource
    private BaseHtInAndOutRuleService baseHtInAndOutRuleService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseInAndOutRule baseInAndOutRule) {
        return ControllerUtil.returnCRUD(baseInAndOutRuleService.save(baseInAndOutRule));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseInAndOutRuleService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseInAndOutRule.update.class) BaseInAndOutRule baseInAndOutRule) {
        return ControllerUtil.returnCRUD(baseInAndOutRuleService.update(baseInAndOutRule));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseInAndOutRule> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseInAndOutRule  baseInAndOutRule = baseInAndOutRuleService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseInAndOutRule,StringUtils.isEmpty(baseInAndOutRule)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseInAndOutRule>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInAndOutRule searchBaseInAndOutRule) {
        Page<Object> page = PageHelper.startPage(searchBaseInAndOutRule.getStartPage(),searchBaseInAndOutRule.getPageSize());
        List<BaseInAndOutRule> list = baseInAndOutRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRule));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInAndOutRule>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInAndOutRule searchBaseInAndOutRule) {
        Page<Object> page = PageHelper.startPage(searchBaseInAndOutRule.getStartPage(),searchBaseInAndOutRule.getPageSize());
        List<BaseHtInAndOutRule> list = baseHtInAndOutRuleService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRule));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseInAndOutRule searchBaseInAndOutRule){
    List<BaseInAndOutRule> list = baseInAndOutRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRule));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "出入库规则信息", BaseInAndOutRule.class, "出入库规则信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
