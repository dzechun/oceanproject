package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.dto.basic.SmtPackageSpecificationDto;
import com.fantechs.common.base.entity.basic.SmtPackageSpecification;
import com.fantechs.common.base.entity.basic.history.SmtHtPackageSpecification;
import com.fantechs.common.base.entity.basic.search.SearchSmtPackageSpecification;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtPackageSpecificationService;
import com.fantechs.provider.imes.basic.service.SmtPackageSpecificationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 *
 * Created by leifengzhi on 2020/11/04.
 */
@RestController
@Api(tags = "包装规格信息管理")
@RequestMapping("/smtPackageSpecification")
@Validated
public class SmtPackageSpecificationController {

    @Autowired
    private SmtPackageSpecificationService smtPackageSpecificationService;
    @Autowired
    private SmtHtPackageSpecificationService smtHtPackageSpecificationService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：packageSpecificationCoded、packageSpecificationName、materialId、barcodeRuleId、packingUnitId",required = true)@RequestBody @Validated SmtPackageSpecification smtPackageSpecification) {
        return ControllerUtil.returnCRUD(smtPackageSpecificationService.save(smtPackageSpecification));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtPackageSpecificationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtPackageSpecification.update.class) SmtPackageSpecification smtPackageSpecification) {
        return ControllerUtil.returnCRUD(smtPackageSpecificationService.update(smtPackageSpecification));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtPackageSpecification> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtPackageSpecification  smtPackageSpecification = smtPackageSpecificationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtPackageSpecification,StringUtils.isEmpty(smtPackageSpecification)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtPackageSpecificationDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtPackageSpecification searchSmtPackageSpecification) {
        Page<Object> page = PageHelper.startPage(searchSmtPackageSpecification.getStartPage(),searchSmtPackageSpecification.getPageSize());
        List<SmtPackageSpecificationDto> list = smtPackageSpecificationService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtPackageSpecification));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtPackageSpecification>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtPackageSpecification searchSmtPackageSpecification) {
        Page<Object> page = PageHelper.startPage(searchSmtPackageSpecification.getStartPage(),searchSmtPackageSpecification.getPageSize());
        List<SmtHtPackageSpecification> list = smtHtPackageSpecificationService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtPackageSpecification));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtPackageSpecification searchSmtPackageSpecification){
    List<SmtPackageSpecificationDto> list = smtPackageSpecificationService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtPackageSpecification));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtPackageSpecification信息", SmtPackageSpecificationDto.class, "SmtPackageSpecification.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
