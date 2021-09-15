package com.fantechs.provider.guest.callagv.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleLog;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvVehicleLog;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.service.CallAgvVehicleLogService;
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

@RestController
@Api(tags = "callAgvVehicleLog控制器")
@RequestMapping("/callAgvVehicleLog")
@Validated
public class CallAgvVehicleLogController {

    @Resource
    private CallAgvVehicleLogService callAgvVehicleLogService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated CallAgvVehicleLog callAgvVehicleLog) {
        return ControllerUtil.returnCRUD(callAgvVehicleLogService.save(callAgvVehicleLog));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(callAgvVehicleLogService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=CallAgvVehicleLog.update.class) CallAgvVehicleLog callAgvVehicleLog) {
        return ControllerUtil.returnCRUD(callAgvVehicleLogService.update(callAgvVehicleLog));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<CallAgvVehicleLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        CallAgvVehicleLog  callAgvVehicleLog = callAgvVehicleLogService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(callAgvVehicleLog,StringUtils.isEmpty(callAgvVehicleLog)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<CallAgvVehicleLogDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchCallAgvVehicleLog searchCallAgvVehicleLog) {
        Page<Object> page = PageHelper.startPage(searchCallAgvVehicleLog.getStartPage(),searchCallAgvVehicleLog.getPageSize());
        List<CallAgvVehicleLogDto> list = callAgvVehicleLogService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvVehicleLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<CallAgvVehicleLogDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchCallAgvVehicleLog searchCallAgvVehicleLog) {
        List<CallAgvVehicleLogDto> list = callAgvVehicleLogService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvVehicleLog));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchCallAgvVehicleLog searchCallAgvVehicleLog){
    List<CallAgvVehicleLogDto> list = callAgvVehicleLogService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvVehicleLog));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "CallAgvVehicleLog信息", CallAgvVehicleLogDto.class, "CallAgvVehicleLog.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
