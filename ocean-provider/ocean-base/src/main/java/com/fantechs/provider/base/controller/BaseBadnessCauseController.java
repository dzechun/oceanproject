package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBadnessCauseDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCause;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCause;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBadnessCause;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseBadnessCauseService;
import com.fantechs.provider.base.service.BaseHtBadnessCauseService;
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
 * Created by leifengzhi on 2021/04/09.
 */
@RestController
@Api(tags = "不良原因信息管理")
@RequestMapping("/baseBadnessCause")
@Validated
public class BaseBadnessCauseController {

    @Resource
    private BaseBadnessCauseService baseBadnessCauseService;
    @Resource
    private BaseHtBadnessCauseService baseHtBadnessCauseService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseBadnessCause baseBadnessCause) {
        return ControllerUtil.returnCRUD(baseBadnessCauseService.save(baseBadnessCause));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseBadnessCauseService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseBadnessCause.update.class) BaseBadnessCause baseBadnessCause) {
        return ControllerUtil.returnCRUD(baseBadnessCauseService.update(baseBadnessCause));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseBadnessCause> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseBadnessCause  baseBadnessCause = baseBadnessCauseService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseBadnessCause,StringUtils.isEmpty(baseBadnessCause)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseBadnessCauseDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBadnessCause searchBaseBadnessCause) {
        Page<Object> page = PageHelper.startPage(searchBaseBadnessCause.getStartPage(),searchBaseBadnessCause.getPageSize());
        List<BaseBadnessCauseDto> list = baseBadnessCauseService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessCause));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtBadnessCause>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBadnessCause searchBaseBadnessCause) {
        Page<Object> page = PageHelper.startPage(searchBaseBadnessCause.getStartPage(),searchBaseBadnessCause.getPageSize());
        List<BaseHtBadnessCause> list = baseHtBadnessCauseService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessCause));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseBadnessCause searchBaseBadnessCause){
    List<BaseBadnessCauseDto> list = baseBadnessCauseService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBadnessCause));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseBadnessCause信息", BaseBadnessCauseDto.class, "BaseBadnessCause.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity<BaseBadnessCause> saveByApi(@ApiParam(value = "必传：routeCode、organizationId",required = true)@RequestBody @Validated BaseBadnessCause baseBadnessCause) {
        int i = baseBadnessCauseService.saveByApi(baseBadnessCause);
        return ControllerUtil.returnCRUD(i);
    }
}
