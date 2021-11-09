package com.fantechs.provider.guest.callagv.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.CallAgvAgvTaskDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvAgvTask;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvAgvTask;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.service.CallAgvAgvTaskService;
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
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "任务管理控制器")
@RequestMapping("/callAgvAgvTask")
@Validated
@Slf4j
public class CallAgvAgvTaskController {

    @Resource
    private CallAgvAgvTaskService callAgvAgvTaskService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated CallAgvAgvTask callAgvAgvTask) {
        return ControllerUtil.returnCRUD(callAgvAgvTaskService.save(callAgvAgvTask));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(callAgvAgvTaskService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=CallAgvAgvTask.update.class) CallAgvAgvTask callAgvAgvTask) {
        return ControllerUtil.returnCRUD(callAgvAgvTaskService.update(callAgvAgvTask));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<CallAgvAgvTask> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        CallAgvAgvTask  callAgvAgvTask = callAgvAgvTaskService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(callAgvAgvTask,StringUtils.isEmpty(callAgvAgvTask)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<CallAgvAgvTaskDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchCallAgvAgvTask searchCallAgvAgvTask) {
        Page<Object> page = PageHelper.startPage(searchCallAgvAgvTask.getStartPage(),searchCallAgvAgvTask.getPageSize());
        List<CallAgvAgvTaskDto> list = callAgvAgvTaskService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<CallAgvAgvTaskDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchCallAgvAgvTask searchCallAgvAgvTask) {
        List<CallAgvAgvTaskDto> list = callAgvAgvTaskService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchCallAgvAgvTask searchCallAgvAgvTask){
    List<CallAgvAgvTaskDto> list = callAgvAgvTaskService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvAgvTask));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "CallAgvAgvTask信息", CallAgvAgvTaskDto.class, "CallAgvAgvTask.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<CallAgvAgvTask> baseAddressImports = EasyPoiUtils.importExcel(file, 0, 1, CallAgvAgvTask.class);
            Map<String, Object> resultMap = callAgvAgvTaskService.importExcel(baseAddressImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
