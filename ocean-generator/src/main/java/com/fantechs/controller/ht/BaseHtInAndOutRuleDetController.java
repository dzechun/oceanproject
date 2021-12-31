package com.fantechs.controller.ht;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.entity.BaseHtInAndOutRuleDet;
import com.fantechs.service.BaseHtInAndOutRuleDetService;
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
@Api(tags = "baseHtInAndOutRuleDet控制器")
@RequestMapping("/baseHtInAndOutRuleDet")
@Validated
public class BaseHtInAndOutRuleDetController {

    @Resource
    private BaseHtInAndOutRuleDetService baseHtInAndOutRuleDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseHtInAndOutRuleDet baseHtInAndOutRuleDet) {
        return ControllerUtil.returnCRUD(baseHtInAndOutRuleDetService.save(baseHtInAndOutRuleDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseHtInAndOutRuleDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseHtInAndOutRuleDet.update.class) BaseHtInAndOutRuleDet baseHtInAndOutRuleDet) {
        return ControllerUtil.returnCRUD(baseHtInAndOutRuleDetService.update(baseHtInAndOutRuleDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseHtInAndOutRuleDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseHtInAndOutRuleDet  baseHtInAndOutRuleDet = baseHtInAndOutRuleDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseHtInAndOutRuleDet,StringUtils.isEmpty(baseHtInAndOutRuleDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseHtInAndOutRuleDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseHtInAndOutRuleDet searchBaseHtInAndOutRuleDet) {
        Page<Object> page = PageHelper.startPage(searchBaseHtInAndOutRuleDet.getStartPage(),searchBaseHtInAndOutRuleDet.getPageSize());
        List<BaseHtInAndOutRuleDetDto> list = baseHtInAndOutRuleDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseHtInAndOutRuleDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseHtInAndOutRuleDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBaseHtInAndOutRuleDet searchBaseHtInAndOutRuleDet) {
        List<BaseHtInAndOutRuleDetDto> list = baseHtInAndOutRuleDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseHtInAndOutRuleDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInAndOutRuleDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseHtInAndOutRuleDet searchBaseHtInAndOutRuleDet) {
        Page<Object> page = PageHelper.startPage(searchBaseHtInAndOutRuleDet.getStartPage(),searchBaseHtInAndOutRuleDet.getPageSize());
        List<BaseHtInAndOutRuleDet> list = baseHtInAndOutRuleDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseHtInAndOutRuleDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseHtInAndOutRuleDet searchBaseHtInAndOutRuleDet){
    List<BaseHtInAndOutRuleDetDto> list = baseHtInAndOutRuleDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseHtInAndOutRuleDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseHtInAndOutRuleDet信息", BaseHtInAndOutRuleDetDto.class, "BaseHtInAndOutRuleDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseHtInAndOutRuleDet> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, BaseHtInAndOutRuleDet.class);
            Map<String, Object> resultMap = baseHtInAndOutRuleDetService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
