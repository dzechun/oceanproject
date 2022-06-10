package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductBomImport;
import com.fantechs.common.base.general.entity.basic.BaseProductBom;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBom;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtProductBomService;
import com.fantechs.provider.base.service.BaseProductBomService;
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
 * Created by wcz on 2020/10/12.
 */
@RestController
@Api(tags = "产品BOM信息管理")
@RequestMapping("/baseProductBom")
@Validated
@Slf4j
public class BaseProductBomController {

    @Resource
    private BaseProductBomService baseProductBomService;
    @Resource
    private BaseHtProductBomService baseHtProductBomService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：productBomCode、materialId", required = true) @RequestBody @Validated BaseProductBom baseProductBom) {
        return ControllerUtil.returnCRUD(baseProductBomService.save(baseProductBom));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProductBomService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BaseProductBom.update.class) BaseProductBom baseProductBom) {
        return ControllerUtil.returnCRUD(baseProductBomService.update(baseProductBom));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProductBom> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseProductBom baseProductBom = baseProductBomService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(baseProductBom, StringUtils.isEmpty(baseProductBom) ? 0 : 1);
    }

    @ApiOperation("产品BOM信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProductBomDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseProductBom searchBaseProductBom) {
        Page<Object> page = PageHelper.startPage(searchBaseProductBom.getStartPage(), searchBaseProductBom.getPageSize());
        List<BaseProductBomDto> list = baseProductBomService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductBom));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("查询下级明细")
    @PostMapping("/findNextLevelProductBomDet")
    public ResponseEntity<BaseProductBomDto> findNextLevelProductBomDet(@ApiParam(value = "查询对象") @RequestBody SearchBaseProductBom searchBaseProductBom) {
        Page<Object> page = PageHelper.startPage(searchBaseProductBom.getStartPage(), searchBaseProductBom.getPageSize());
        BaseProductBomDto BaseProductBomDto = baseProductBomService.findNextLevelProductBomDet(searchBaseProductBom);
        return ControllerUtil.returnDataSuccess(BaseProductBomDto, (int) page.getTotal());
    }


    @ApiOperation("产品BOM信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProductBom>> findHtList(@ApiParam(value = "查询对象") @RequestBody SearchBaseProductBom searchBaseProductBom) {
        Page<Object> page = PageHelper.startPage(searchBaseProductBom.getStartPage(), searchBaseProductBom.getPageSize());
        List<BaseHtProductBom> list = baseHtProductBomService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductBom));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseProductBom searchBaseProductBom) {
        List<BaseProductBomDto> list = baseProductBomService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductBom));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出产品BOM信息", "产品BOM信息", "产品BOM信息.xls", response);
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入部件组成信息",notes = "从excel导入部件组成信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseProductBomImport> baseProductBomImports = EasyPoiUtils.importExcel(file, 2, 1, BaseProductBomImport.class);
            Map<String, Object> resultMap = baseProductBomService.importExcel(baseProductBomImports);
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
    public ResponseEntity<BaseProductBom> addOrUpdate(@ApiParam(value = "必传：productBomCode、materialId",required = true)@RequestBody @Validated BaseProductBom baseProductBom) {
        BaseProductBom baseProductBoms = baseProductBomService.addOrUpdate(baseProductBom);
        return ControllerUtil.returnDataSuccess(baseProductBoms, StringUtils.isEmpty(baseProductBoms) ? 0 : 1);
    }
}
