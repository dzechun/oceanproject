package com.fantechs.provider.pm.controller;

import com.fantechs.common.base.dto.apply.SmtProcessListProcessDto;
import com.fantechs.common.base.entity.apply.SmtProcessListProcess;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.entity.apply.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.provider.pm.service.SmtProcessListProcessService;
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
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/23
*/
@RestController
@Api(tags = "过站信息")
@RequestMapping("/smtProcessListProcess")
@Validated
public class SmtProcessListProcessController {

    @Autowired
    private SmtProcessListProcessService smtProcessListProcessService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtProcessListProcess smtProcessListProcess) {
        return ControllerUtil.returnCRUD(smtProcessListProcessService.save(smtProcessListProcess));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtProcessListProcessService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtProcessListProcess.update.class) SmtProcessListProcess smtProcessListProcess) {
        return ControllerUtil.returnCRUD(smtProcessListProcessService.update(smtProcessListProcess));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtProcessListProcessDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProcessListProcess searchSmtProcessListProcess) {
        Page<Object> page = PageHelper.startPage(searchSmtProcessListProcess.getStartPage(),searchSmtProcessListProcess.getPageSize());
        List<SmtProcessListProcessDto> list = smtProcessListProcessService.findList(searchSmtProcessListProcess);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtProcessListProcess searchSmtProcessListProcess){
    List<SmtProcessListProcessDto> list = smtProcessListProcessService.findList(searchSmtProcessListProcess);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtProcessListProcess信息", SmtProcessListProcessDto.class, "SmtProcessListProcess.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    /**  开始作业
     *  把任务池任务分解，按照工艺流程，生成过站工序列表
     */
    @ApiOperation("开始作业")
    @PostMapping("/startJob")
    public ResponseEntity startJob(SmtWorkOrderBarcodePool smtWorkOrderBarcodePool){
        return ControllerUtil.returnCRUD(smtProcessListProcessService.startJob(smtWorkOrderBarcodePool));
    }

}
