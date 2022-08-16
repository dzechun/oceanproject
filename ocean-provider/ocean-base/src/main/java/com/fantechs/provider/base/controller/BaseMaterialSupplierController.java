package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseMaterialSupplierDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseMaterialSupplierImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterialSupplier;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialSupplier;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialSupplier;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseMaterialSupplierService;
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
import java.util.NoSuchElementException;

/**
 *
 * Created by wcz on 2020/11/03.
 */
@RestController
@Api(tags = "物料编码关联客户料号")
@RequestMapping("/baseMaterialSupplier")
@Validated
@Slf4j
public class BaseMaterialSupplierController {

    @Resource
    private BaseMaterialSupplierService baseMaterialSupplierService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialSupplierId,materialId,materialSupplierCode,supplierId",required = true)@RequestBody @Validated BaseMaterialSupplier baseMaterialSupplier) {
        return ControllerUtil.returnCRUD(baseMaterialSupplierService.save(baseMaterialSupplier));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseMaterialSupplierService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseMaterialSupplier.update.class) BaseMaterialSupplier baseMaterialSupplier) {
        return ControllerUtil.returnCRUD(baseMaterialSupplierService.update(baseMaterialSupplier));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseMaterialSupplier> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseMaterialSupplier baseMaterialSupplier = baseMaterialSupplierService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseMaterialSupplier,StringUtils.isEmpty(baseMaterialSupplier)?0:1);
    }

    @ApiOperation("物料编码关联客户料号列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseMaterialSupplierDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseMaterialSupplier searchBaseMaterialSupplier) {
        Page<Object> page = PageHelper.startPage(searchBaseMaterialSupplier.getStartPage(), searchBaseMaterialSupplier.getPageSize());
        List<BaseMaterialSupplierDto> list = baseMaterialSupplierService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialSupplier));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtMaterialSupplier>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseMaterialSupplier searchBaseMaterialSupplier) {
        Page<Object> page = PageHelper.startPage(searchBaseMaterialSupplier.getStartPage(),searchBaseMaterialSupplier.getPageSize());
        List<BaseHtMaterialSupplier> list = baseMaterialSupplierService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialSupplier));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchBaseMaterialSupplier searchBaseMaterialSupplier){
    List<BaseMaterialSupplierDto> list = baseMaterialSupplierService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialSupplier));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出物料编码关联客户料号信息", "物料编码关联客户料号信息", BaseMaterialSupplierDto.class, "物料编码关联客户料号.xls", response);
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
    @ApiOperation(value = "从excel导入信息",notes = "从excel导入信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseMaterialSupplierImport> baseMaterialSupplierImports = EasyPoiUtils.importExcel(file, 2, 1, BaseMaterialSupplierImport.class);
            Map<String, Object> resultMap = baseMaterialSupplierService.importExcel(baseMaterialSupplierImports);
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
