package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.history.SmtHtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtStorageMaterialService;
import com.fantechs.provider.imes.basic.service.SmtStorageMaterialService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/24.
 */
@RestController
@Api(tags = "储位物料信息")
@RequestMapping("/smtStorageMaterial")
@Validated
public class SmtStorageMaterialController {

    @Autowired
    private SmtStorageMaterialService smtStorageMaterialService;

    @Autowired
    private SmtHtStorageMaterialService smtHtStorageMaterialService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：storageId",required = true)@RequestBody @Validated SmtStorageMaterial smtStorageMaterial) {
        return ControllerUtil.returnCRUD(smtStorageMaterialService.save(smtStorageMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtStorageMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = SmtStorageMaterial.update.class) SmtStorageMaterial smtStorageMaterial) {
        return ControllerUtil.returnCRUD(smtStorageMaterialService.update(smtStorageMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtStorageMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtStorageMaterial smtStorageMaterial = smtStorageMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtStorageMaterial,StringUtils.isEmpty(smtStorageMaterial)?0:1);
    }

    @ApiOperation("根据条件查询信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtStorageMaterial>> findList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchSmtStorageMaterial searchSmtStorageMaterial) {
        Page<Object> page = PageHelper.startPage(searchSmtStorageMaterial.getStartPage(),searchSmtStorageMaterial.getPageSize());
        List<SmtStorageMaterial> list = smtStorageMaterialService.findList(searchSmtStorageMaterial);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("根据条件查询信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtStorageMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchSmtStorageMaterial searchSmtStorageMaterial) {
        Page<Object> page = PageHelper.startPage(searchSmtStorageMaterial.getStartPage(),searchSmtStorageMaterial.getPageSize());
        List<SmtHtStorageMaterial> list = smtHtStorageMaterialService.findHtList(searchSmtStorageMaterial);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出储位物料信息excel",notes = "导出储位物料信息excel")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody(required = false) SearchSmtStorageMaterial searchSmtStorageMaterial){
        List<SmtStorageMaterial> list =smtStorageMaterialService.findList(searchSmtStorageMaterial);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出储位物料信息", "储位物料信息", SmtStorageMaterial.class, "储位物料信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
