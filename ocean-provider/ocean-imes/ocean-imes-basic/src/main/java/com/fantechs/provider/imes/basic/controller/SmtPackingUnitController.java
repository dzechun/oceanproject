package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtPackingUnitDto;
import com.fantechs.common.base.dto.basic.imports.SmtPackingUnitImport;
import com.fantechs.common.base.entity.basic.SmtPackingUnit;
import com.fantechs.common.base.entity.basic.SmtSignature;
import com.fantechs.common.base.entity.basic.history.SmtHtPackingUnit;
import com.fantechs.common.base.entity.basic.search.SearchSmtPackingUnit;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtPackingUnitService;
import com.fantechs.provider.imes.basic.service.SmtPackingUnitService;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/03.
 */
@RestController
@Api(tags = "包装单位信息管理")
@RequestMapping("/smtPackingUnit")
@Validated
@Slf4j
public class SmtPackingUnitController {

    @Autowired
    private SmtPackingUnitService smtPackingUnitService;
    @Autowired
    private SmtHtPackingUnitService smtHtPackingUnitService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：packingUnitName",required = true)@RequestBody @Validated SmtPackingUnit smtPackingUnit) {
        return ControllerUtil.returnCRUD(smtPackingUnitService.save(smtPackingUnit));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtPackingUnitService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传、packingUnitName",required = true)@RequestBody @Validated(value=SmtPackingUnit.update.class) SmtPackingUnit smtPackingUnit) {
        return ControllerUtil.returnCRUD(smtPackingUnitService.update(smtPackingUnit));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtPackingUnit> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtPackingUnit  smtPackingUnit = smtPackingUnitService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtPackingUnit,StringUtils.isEmpty(smtPackingUnit)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtPackingUnitDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtPackingUnit searchSmtPackingUnit) {
        Page<Object> page = PageHelper.startPage(searchSmtPackingUnit.getStartPage(),searchSmtPackingUnit.getPageSize());
        List<SmtPackingUnitDto> list = smtPackingUnitService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtPackingUnit));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtPackingUnit>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtPackingUnit searchSmtPackingUnit) {
        Page<Object> page = PageHelper.startPage(searchSmtPackingUnit.getStartPage(),searchSmtPackingUnit.getPageSize());
        List<SmtHtPackingUnit> list = smtHtPackingUnitService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtPackingUnit));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtPackingUnit searchSmtPackingUnit){
    List<SmtPackingUnitDto> smtPackingUnitDtos = smtPackingUnitService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtPackingUnit));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(smtPackingUnitDtos, "导出信息", "SmtPackingUnit信息", SmtPackingUnitDto.class, "SmtPackingUnit.xls", response);
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
            List<SmtPackingUnitImport> smtPackingUnitImports = EasyPoiUtils.importExcel(file, 2, 1, SmtPackingUnitImport.class);
            Map<String, Object> resultMap = smtPackingUnitService.importExcel(smtPackingUnitImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
