package com.fantechs.provider.base.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseFactoryImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageMaterialImport;
import com.fantechs.common.base.general.entity.basic.BaseStorageMaterial;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtStorageMaterialService;
import com.fantechs.provider.base.service.BaseStorageMaterialService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "物料专用库位管理")
@RequestMapping("/baseStorageMaterial")
@Validated
@Slf4j
public class BaseStorageMaterialController {

    @Resource
    private BaseStorageMaterialService baseStorageMaterialService;

    @Resource
    private BaseHtStorageMaterialService baseHtStorageMaterialService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：storageId、materialId",required = true)@RequestBody @Validated BaseStorageMaterial baseStorageMaterial) {
        return ControllerUtil.returnCRUD(baseStorageMaterialService.save(baseStorageMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseStorageMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = BaseStorageMaterial.update.class) BaseStorageMaterial baseStorageMaterial) {
        return ControllerUtil.returnCRUD(baseStorageMaterialService.update(baseStorageMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseStorageMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        BaseStorageMaterial baseStorageMaterial = baseStorageMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseStorageMaterial,StringUtils.isEmpty(baseStorageMaterial)?0:1);
    }

    @ApiOperation("根据条件查询信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseStorageMaterial>> findList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchBaseStorageMaterial searchBaseStorageMaterial) {
        Page<Object> page = PageHelper.startPage(searchBaseStorageMaterial.getStartPage(), searchBaseStorageMaterial.getPageSize());
        searchBaseStorageMaterial.setCodeQueryMark(2);
        List<BaseStorageMaterial> list = baseStorageMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorageMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("根据条件查询信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtStorageMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchBaseStorageMaterial searchBaseStorageMaterial) {
        Page<Object> page = PageHelper.startPage(searchBaseStorageMaterial.getStartPage(), searchBaseStorageMaterial.getPageSize());
        searchBaseStorageMaterial.setCodeQueryMark(2);
        List<BaseHtStorageMaterial> list = baseHtStorageMaterialService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseStorageMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出物料专用库位信息excel",notes = "导出储位物料信息excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody(required = false) SearchBaseStorageMaterial searchBaseStorageMaterial){
        List<BaseStorageMaterial> list = baseStorageMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorageMaterial));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "物料专用库位信息", "物料专用库位信息", BaseStorageMaterial.class, "物料专用库位.xls", response);
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
            List<BaseStorageMaterialImport> baseStorageMaterialImports = EasyPoiUtils.importExcel(file, 2, 1, BaseStorageMaterialImport.class);
            Map<String, Object> resultMap = baseStorageMaterialService.importExcel(baseStorageMaterialImports);
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
