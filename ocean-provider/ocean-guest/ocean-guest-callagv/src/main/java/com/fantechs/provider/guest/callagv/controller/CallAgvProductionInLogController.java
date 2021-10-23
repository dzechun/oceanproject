package com.fantechs.provider.guest.callagv.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.CallAgvProductionInLogDetDto;
import com.fantechs.common.base.general.dto.callagv.CallAgvProductionInLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvProductionInLog;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvProductionInLog;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.service.CallAgvProductionInLogService;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "callAgvProductionInLog控制器")
@RequestMapping("/callAgvProductionInLog")
@Validated
public class CallAgvProductionInLogController {

    @Resource
    private CallAgvProductionInLogService callAgvProductionInLogService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated CallAgvProductionInLog callAgvProductionInLog) {
        return ControllerUtil.returnCRUD(callAgvProductionInLogService.save(callAgvProductionInLog));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(callAgvProductionInLogService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=CallAgvProductionInLog.update.class) CallAgvProductionInLog callAgvProductionInLog) {
        return ControllerUtil.returnCRUD(callAgvProductionInLogService.update(callAgvProductionInLog));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<CallAgvProductionInLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        CallAgvProductionInLog  callAgvProductionInLog = callAgvProductionInLogService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(callAgvProductionInLog,StringUtils.isEmpty(callAgvProductionInLog)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<CallAgvProductionInLogDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchCallAgvProductionInLog searchCallAgvProductionInLog) {
        Page<Object> page = PageHelper.startPage(searchCallAgvProductionInLog.getStartPage(),searchCallAgvProductionInLog.getPageSize());
        List<CallAgvProductionInLogDto> list = callAgvProductionInLogService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvProductionInLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<CallAgvProductionInLogDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchCallAgvProductionInLog searchCallAgvProductionInLog) {
        List<CallAgvProductionInLogDto> list = callAgvProductionInLogService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvProductionInLog));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("明细列表")
    @PostMapping("/findDetList")
    public ResponseEntity<List<CallAgvProductionInLogDetDto>> findDetList(@ApiParam(value = "查询对象")@RequestBody SearchCallAgvProductionInLog searchCallAgvProductionInLog) {
        Page<Object> page = PageHelper.startPage(searchCallAgvProductionInLog.getStartPage(),searchCallAgvProductionInLog.getPageSize());
        List<CallAgvProductionInLogDetDto> list = callAgvProductionInLogService.findDetList(ControllerUtil.dynamicConditionByEntity(searchCallAgvProductionInLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchCallAgvProductionInLog searchCallAgvProductionInLog){
        Map<String, Object> map = callAgvProductionInLogService.export(ControllerUtil.dynamicConditionByEntity(searchCallAgvProductionInLog));
    try {
        // 导出操作
        // 导出操作
        List<Class<?>> clzList = new LinkedList<>();
        clzList.add(CallAgvProductionInLogDto.class);
        clzList.add(CallAgvProductionInLogDetDto.class);
        EasyPoiUtils.exportExcelSheetList(map, clzList, "生产出入库信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
