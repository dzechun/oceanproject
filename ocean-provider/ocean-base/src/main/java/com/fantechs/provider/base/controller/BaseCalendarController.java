package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseCalendarDto;
import com.fantechs.common.base.general.entity.basic.BaseCalendar;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseCalendar;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseCalendarService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by leifengzhi on 2020/12/21.
 */
@RestController
@Api(tags = "日历信息管理")
@RequestMapping("/baseCalendar")
@Validated
public class BaseCalendarController {

    @Autowired
    private BaseCalendarService baseCalendarService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：proLineId", required = true) @RequestBody @Validated BaseCalendar baseCalendar) {
        return ControllerUtil.returnCRUD(baseCalendarService.save(baseCalendar));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseCalendarService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BaseCalendar.update.class) BaseCalendar baseCalendar) {
        return ControllerUtil.returnCRUD(baseCalendarService.update(baseCalendar));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseCalendar> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseCalendar baseCalendar = baseCalendarService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(baseCalendar, StringUtils.isEmpty(baseCalendar) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseCalendarDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseCalendar searchBaseCalendar) {
        Page<Object> page = PageHelper.startPage(searchBaseCalendar.getStartPage(), searchBaseCalendar.getPageSize());
        List<BaseCalendarDto> list = baseCalendarService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseCalendar));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    /*@ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseCalendar>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseCalendar searchBaseCalendar) {
        Page<Object> page = PageHelper.startPage(searchBaseCalendar.getStartPage(),searchBaseCalendar.getPageSize());
        List<BaseCalendar> list = baseCalendarService.findList(searchBaseCalendar);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    /*@PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseCalendar searchBaseCalendar){
    List<BaseCalendar> list = baseCalendarService.findList(searchBaseCalendar);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseCalendar信息", BaseCalendar.class, "BaseCalendar.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }*/

    /*@ApiOperation("获取详情")
    @PostMapping("/findAllWorkShiftTime")
    public ResponseEntity<BaseCalendarDto> findAllWorkShiftTime(
            @ApiParam(value = "proLineId", required = true) @RequestParam Long proLineId,
            @ApiParam(value = "date", required = true) @RequestParam  String date
            ) {
        BaseCalendarDto baseCalendarDto = baseCalendarService.findAllWorkShiftTime(proLineId, date);
        return ControllerUtil.returnSuccess("操作成功",baseCalendarDto);
    }*/
}
