package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseCalendarWorkShiftDto;
import com.fantechs.common.base.general.entity.basic.BaseCalendarWorkShift;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseCalendarWorkShift;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseCalendarWorkShiftService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/21.
 */
@RestController
@Api(tags = "日历班次关系信息管理")
@RequestMapping("/baseCalendarWorkShift")
@Validated
public class BaseCalendarWorkShiftController {

    @Autowired
    private BaseCalendarWorkShiftService baseCalendarWorkShiftService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：workShiftId、day",required = true)@RequestBody @Validated BaseCalendarWorkShift baseCalendarWorkShift) {
        return ControllerUtil.returnCRUD(baseCalendarWorkShiftService.save(baseCalendarWorkShift));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseCalendarWorkShiftService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseCalendarWorkShift.update.class) BaseCalendarWorkShift baseCalendarWorkShift) {
        return ControllerUtil.returnCRUD(baseCalendarWorkShiftService.update(baseCalendarWorkShift));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseCalendarWorkShift> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseCalendarWorkShift  baseCalendarWorkShift = baseCalendarWorkShiftService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseCalendarWorkShift,StringUtils.isEmpty(baseCalendarWorkShift)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseCalendarWorkShiftDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseCalendarWorkShift searchBaseCalendarWorkShift) {
        Page<Object> page = PageHelper.startPage(searchBaseCalendarWorkShift.getStartPage(),searchBaseCalendarWorkShift.getPageSize());
        List<BaseCalendarWorkShiftDto> list = baseCalendarWorkShiftService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseCalendarWorkShift));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /*@ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseCalendarWorkShift>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseCalendarWorkShift searchBaseCalendarWorkShift) {
        Page<Object> page = PageHelper.startPage(searchBaseCalendarWorkShift.getStartPage(),searchBaseCalendarWorkShift.getPageSize());
        List<BaseCalendarWorkShift> list = baseCalendarWorkShiftService.findList(searchBaseCalendarWorkShift);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseCalendarWorkShift searchBaseCalendarWorkShift){
    List<BaseCalendarWorkShift> list = baseCalendarWorkShiftService.findList(searchBaseCalendarWorkShift);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseCalendarWorkShift信息", BaseCalendarWorkShift.class, "BaseCalendarWorkShift.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }*/

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchSave")
    public ResponseEntity batchSave(@ApiParam(value = "必传：workShiftId、day、calendarId、proLineId")@RequestBody List<BaseCalendarWorkShift> baseCalendarWorkShifts) {
        return ControllerUtil.returnCRUD(baseCalendarWorkShiftService.batchSave(baseCalendarWorkShifts));
    }

    @ApiOperation(value = "根据日历和日期清空班次",notes = "根据日历和日期清空班次")
    @PostMapping("/deleteByCalendarIdAndDay")
    public ResponseEntity deleteByCalendarIdAndDay(@ApiParam(value = "日历id")@RequestParam @NotNull(message = "日历id不能为空")Integer calendarId,
                                                   @ApiParam(value = "日期-天")@RequestParam @NotNull(message = "日期不能为空")Byte day) {
        return ControllerUtil.returnCRUD(baseCalendarWorkShiftService.deleteByCalendarIdAndDay(calendarId,day));
    }
}
