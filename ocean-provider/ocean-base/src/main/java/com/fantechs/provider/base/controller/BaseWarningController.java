package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarningDto;
import com.fantechs.common.base.general.entity.basic.BaseWarning;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarning;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarning;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtWarningService;
import com.fantechs.provider.base.service.BaseWarningService;
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
 * Created by leifengzhi on 2021/03/03.
 */
@RestController
@Api(tags = "预警信息管理")
@RequestMapping("/baseWarning")
@Validated
public class BaseWarningController {

    @Resource
    private BaseWarningService baseWarningService;
    @Resource
    private BaseHtWarningService baseHtWarningService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseWarning baseWarning) {
        return ControllerUtil.returnCRUD(baseWarningService.save(baseWarning));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseWarningService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseWarning.update.class) BaseWarning baseWarning) {
        return ControllerUtil.returnCRUD(baseWarningService.update(baseWarning));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseWarning> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseWarning  baseWarning = baseWarningService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseWarning,StringUtils.isEmpty(baseWarning)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseWarningDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWarning searchBaseWarning) {
        Page<Object> page = PageHelper.startPage(searchBaseWarning.getStartPage(),searchBaseWarning.getPageSize());
        List<BaseWarningDto> list = baseWarningService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWarning));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtWarning>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWarning searchBaseWarning) {
        Page<Object> page = PageHelper.startPage(searchBaseWarning.getStartPage(),searchBaseWarning.getPageSize());
        List<BaseHtWarning> list = baseHtWarningService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseWarning));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseWarning searchBaseWarning){
    List<BaseWarningDto> list = baseWarningService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWarning));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseWarning信息", BaseWarningDto.class, "BaseWarning.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
