package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigReturnDto;
import com.fantechs.common.base.general.entity.eam.EamJigReturn;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigReturn;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigReturn;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtJigReturnService;
import com.fantechs.provider.eam.service.EamJigReturnService;
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
 * Created by leifengzhi on 2021/07/30.
 */
@RestController
@Api(tags = "治具归还")
@RequestMapping("/eamJigReturn")
@Validated
public class EamJigReturnController {

    @Resource
    private EamJigReturnService eamJigReturnService;
    @Resource
    private EamHtJigReturnService eamHtJigReturnService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigReturn eamJigReturn) {
        return ControllerUtil.returnCRUD(eamJigReturnService.save(eamJigReturn));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigReturnService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigReturn.update.class) EamJigReturn eamJigReturn) {
        return ControllerUtil.returnCRUD(eamJigReturnService.update(eamJigReturn));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigReturn> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigReturn  eamJigReturn = eamJigReturnService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigReturn,StringUtils.isEmpty(eamJigReturn)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigReturnDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigReturn searchEamJigReturn) {
        Page<Object> page = PageHelper.startPage(searchEamJigReturn.getStartPage(),searchEamJigReturn.getPageSize());
        List<EamJigReturnDto> list = eamJigReturnService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigReturn));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigReturn>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigReturn searchEamJigReturn) {
        Page<Object> page = PageHelper.startPage(searchEamJigReturn.getStartPage(),searchEamJigReturn.getPageSize());
        List<EamHtJigReturn> list = eamHtJigReturnService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigReturn));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigReturn searchEamJigReturn){
    List<EamJigReturnDto> list = eamJigReturnService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigReturn));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "治具归还", EamJigReturnDto.class, "治具归还.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
