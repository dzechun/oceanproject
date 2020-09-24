package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtStorageMaterialService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * Created by wcz on 2020/09/24.
 */
@RestController
@Api(tags = "smtStorageMaterial控制器")
@RequestMapping("/smtStorageMaterial")
public class SmtStorageMaterialController {

    @Autowired
    private SmtStorageMaterialService smtStorageMaterialService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody SmtStorageMaterial smtStorageMaterial) {
        return ControllerUtil.returnCRUD(smtStorageMaterialService.insert(smtStorageMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtStorageMaterialService.batchDel(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SmtStorageMaterial smtStorageMaterial) {
        if(StringUtils.isEmpty(smtStorageMaterial.getStorageMaterialId())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtStorageMaterialService.updateById(smtStorageMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtStorageMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam Long id) {
        if(StringUtils.isEmpty(id)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtStorageMaterial smtStorageMaterial = smtStorageMaterialService.selectById(id);
        return  ControllerUtil.returnDataSuccess(smtStorageMaterial,StringUtils.isEmpty(smtStorageMaterial)?0:1);
    }

    @ApiOperation("根据条件查询信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtStorageMaterial>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStorageMaterial searchSmtStorageMaterial) {
        Page<Object> page = PageHelper.startPage(searchSmtStorageMaterial.getStartPage(),searchSmtStorageMaterial.getPageSize());
        List<SmtStorageMaterial> list = smtStorageMaterialService.findList(searchSmtStorageMaterial);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
