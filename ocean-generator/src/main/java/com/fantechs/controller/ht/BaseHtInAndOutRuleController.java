package com.fantechs.controller.ht;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.entity.BaseHtInAndOutRule;
import com.fantechs.service.BaseHtInAndOutRuleService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by mr.lei on 2021/12/30.
 */
@RestController
@Api(tags = "baseHtInAndOutRule控制器")
@RequestMapping("/baseHtInAndOutRule")
@Validated
public class BaseHtInAndOutRuleController {

    @Resource
    private BaseHtInAndOutRuleService baseHtInAndOutRuleService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseHtInAndOutRule baseHtInAndOutRule) {
        return ControllerUtil.returnCRUD(baseHtInAndOutRuleService.save(baseHtInAndOutRule));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseHtInAndOutRuleService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseHtInAndOutRule.update.class) BaseHtInAndOutRule baseHtInAndOutRule) {
        return ControllerUtil.returnCRUD(baseHtInAndOutRuleService.update(baseHtInAndOutRule));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseHtInAndOutRule> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseHtInAndOutRule  baseHtInAndOutRule = baseHtInAndOutRuleService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseHtInAndOutRule,StringUtils.isEmpty(baseHtInAndOutRule)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseHtInAndOutRuleDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseHtInAndOutRule searchBaseHtInAndOutRule) {
        Page<Object> page = PageHelper.startPage(searchBaseHtInAndOutRule.getStartPage(),searchBaseHtInAndOutRule.getPageSize());
        List<BaseHtInAndOutRuleDto> list = baseHtInAndOutRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseHtInAndOutRule));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseHtInAndOutRuleDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBaseHtInAndOutRule searchBaseHtInAndOutRule) {
        List<BaseHtInAndOutRuleDto> list = baseHtInAndOutRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseHtInAndOutRule));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInAndOutRule>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseHtInAndOutRule searchBaseHtInAndOutRule) {
        Page<Object> page = PageHelper.startPage(searchBaseHtInAndOutRule.getStartPage(),searchBaseHtInAndOutRule.getPageSize());
        List<BaseHtInAndOutRule> list = baseHtInAndOutRuleService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseHtInAndOutRule));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseHtInAndOutRule searchBaseHtInAndOutRule){
    List<BaseHtInAndOutRuleDto> list = baseHtInAndOutRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseHtInAndOutRule));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseHtInAndOutRule信息", BaseHtInAndOutRuleDto.class, "BaseHtInAndOutRule.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseHtInAndOutRule> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, BaseHtInAndOutRule.class);
            Map<String, Object> resultMap = baseHtInAndOutRuleService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
