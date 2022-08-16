package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseLabelMaterialDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseBadnessCauseImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseLabelMaterialImport;
import com.fantechs.common.base.general.entity.basic.BaseLabelMaterial;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtLabelMaterialService;
import com.fantechs.provider.base.service.BaseLabelMaterialService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@RestController
@Api(tags = "产品标签关联信息")
@RequestMapping("/baseLabelMaterial")
@Validated
@Slf4j
public class BaseLabelMaterialController {

    @Resource
    private BaseLabelMaterialService baseLabelMaterialService;
    @Resource
    private BaseHtLabelMaterialService baseHtLabelMaterialService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseLabelMaterial baseLabelMaterial) {
        return ControllerUtil.returnCRUD(baseLabelMaterialService.save(baseLabelMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseLabelMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseLabelMaterial.update.class) BaseLabelMaterial baseLabelMaterial) {
        return ControllerUtil.returnCRUD(baseLabelMaterialService.update(baseLabelMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseLabelMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseLabelMaterial baseLabelMaterial = baseLabelMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseLabelMaterial,StringUtils.isEmpty(baseLabelMaterial)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseLabelMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabelMaterial searchBaseLabelMaterial) {
        Page<Object> page = PageHelper.startPage(searchBaseLabelMaterial.getStartPage(), searchBaseLabelMaterial.getPageSize());
        List<BaseLabelMaterialDto> list = baseLabelMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtLabelMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabelMaterial searchBaseLabelMaterial) {
        Page<Object> page = PageHelper.startPage(searchBaseLabelMaterial.getStartPage(), searchBaseLabelMaterial.getPageSize());
        List<BaseHtLabelMaterial> list = baseHtLabelMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseLabelMaterial searchBaseLabelMaterial){
    List<BaseLabelMaterialDto> list = baseLabelMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabelMaterial));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "标签关联物料信息", BaseLabelMaterialDto.class, "标签关联物料信息.xls", response);
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
            List<BaseLabelMaterialImport> baseLabelMaterialImports = EasyPoiUtils.importExcel(file, 2, 1, BaseLabelMaterialImport.class);
            Map<String, Object> resultMap = baseLabelMaterialService.importExcel(baseLabelMaterialImports);
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
