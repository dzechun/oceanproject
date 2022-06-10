package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseMaterialImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtMaterialService;
import com.fantechs.provider.base.service.BaseMaterialService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wcz
 * @Date: 2020/9/15 11:00
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/baseMaterial")
@Api(tags = "物料信息管理")
@Slf4j
@Validated
public class BaseMaterialController {


    @Resource
    private BaseMaterialService baseMaterialService;

    @Resource
    private BaseHtMaterialService baseHtMaterialService;

    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation("根据条件查询物料信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseMaterial searchBaseMaterial){
        Page<Object> page = PageHelper.startPage(searchBaseMaterial.getStartPage(), searchBaseMaterial.getPageSize());
        List<BaseMaterialDto> smtMaterials = baseMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterial));
        return ControllerUtil.returnDataSuccess(smtMaterials,(int)page.getTotal());
    }

    @ApiOperation("查询盘点明细物料信息列表")
    @PostMapping("/findStockDetMaterialList")
    public ResponseEntity<List<BaseMaterialDto>> findStockDetMaterialList(@ApiParam(value = "查询对象")@RequestBody SearchBaseMaterial searchBaseMaterial){
        Page<Object> page = PageHelper.startPage(searchBaseMaterial.getStartPage(), searchBaseMaterial.getPageSize());
        List<BaseMaterialDto> smtMaterials = baseMaterialService.findStockDetMaterialList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterial));
        return ControllerUtil.returnDataSuccess(smtMaterials,(int)page.getTotal());
    }

    @ApiOperation("根据条件查询物料信息列表")
    @PostMapping("/getAll")
    public ResponseEntity<List<BaseMaterialDto>> getAll(){
        List<BaseMaterialDto> smtMaterials = baseMaterialService.findAll(new HashMap<>());
        return ControllerUtil.returnDataSuccess(smtMaterials, smtMaterials.size());
    }

    @ApiOperation("增加物料信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialCode、productModelId、transferQuantity",required = true)@RequestBody @Validated BaseMaterial baseMaterial){
        return ControllerUtil.returnCRUD(baseMaterialService.save(baseMaterial));

    }

    @ApiOperation("修改物料信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "物料信息对象，物料信息Id必传",required = true)@RequestBody @Validated(value = BaseMaterial.update.class) BaseMaterial baseMaterial){
        return ControllerUtil.returnCRUD(baseMaterialService.update(baseMaterial));

    }

    @ApiOperation("删除物料信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "物料对象ID",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids){
        return ControllerUtil.returnCRUD(baseMaterialService.batchDelete(ids));
    }

    @ApiOperation("获取物料详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseMaterial> detail(@ApiParam(value = "物料ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long materialId){
        BaseMaterial baseMaterial = baseMaterialService.selectByKey(materialId);
        return  ControllerUtil.returnDataSuccess(baseMaterial,StringUtils.isEmpty(baseMaterial)?0:1);
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出物料excel",notes = "导出物料excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                              @RequestBody(required = false) SearchBaseMaterial searchBaseMaterial){
        List<BaseMaterialDto> list = baseMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterial));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        try {
            // 自定义导出操作
            EasyPoiUtils.customExportExcel(list, customExportParamList, "物料信息表", "物料信息", "物料信息.xls", response);
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
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){

        // 导入操作
        try {
            List<BaseMaterialImport> baseMaterialImports = EasyPoiUtils.importExcel(file,2,1, BaseMaterialImport.class);
            Map<String, Object> resultMap = baseMaterialService.importExcel(baseMaterialImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
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

    @ApiOperation(value = "获取物料履历列表",notes = "获取物料履历列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseMaterial searchBaseMaterial){
        Page<Object> page = PageHelper.startPage(searchBaseMaterial.getStartPage(), searchBaseMaterial.getPageSize());
        List<BaseHtMaterial> htMaterials = baseHtMaterialService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterial));
        return ControllerUtil.returnDataSuccess(htMaterials,(int)page.getTotal());
    }

    @ApiOperation("批量新增物料信息")
    @PostMapping("/addList")
    public ResponseEntity addList(@ApiParam(value = "物料信息集合")@RequestBody List<BaseMaterial> baseMaterials){
        return ControllerUtil.returnCRUD(baseMaterialService.batchSave(baseMaterials));
    }

    @ApiOperation("批量更新物料信息")
    @PostMapping("/batchUpdateByCode")
    public ResponseEntity batchUpdateByCode(@ApiParam(value = "物料信息集合",required = true)@RequestBody List<BaseMaterial> baseMaterials){
        return ControllerUtil.returnCRUD(baseMaterialService.batchUpdateByCode(baseMaterials));
    }

    @ApiOperation("批量更新物料信息")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "物料信息集合",required = true)@RequestBody List<BaseMaterial> baseMaterials){
        return ControllerUtil.returnCRUD(baseMaterialService.batchUpdate(baseMaterials));
    }

    @ApiOperation("增加物料信息")
    @PostMapping("/saveApi")
    public ResponseEntity<BaseMaterial> saveApi(@ApiParam(value = "必传：materialCode",required = true)@RequestBody @Validated BaseMaterial baseMaterial){
        BaseMaterial material = baseMaterialService.saveApi(baseMaterial);
        return ControllerUtil.returnDataSuccess(material,1);
    }
}
