package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseCustomerImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseSupplierImport;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInspectionExemptedList;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseSupplierReUserService;
import com.fantechs.provider.base.service.BaseSupplierService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2020/09/27.
 */
@RestController
@Api(tags = "供应商信息管理")
@RequestMapping("/baseSupplier")
@Validated
@Slf4j
public class BaseSupplierController {


    @Resource
    private BaseSupplierService baseSupplierService;
    @Resource
    private BaseSupplierReUserService baseSupplierReUserService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：supplierCode、supplierName",required = true)@RequestBody @Validated BaseSupplier baseSupplier) {
        return ControllerUtil.returnCRUD(baseSupplierService.save(baseSupplier));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSupplierService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = BaseSupplier.update.class) BaseSupplier baseSupplier) {
        return ControllerUtil.returnCRUD(baseSupplierService.update(baseSupplier));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSupplier> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseSupplier baseSupplier = baseSupplierService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSupplier,StringUtils.isEmpty(baseSupplier)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSupplier>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSupplier searchBaseSupplier) {
        Page<Object> page = PageHelper.startPage(searchBaseSupplier.getStartPage(), searchBaseSupplier.getPageSize());
        List<BaseSupplier> list = baseSupplierService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSupplier));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtSupplier>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSupplier searchBaseSupplier) {
        Page<Object> page = PageHelper.startPage(searchBaseSupplier.getStartPage(),searchBaseSupplier.getPageSize());
        List<BaseHtSupplier> list = baseSupplierService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseSupplier));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseSupplier>> findAll() {
        List<BaseSupplier> list = baseSupplierService.findAll(new HashMap<>());
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("非免检客户（供应商）列表")
    @PostMapping("/findInspectionSupplierList")
    public ResponseEntity<List<BaseSupplier>> findInspectionSupplierList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionExemptedList searchBaseInspectionExemptedList) {
        Page<Object> page = PageHelper.startPage(searchBaseInspectionExemptedList.getStartPage(), searchBaseInspectionExemptedList.getPageSize());
        List<BaseSupplier> list = baseSupplierService.findInspectionSupplierList(searchBaseInspectionExemptedList);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出供应商excel",notes = "导出供应商excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseSupplier searchBaseSupplier){
        List<BaseSupplier> list = baseSupplierService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSupplier));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = com.fantechs.common.base.utils.BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "供应商信息导出", "供应商信息", "供应商.xls", response);
    }

    /**
     * 从excel导入供应商数据
     * @return
     * @throws
     */
    @PostMapping(value = "/importSupplier")
    @ApiOperation(value = "从excel导入供应商信息",notes = "从excel导入供应商信息")
    public ResponseEntity importSupplierExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file,@ApiParam(value = "身份标识（1、供应商 2、客户）") @RequestParam Byte supplierType){
        try {
            // 导入操作
            List<BaseSupplierImport> baseSupplierImports = EasyPoiUtils.importExcel(file, 2, 1, BaseSupplierImport.class);
            Map<String, Object> resultMap = baseSupplierService.importExcel(baseSupplierImports, supplierType);
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

    /**
     * 从excel导入客户数据数据
     * @return
     * @throws
     */
    @PostMapping(value = "/importCustomer")
    @ApiOperation(value = "从excel导入客户信息",notes = "从excel导入客户信息")
    public ResponseEntity importCustomerExcel(@ApiParam(value ="输入excel文件",required = true)
                                              @RequestPart(value="file") MultipartFile file,@ApiParam(value = "身份标识（1、供应商 2、客户）") @RequestParam Byte supplierType){
        try {
            // 导入操作
            List<BaseCustomerImport> baseCustomerImports = EasyPoiUtils.importExcel(file, 2, 1, BaseCustomerImport.class);
            List<BaseSupplierImport> baseSupplierImports = new ArrayList<>();
            if (StringUtils.isNotEmpty(baseCustomerImports)){
                for (BaseCustomerImport baseCustomerImport : baseCustomerImports) {
                    BaseSupplierImport baseSupplierImport = new BaseSupplierImport();
                    BeanUtils.copyProperties(baseCustomerImport, baseSupplierImport);
                    baseSupplierImports.add(baseSupplierImport);
                }
            }
            Map<String, Object> resultMap = baseSupplierService.importExcel(baseSupplierImports,supplierType);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity saveByApi(@ApiParam(value = "必传：supplierCode、supplierName",required = true)@RequestBody @Validated BaseSupplier baseSupplier) {
        return ControllerUtil.returnCRUD(baseSupplierService.saveByApi(baseSupplier));
    }
}
