package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseHtInAndOutRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseInAndOutRuleDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseInAndOutRuleImport;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInAndOutRule;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseInAndOutRuleService;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/12/30.
 */
@RestController
@Api(tags = "出入库规则")
@RequestMapping("/baseInAndOutRule")
@Validated
@Slf4j
public class BaseInAndOutRuleController {

    @Resource
    private BaseInAndOutRuleService baseInAndOutRuleService;

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
    public ResponseEntity<List<BaseInAndOutRuleDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInAndOutRule searchBaseInAndOutRule) {
        Page<Object> page = PageHelper.startPage(searchBaseInAndOutRule.getStartPage(),searchBaseInAndOutRule.getPageSize());
        List<BaseInAndOutRuleDto> list = baseInAndOutRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRule));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseInAndOutRuleDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBaseInAndOutRule searchBaseInAndOutRule) {
        List<BaseInAndOutRuleDto> list = baseInAndOutRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRule));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInAndOutRuleDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInAndOutRule searchBaseInAndOutRule) {
        Page<Object> page = PageHelper.startPage(searchBaseInAndOutRule.getStartPage(),searchBaseInAndOutRule.getPageSize());
        List<BaseHtInAndOutRuleDto> list = baseInAndOutRuleService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRule));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseInAndOutRule searchBaseInAndOutRule){
    List<BaseInAndOutRuleDto> list = baseInAndOutRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRule));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "出入库规则维护", "出入库规则维护", BaseInAndOutRuleDto.class, "出入库规则维护.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseInAndOutRuleImport> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, BaseInAndOutRuleImport.class);
            Map<String, Object> resultMap = baseInAndOutRuleService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation("查询规则视图")
    @PostMapping("/findView")
    public ResponseEntity<List<String>> findView(@ApiParam("类型(1-入库 2-出库 3-批次)")@RequestParam Byte category) {
        List<String> list = baseInAndOutRuleService.findView(category);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("入库规则")
    @PostMapping("/inRule")
    public ResponseEntity<Long> inRule(@RequestParam Long warehouseId, @RequestParam Long materialId, @RequestParam BigDecimal qty) {
        return ControllerUtil.returnDataSuccess(baseInAndOutRuleService.inRule(warehouseId, materialId, qty),1);
    }

    @ApiOperation("出库规则")
    @PostMapping("/outRule")
    public ResponseEntity<List<String>> outRule(@RequestParam Long warehouseId,@RequestParam Long storageId, @RequestParam Long materialId, @RequestParam BigDecimal qty) {
        return ControllerUtil.returnDataSuccess(baseInAndOutRuleService.outRule(warehouseId,storageId, materialId, qty),1);
    }
}
