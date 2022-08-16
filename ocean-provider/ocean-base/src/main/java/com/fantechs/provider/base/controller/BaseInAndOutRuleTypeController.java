package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleType;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRule;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRuleType;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInAndOutRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInAndOutRuleType;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtInAndOutRuleTypeService;
import com.fantechs.provider.base.service.BaseInAndOutRuleTypeService;
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
@Api(tags = "规则类型控制器")
@RequestMapping("/baseInAndOutRuleType")
@Validated
public class BaseInAndOutRuleTypeController {

    @Resource
    private BaseInAndOutRuleTypeService baseInAndOutRuleTypeService;
    @Resource
    private BaseHtInAndOutRuleTypeService baseHtInAndOutRuleTypeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseInAndOutRuleType baseInAndOutRuleType) {
        return ControllerUtil.returnCRUD(baseInAndOutRuleTypeService.save(baseInAndOutRuleType));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseInAndOutRuleTypeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseInAndOutRuleType.update.class) BaseInAndOutRuleType baseInAndOutRuleType) {
        return ControllerUtil.returnCRUD(baseInAndOutRuleTypeService.update(baseInAndOutRuleType));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseInAndOutRuleType> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseInAndOutRuleType  baseInAndOutRuleType = baseInAndOutRuleTypeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseInAndOutRuleType,StringUtils.isEmpty(baseInAndOutRuleType)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseInAndOutRuleType>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInAndOutRuleType searchBaseInAndOutRuleType) {
        Page<Object> page = PageHelper.startPage(searchBaseInAndOutRuleType.getStartPage(),searchBaseInAndOutRuleType.getPageSize());
        List<BaseInAndOutRuleType> list = baseInAndOutRuleTypeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRuleType));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInAndOutRuleType>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInAndOutRuleType searchBaseInAndOutRuleType) {
        Page<Object> page = PageHelper.startPage(searchBaseInAndOutRuleType.getStartPage(),searchBaseInAndOutRuleType.getPageSize());
        List<BaseHtInAndOutRuleType> list = baseHtInAndOutRuleTypeService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRuleType));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseInAndOutRuleType searchBaseInAndOutRuleType){
    List<BaseInAndOutRuleType> list = baseInAndOutRuleTypeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRuleType));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "出入库规则类型信息", BaseInAndOutRuleType.class, "出入库规则类型信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
