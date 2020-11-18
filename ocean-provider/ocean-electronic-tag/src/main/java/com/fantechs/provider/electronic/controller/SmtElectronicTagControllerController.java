package com.fantechs.provider.electronic.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.ImportSmtElectronicTagControllerDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagControllerDto;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagController;
import com.fantechs.common.base.electronic.entity.history.SmtHtElectronicTagController;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagController;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.SmtElectronicTagControllerService;
import com.fantechs.provider.electronic.service.SmtHtElectronicTagControllerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/11/16.
 */
@RestController
@Api(tags = "电子标签控制器")
@RequestMapping("/smtElectronicTagController")
@Validated
@Slf4j
public class SmtElectronicTagControllerController {

    @Autowired
    private SmtElectronicTagControllerService smtElectronicTagControllerService;
    @Autowired
    private SmtHtElectronicTagControllerService smtHtElectronicTagControllerService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：electronicTagControllerCode、electronicTagControllerName、electronicTagControllerIp、electronicTagControllerPort、",required = true)
                                  @RequestBody @Validated SmtElectronicTagController smtElectronicTagController) {
        return ControllerUtil.returnCRUD(smtElectronicTagControllerService.save(smtElectronicTagController));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtElectronicTagControllerService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= SmtElectronicTagController.update.class) SmtElectronicTagController smtElectronicTagController) {
        return ControllerUtil.returnCRUD(smtElectronicTagControllerService.update(smtElectronicTagController));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtElectronicTagController> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtElectronicTagController  smtElectronicTagController = smtElectronicTagControllerService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtElectronicTagController,StringUtils.isEmpty(smtElectronicTagController)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtElectronicTagControllerDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtElectronicTagController searchSmtElectronicTagController) {
        Page<Object> page = PageHelper.startPage(searchSmtElectronicTagController.getStartPage(),searchSmtElectronicTagController.getPageSize());
        List<SmtElectronicTagControllerDto> list = smtElectronicTagControllerService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtElectronicTagController));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtElectronicTagController>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtElectronicTagController searchSmtElectronicTagController) {
        Page<Object> page = PageHelper.startPage(searchSmtElectronicTagController.getStartPage(),searchSmtElectronicTagController.getPageSize());
        List<SmtHtElectronicTagController> list = smtHtElectronicTagControllerService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtElectronicTagController));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtElectronicTagController searchSmtElectronicTagController){
    List<SmtElectronicTagControllerDto> list = smtElectronicTagControllerService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtElectronicTagController));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "电子标签控制器信息", SmtElectronicTagControllerDto.class, "SmtElectronicTagController.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入电子标签信息",notes = "从excel导入电子标签信息")
    public ResponseEntity importUsers(@ApiParam(value ="输入excel文件",required = true)
                                          @RequestPart(value="file") MultipartFile file){
        int i=0;
        try {
            // 导入操作
            List<ImportSmtElectronicTagControllerDto> importSmtElectronicTagControllerDtos = EasyPoiUtils.importExcel(file,ImportSmtElectronicTagControllerDto.class);
            i= smtElectronicTagControllerService.importElectronicTagController(importSmtElectronicTagControllerDtos);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
        return ControllerUtil.returnCRUD(i);
    }
}
