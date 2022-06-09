package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePlatePartsImport;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlateParts;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePlateParts;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtPlatePartsService;
import com.fantechs.provider.base.service.BasePlatePartsService;
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
 * Created by leifengzhi on 2021/01/15.
 */
@RestController
@Api(tags = "部件组成信息管理")
@RequestMapping("/basePlateParts")
@Validated
@Slf4j
public class BasePlatePartsController {

    @Autowired
    private BasePlatePartsService basePlatePartsService;
    @Resource
    private BaseHtPlatePartsService baseHtPlatePartsService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BasePlateParts basePlateParts) {
        return ControllerUtil.returnCRUD(basePlatePartsService.save(basePlateParts));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(basePlatePartsService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BasePlateParts.update.class) BasePlateParts basePlateParts) {
        return ControllerUtil.returnCRUD(basePlatePartsService.update(basePlateParts));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BasePlateParts> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BasePlateParts  basePlateParts = basePlatePartsService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(basePlateParts,StringUtils.isEmpty(basePlateParts)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BasePlatePartsDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlateParts searchBasePlateParts) {
        Page<Object> page = PageHelper.startPage(searchBasePlateParts.getStartPage(),searchBasePlateParts.getPageSize());
        List<BasePlatePartsDto> list =  basePlatePartsService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlateParts));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtPlateParts>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlateParts searchBasePlateParts) {
        Page<Object> page = PageHelper.startPage(searchBasePlateParts.getStartPage(),searchBasePlateParts.getPageSize());
        List<BaseHtPlateParts> list = baseHtPlatePartsService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBasePlateParts));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBasePlateParts searchBasePlateParts){
        List<BasePlatePartsDto> list = basePlatePartsService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlateParts));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "部件组成导出信息", "部件组成信息", "部件组成.xls", response);

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
            List<BasePlatePartsImport> basePlatePartsImports = EasyPoiUtils.importExcel(file, 2, 1, BasePlatePartsImport.class);
            Map<String, Object> resultMap = basePlatePartsService.importExcel(basePlatePartsImports);
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
