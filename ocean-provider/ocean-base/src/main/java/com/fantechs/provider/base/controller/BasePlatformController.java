package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BasePlatformImport;
import com.fantechs.common.base.general.entity.basic.BasePlatform;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlatform;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePlatform;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtPlatformService;
import com.fantechs.provider.base.service.BasePlatformService;
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
 * Created by leifengzhi on 2021/10/14.
 */
@RestController
@Api(tags = "月台信息")
@RequestMapping("/basePlatform")
@Validated
@Slf4j
public class BasePlatformController {

    @Resource
    private BasePlatformService basePlatformService;
    @Resource
    private BaseHtPlatformService baseHtPlatformService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BasePlatform basePlatform) {
        return ControllerUtil.returnCRUD(basePlatformService.save(basePlatform));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(basePlatformService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BasePlatform.update.class) BasePlatform basePlatform) {
        return ControllerUtil.returnCRUD(basePlatformService.update(basePlatform));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BasePlatform> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BasePlatform  basePlatform = basePlatformService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(basePlatform,StringUtils.isEmpty(basePlatform)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BasePlatform>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlatform searchBasePlatform) {
        Page<Object> page = PageHelper.startPage(searchBasePlatform.getStartPage(),searchBasePlatform.getPageSize());
        List<BasePlatform> list = basePlatformService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlatform));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BasePlatform>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBasePlatform searchBasePlatform) {
        List<BasePlatform> list = basePlatformService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlatform));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtPlatform>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlatform searchBasePlatform) {
        Page<Object> page = PageHelper.startPage(searchBasePlatform.getStartPage(),searchBasePlatform.getPageSize());
        List<BaseHtPlatform> list = baseHtPlatformService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBasePlatform));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBasePlatform searchBasePlatform){
        List<BasePlatform> list = basePlatformService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePlatform));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "月台信息", "月台信息.xls", response);
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
            List<BasePlatformImport> basePlatformImports = EasyPoiUtils.importExcel(file, 2, 1, BasePlatformImport.class);
            Map<String, Object> resultMap = basePlatformService.importExcel(basePlatformImports);
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
