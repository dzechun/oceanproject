package com.fantechs.provider.electronic.controller;

import com.fantechs.common.base.electronic.dto.SmtPaddingMaterialDto;
import com.fantechs.common.base.electronic.entity.SmtPaddingMaterial;
import com.fantechs.common.base.electronic.entity.search.SearchSmtPaddingMaterial;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.SmtPaddingMaterialService;
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
 * Created by leifengzhi on 2020/12/10.
 */
@RestController
@Api(tags = "上料单管理")
@RequestMapping("/smtPaddingMaterial")
@Validated
public class SmtPaddingMaterialController {

    @Autowired
    private SmtPaddingMaterialService smtPaddingMaterialService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：paddingMaterialCode、materialCode、quantity",required = true)@RequestBody @Validated SmtPaddingMaterial smtPaddingMaterial) {
        return ControllerUtil.returnCRUD(smtPaddingMaterialService.save(smtPaddingMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtPaddingMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtPaddingMaterial.update.class) SmtPaddingMaterial smtPaddingMaterial) {
        return ControllerUtil.returnCRUD(smtPaddingMaterialService.update(smtPaddingMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtPaddingMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtPaddingMaterial  smtPaddingMaterial = smtPaddingMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtPaddingMaterial,StringUtils.isEmpty(smtPaddingMaterial)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtPaddingMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtPaddingMaterial searchSmtPaddingMaterial) {
        Page<Object> page = PageHelper.startPage(searchSmtPaddingMaterial.getStartPage(),searchSmtPaddingMaterial.getPageSize());
        List<SmtPaddingMaterialDto> list = smtPaddingMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtPaddingMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtPaddingMaterial searchSmtPaddingMaterial){
    List<SmtPaddingMaterialDto> list = smtPaddingMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtPaddingMaterial));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtPaddingMaterial信息", SmtPaddingMaterialDto.class, "SmtPaddingMaterial.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
