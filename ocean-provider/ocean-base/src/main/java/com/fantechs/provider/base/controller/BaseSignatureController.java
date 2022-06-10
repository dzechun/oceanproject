package com.fantechs.provider.base.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseSignatureImport;
import com.fantechs.common.base.general.entity.basic.BaseSignature;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSignature;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSignature;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtSignatureService;
import com.fantechs.provider.base.service.BaseSignatureService;
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

/**
 *
 * Created by wcz on 2020/09/24.
 */
@RestController
@Api(tags = "物料特征码信息管理")
@RequestMapping("/baseSignature")
@Validated
@Slf4j
public class BaseSignatureController {

    @Resource
    private BaseSignatureService baseSignatureService;
    @Resource
    private BaseHtSignatureService baseHtSignatureService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialId、signatureCode",required = true)@RequestBody @Validated BaseSignature baseSignature) {
        return ControllerUtil.returnCRUD(baseSignatureService.save(baseSignature));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSignatureService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = BaseSignature.update.class) BaseSignature baseSignature) {
        return ControllerUtil.returnCRUD(baseSignatureService.update(baseSignature));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSignature> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        BaseSignature baseSignature = baseSignatureService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSignature,StringUtils.isEmpty(baseSignature)?0:1);
    }

    @ApiOperation("根据条件查询物料特征码信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSignature>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSignature searchBaseSignature) {
        Page<Object> page = PageHelper.startPage(searchBaseSignature.getStartPage(), searchBaseSignature.getPageSize());
        List<BaseSignature> list = baseSignatureService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSignature));
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
                            @RequestBody(required = false) SearchBaseSignature searchBaseSignature){
    List<BaseSignature> list = baseSignatureService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSignature));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        try {
            // 自定义导出操作
            EasyPoiUtils.customExportExcel(list, customExportParamList, "导出物料特征码信息", "物料特征码信息", "物料特征码信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("根据条件查询物料特征码历史信息列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtSignature>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSignature searchBaseSignature) {
        Page<Object> page = PageHelper.startPage(searchBaseSignature.getStartPage(), searchBaseSignature.getPageSize());
        List<BaseHtSignature> list = baseHtSignatureService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseSignature));
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
            List<BaseSignatureImport> baseSignatureImports = EasyPoiUtils.importExcel(file, 2, 1, BaseSignatureImport.class);
            Map<String, Object> resultMap = baseSignatureService.importExcel(baseSignatureImports);
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
}
