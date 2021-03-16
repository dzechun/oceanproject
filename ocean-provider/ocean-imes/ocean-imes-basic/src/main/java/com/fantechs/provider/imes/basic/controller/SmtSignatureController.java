package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtMaterialSupplierDto;
import com.fantechs.common.base.dto.basic.imports.SmtSignatureImport;
import com.fantechs.common.base.entity.basic.SmtSignature;
import com.fantechs.common.base.entity.basic.history.SmtHtSignature;
import com.fantechs.common.base.entity.basic.search.SearchSmtSignature;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtSignatureService;
import com.fantechs.provider.imes.basic.service.SmtSignatureService;
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
 * Created by wcz on 2020/09/24.
 */
@RestController
@Api(tags = "物料特征码信息管理")
@RequestMapping("/smtSignature")
@Validated
@Slf4j
public class SmtSignatureController {

    @Autowired
    private SmtSignatureService smtSignatureService;
    @Autowired
    private SmtHtSignatureService smtHtSignatureService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialId、signatureCode",required = true)@RequestBody @Validated SmtSignature smtSignature) {
        return ControllerUtil.returnCRUD(smtSignatureService.save(smtSignature));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtSignatureService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = SmtSignature.update.class) SmtSignature smtSignature) {
        return ControllerUtil.returnCRUD(smtSignatureService.update(smtSignature));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtSignature> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtSignature  smtSignature = smtSignatureService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtSignature,StringUtils.isEmpty(smtSignature)?0:1);
    }

    @ApiOperation("根据条件查询物料特征码信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtSignature>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtSignature searchSmtSignature) {
        Page<Object> page = PageHelper.startPage(searchSmtSignature.getStartPage(),searchSmtSignature.getPageSize());
        List<SmtSignature> list = smtSignatureService.findList(searchSmtSignature);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /**
    * 导出数据
    * @return
    * @throws
    */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出物料特征码信息excel",notes = "导出物料特征码信息excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchSmtSignature searchSmtSignature){
    List<SmtSignature> list =smtSignatureService.findList(searchSmtSignature);
        try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出物料特征码信息", "物料特征码信息", SmtSignature.class, "物料特征码信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("根据条件查询物料特征码历史信息列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtSignature>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtSignature searchSmtSignature) {
        Page<Object> page = PageHelper.startPage(searchSmtSignature.getStartPage(),searchSmtSignature.getPageSize());
        List<SmtHtSignature> list = smtHtSignatureService.findHtList(searchSmtSignature);
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
            List<SmtSignatureImport> smtSignatureImports = EasyPoiUtils.importExcel(file, 2, 1, SmtSignatureImport.class);
            Map<String, Object> resultMap = smtSignatureService.importExcel(smtSignatureImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
