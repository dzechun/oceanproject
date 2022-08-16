package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseProductFamilyDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseProductFamilyImport;
import com.fantechs.common.base.general.entity.basic.BaseProductFamily;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductFamily;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtProductFamilyService;
import com.fantechs.provider.base.service.BaseProductFamilyService;
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
import java.util.NoSuchElementException;

/**
 *
 * Created by leifengzhi on 2020/12/15.
 */
@RestController
@Api(tags = "产品族信息管理")
@RequestMapping("/baseProductFamily")
@Validated
@Slf4j
public class BaseProductFamilyController {

    @Autowired
    private BaseProductFamilyService baseProductFamilyService;
    @Resource
    private BaseHtProductFamilyService baseHtProductFamilyService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：productFamilyCode、productFamilyName",required = true)@RequestBody @Validated BaseProductFamily baseProductFamily) {
        return ControllerUtil.returnCRUD(baseProductFamilyService.save(baseProductFamily));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseProductFamilyService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseProductFamily.update.class) BaseProductFamily baseProductFamily) {
        return ControllerUtil.returnCRUD(baseProductFamilyService.update(baseProductFamily));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseProductFamily> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseProductFamily  baseProductFamily = baseProductFamilyService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseProductFamily,StringUtils.isEmpty(baseProductFamily)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseProductFamilyDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductFamily searchBaseProductFamily) {
        Page<Object> page = PageHelper.startPage(searchBaseProductFamily.getStartPage(),searchBaseProductFamily.getPageSize());
        List<BaseProductFamilyDto> list = baseProductFamilyService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductFamily));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtProductFamily>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductFamily searchBaseProductFamily) {
        Page<Object> page = PageHelper.startPage(searchBaseProductFamily.getStartPage(),searchBaseProductFamily.getPageSize());
        List<BaseHtProductFamily> list = baseHtProductFamilyService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseProductFamily));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseProductFamily searchBaseProductFamily){
    List<BaseProductFamilyDto> list = baseProductFamilyService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseProductFamily));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseProductFamily信息", BaseProductFamilyDto.class, "BaseProductFamily.xls", response);
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
    @ApiOperation(value = "从excel导入产品族信息",notes = "从excel导入产品族信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseProductFamilyImport> baseProductFamilyImports = EasyPoiUtils.importExcel(file, 2, 1, BaseProductFamilyImport.class);
            Map<String, Object> resultMap = baseProductFamilyService.importExcel(baseProductFamilyImports);
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
