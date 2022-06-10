package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePackageSpecificationImport;
import com.fantechs.common.base.general.entity.basic.BasePackageSpecification;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPackageSpecification;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePackageSpecification;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtPackageSpecificationService;
import com.fantechs.provider.base.service.BasePackageSpecificationService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


/**
 *
 * Created by leifengzhi on 2020/11/04.
 */
@RestController
@Api(tags = "包装规格信息管理")
@RequestMapping("/basePackageSpecification")
@Validated
@Slf4j
public class BasePackageSpecificationController {

    @Autowired
    private BasePackageSpecificationService basePackageSpecificationService;
    @Autowired
    private BaseHtPackageSpecificationService baseHtPackageSpecificationService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：packageSpecificationCoded、packageSpecificationName",required = true)@RequestBody @Validated BasePackageSpecification basePackageSpecification) {
        return ControllerUtil.returnCRUD(basePackageSpecificationService.save(basePackageSpecification));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(basePackageSpecificationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BasePackageSpecification.update.class) BasePackageSpecification basePackageSpecification) {
        return ControllerUtil.returnCRUD(basePackageSpecificationService.update(basePackageSpecification));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BasePackageSpecification> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BasePackageSpecification basePackageSpecification = basePackageSpecificationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(basePackageSpecification,StringUtils.isEmpty(basePackageSpecification)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BasePackageSpecificationDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBasePackageSpecification searchBasePackageSpecification) {
        Page<Object> page = PageHelper.startPage(searchBasePackageSpecification.getStartPage(), searchBasePackageSpecification.getPageSize());
        List<BasePackageSpecificationDto> list = basePackageSpecificationService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePackageSpecification));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("根据物料工序查询列表")
    @PostMapping("/findByMaterialProcess")
    public ResponseEntity<List<BasePackageSpecificationDto>> findByMaterialProcess(@ApiParam(value = "查询对象")@RequestBody SearchBasePackageSpecification searchBasePackageSpecification) {
        Page<Object> page = PageHelper.startPage(searchBasePackageSpecification.getStartPage(), searchBasePackageSpecification.getPageSize());
        List<BasePackageSpecificationDto> list = basePackageSpecificationService.findByMaterialProcess(ControllerUtil.dynamicConditionByEntity(searchBasePackageSpecification));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtPackageSpecification>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBasePackageSpecification searchBasePackageSpecification) {
        Page<Object> page = PageHelper.startPage(searchBasePackageSpecification.getStartPage(), searchBasePackageSpecification.getPageSize());
        List<BaseHtPackageSpecification> list = baseHtPackageSpecificationService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBasePackageSpecification));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBasePackageSpecification searchBasePackageSpecification){
        List<BasePackageSpecificationDto> list = basePackageSpecificationService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePackageSpecification));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "SmtPackageSpecification信息", "SmtPackageSpecification.xls", response);

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
            List<BasePackageSpecificationImport> basePackageSpecificationImports = EasyPoiUtils.importExcel(file, 2, 1, BasePackageSpecificationImport.class);
            Map<String, Object> resultMap = basePackageSpecificationService.importExcel(basePackageSpecificationImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        } catch (RuntimeException e) {
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
