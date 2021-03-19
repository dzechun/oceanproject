package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.imports.SmtProcessImport;
import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.SmtWorkshopSection;
import com.fantechs.common.base.entity.basic.history.SmtHtProcess;
import com.fantechs.common.base.entity.basic.search.SearchSmtProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtProcessService;
import com.fantechs.provider.imes.basic.service.SmtProcessService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/25.
 */
@RestController
@Api(tags = "工序信息管理")
@RequestMapping("/smtProcess")
@Validated
@Slf4j
public class SmtProcessController {

    @Autowired
    private SmtProcessService smtProcessService;

    @Autowired
    private SmtHtProcessService smtHtProcessService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：processCode、processName、processCategoryId",required = true)@RequestBody @Validated SmtProcess smtProcess) {
        return ControllerUtil.returnCRUD(smtProcessService.save(smtProcess));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtProcessService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = SmtProcess.update.class) SmtProcess smtProcess) {
        return ControllerUtil.returnCRUD(smtProcessService.update(smtProcess));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtProcess> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        SmtProcess smtProcess = smtProcessService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtProcess,StringUtils.isEmpty(smtProcess)?0:1);
    }

    @ApiOperation("根据条件查询工序信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtProcess>> findList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchSmtProcess searchSmtProcess) {
        Page<Object> page = PageHelper.startPage(searchSmtProcess.getStartPage(),searchSmtProcess.getPageSize());
        List<SmtProcess> list = smtProcessService.findList(searchSmtProcess);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /**
    * 导出数据
    * @return
    * @throws
    */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出工序信息excel",notes = "导出工序信息excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody(required = false) SearchSmtProcess searchSmtProcess){
    List<SmtProcess> list =smtProcessService.findList(searchSmtProcess);
        try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "工序信息", "工序信息", SmtProcess.class, "工序信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("根据条件查询工序信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtProcess>> findHtList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchSmtProcess searchSmtProcess) {
        Page<Object> page = PageHelper.startPage(searchSmtProcess.getStartPage(),searchSmtProcess.getPageSize());
        List<SmtHtProcess> list = smtHtProcessService.findHtList(searchSmtProcess);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入电子标签信息",notes = "从excel导入电子标签信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<SmtProcessImport> smtProcessImports = EasyPoiUtils.importExcel(file, 2, 1, SmtProcessImport.class);
            Map<String, Object> resultMap = smtProcessService.importExcel(smtProcessImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
