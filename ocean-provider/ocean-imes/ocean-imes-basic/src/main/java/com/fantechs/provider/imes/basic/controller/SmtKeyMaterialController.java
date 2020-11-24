package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.dto.basic.SmtKeyMaterialDto;
import com.fantechs.common.base.entity.basic.SmtKeyMaterial;
import com.fantechs.common.base.entity.basic.history.SmtHtKeyMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtKeyMaterial;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtKeyMaterialMapper;
import com.fantechs.provider.imes.basic.service.SmtKeyMaterialService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/11/24.
 */
@RestController
@Api(tags = "关键物料信息")
@RequestMapping("/smtKeyMaterial")
@Validated
public class SmtKeyMaterialController {

    @Autowired
    private SmtKeyMaterialService smtKeyMaterialService;
    @Resource
    private SmtHtKeyMaterialMapper smtHtKeyMaterialMapper;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：productType、partMaterialId",required = true)@RequestBody @Validated SmtKeyMaterial smtKeyMaterial) {
        return ControllerUtil.returnCRUD(smtKeyMaterialService.save(smtKeyMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtKeyMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtKeyMaterial.update.class) SmtKeyMaterial smtKeyMaterial) {
        return ControllerUtil.returnCRUD(smtKeyMaterialService.update(smtKeyMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtKeyMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtKeyMaterial  smtKeyMaterial = smtKeyMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtKeyMaterial,StringUtils.isEmpty(smtKeyMaterial)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtKeyMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtKeyMaterial searchSmtKeyMaterial) {
        Page<Object> page = PageHelper.startPage(searchSmtKeyMaterial.getStartPage(),searchSmtKeyMaterial.getPageSize());
        List<SmtKeyMaterialDto> list = smtKeyMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtKeyMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtKeyMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtKeyMaterial searchSmtKeyMaterial) {
        Page<Object> page = PageHelper.startPage(searchSmtKeyMaterial.getStartPage(),searchSmtKeyMaterial.getPageSize());
        List<SmtHtKeyMaterial> list = smtHtKeyMaterialMapper.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtKeyMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtKeyMaterial searchSmtKeyMaterial){
    List<SmtKeyMaterialDto> list = smtKeyMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtKeyMaterial));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtKeyMaterial信息", SmtKeyMaterialDto.class, "SmtKeyMaterial.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
