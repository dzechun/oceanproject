package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseWorkShiftTime;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkShiftTime;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseWorkShiftTimeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/04.
 */
@RestController
@Api(tags = "班次时间信息管理")
@RequestMapping("/baseWorkShiftTime")
@Validated
public class BaseWorkShiftTimeController {

    @Autowired
    private BaseWorkShiftTimeService baseWorkShiftTimeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseWorkShiftTime baseWorkShiftTime) {
        return ControllerUtil.returnCRUD(baseWorkShiftTimeService.save(baseWorkShiftTime));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseWorkShiftTimeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseWorkShiftTime.update.class) BaseWorkShiftTime baseWorkShiftTime) {
        return ControllerUtil.returnCRUD(baseWorkShiftTimeService.update(baseWorkShiftTime));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseWorkShiftTime> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseWorkShiftTime  baseWorkShiftTime = baseWorkShiftTimeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseWorkShiftTime,StringUtils.isEmpty(baseWorkShiftTime)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseWorkShiftTime>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorkShiftTime searchBaseWorkShiftTime) {
        Page<Object> page = PageHelper.startPage(searchBaseWorkShiftTime.getStartPage(),searchBaseWorkShiftTime.getPageSize());
        List<BaseWorkShiftTime> list = baseWorkShiftTimeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShiftTime));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseWorkShiftTime searchBaseWorkShiftTime){
    List<BaseWorkShiftTime> list = baseWorkShiftTimeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShiftTime));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseWorkShiftTime信息", BaseWorkShiftTime.class, "BaseWorkShiftTime.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
