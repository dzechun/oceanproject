package com.fantechs.provider.base.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseProcessImport;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseWorkshopSection;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtProcessService;
import com.fantechs.provider.base.service.BaseProcessService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
/**
 *
 * Created by wcz on 2020/09/25.
 */
@RestController
@Api(tags = "工序信息管理")
@RequestMapping("/baseProcess")
@Validated
@Slf4j
public class BaseProcessController {

    @Resource
    private BaseProcessService baseProcessService;

    @Resource
    private BaseHtProcessService baseHtProcessService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：processCode、processName、processCategoryId",required = true)@RequestBody @Validated BaseProcess baseProcess) {
        return ControllerUtil.returnCRUD(baseProcessService.save(baseProcess));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProcessService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = BaseProcess.update.class) BaseProcess baseProcess) {
        return ControllerUtil.returnCRUD(baseProcessService.update(baseProcess));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProcess> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseProcess baseProcess = baseProcessService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProcess,StringUtils.isEmpty(baseProcess)?0:1);
    }

    @ApiOperation("根据条件查询工序信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProcess>> findList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchBaseProcess searchBaseProcess) {
        Page<Object> page = PageHelper.startPage(searchBaseProcess.getStartPage(), searchBaseProcess.getPageSize());
        List<BaseProcess> list = baseProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProcess));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /**
    * 导出数据
    * @return
    * @throws
    */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出工序信息excel",notes = "导出工序信息excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody(required = false) SearchBaseProcess searchBaseProcess){
    List<BaseProcess> list = baseProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProcess));
        try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "工序信息", "工序信息", BaseProcess.class, "工序信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("根据条件查询工序信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProcess>> findHtList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchBaseProcess searchBaseProcess) {
        Page<Object> page = PageHelper.startPage(searchBaseProcess.getStartPage(), searchBaseProcess.getPageSize());
        List<BaseHtProcess> list = baseHtProcessService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProcess));
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
            List<BaseProcessImport> baseProcessImports = EasyPoiUtils.importExcel(file, 2, 1, BaseProcessImport.class);
            Map<String, Object> resultMap = baseProcessService.importExcel(baseProcessImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        }catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/addOrUpdate")
    public ResponseEntity<BaseProcess> addOrUpdate(@ApiParam(value = "必传：productBomCode、materialId",required = true)@RequestBody @Validated BaseProcess baseProcess) {
        BaseProcess baseProcesss = baseProcessService.addOrUpdate(baseProcess);
        return ControllerUtil.returnDataSuccess(baseProcesss, StringUtils.isEmpty(baseProcesss) ? 0 : 1);
    }
}
