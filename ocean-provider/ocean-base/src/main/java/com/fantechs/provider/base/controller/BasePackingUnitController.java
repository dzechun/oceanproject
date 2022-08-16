package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BasePackingUnitDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePackingUnitImport;
import com.fantechs.common.base.general.entity.basic.BasePackingUnit;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPackingUnit;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePackingUnit;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtPackingUnitService;
import com.fantechs.provider.base.service.BasePackingUnitService;
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
 * Created by leifengzhi on 2020/11/03.
 */
@RestController
@Api(tags = "包装单位信息管理")
@RequestMapping("/basePackingUnit")
@Validated
@Slf4j
public class BasePackingUnitController {

    @Resource
    private BasePackingUnitService basePackingUnitService;
    @Resource
    private BaseHtPackingUnitService baseHtPackingUnitService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：packingUnitName",required = true)@RequestBody @Validated BasePackingUnit basePackingUnit) {
        return ControllerUtil.returnCRUD(basePackingUnitService.save(basePackingUnit));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(basePackingUnitService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传、packingUnitName",required = true)@RequestBody @Validated(value= BasePackingUnit.update.class) BasePackingUnit basePackingUnit) {
        return ControllerUtil.returnCRUD(basePackingUnitService.update(basePackingUnit));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BasePackingUnit> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BasePackingUnit basePackingUnit = basePackingUnitService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(basePackingUnit,StringUtils.isEmpty(basePackingUnit)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BasePackingUnitDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBasePackingUnit searchBasePackingUnit) {
        Page<Object> page = PageHelper.startPage(searchBasePackingUnit.getStartPage(), searchBasePackingUnit.getPageSize());
        List<BasePackingUnitDto> list = basePackingUnitService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePackingUnit));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtPackingUnit>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBasePackingUnit searchBasePackingUnit) {
        Page<Object> page = PageHelper.startPage(searchBasePackingUnit.getStartPage(), searchBasePackingUnit.getPageSize());
        List<BaseHtPackingUnit> list = baseHtPackingUnitService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBasePackingUnit));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBasePackingUnit searchBasePackingUnit){
    List<BasePackingUnitDto> smtPackingUnitDtos = basePackingUnitService.findList(ControllerUtil.dynamicConditionByEntity(searchBasePackingUnit));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(smtPackingUnitDtos, "导出信息", "SmtPackingUnit信息", BasePackingUnitDto.class, "SmtPackingUnit.xls", response);
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
        try {
            // 导入操作
            List<BasePackingUnitImport> basePackingUnitImports = EasyPoiUtils.importExcel(file, 2, 1, BasePackingUnitImport.class);
            Map<String, Object> resultMap = basePackingUnitService.importExcel(basePackingUnitImports);
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
