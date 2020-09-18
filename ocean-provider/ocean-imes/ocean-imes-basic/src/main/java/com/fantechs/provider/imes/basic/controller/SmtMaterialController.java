package com.fantechs.provider.imes.basic.controller;



import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.history.SmtHtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtMaterialService;
import com.fantechs.provider.imes.basic.service.SmtMaterialService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Auther: wcz
 * @Date: 2020/9/15 11:00
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/smtMaterial")
@Api(tags = "物料信息管理")
@Slf4j
public class SmtMaterialController {


    @Autowired
    private SmtMaterialService smtMaterialServiceImpl;

    @Autowired
    private SmtHtMaterialService smtHtMaterialServiceImpl;

    @ApiOperation("根据条件查询物料信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtMaterial>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtMaterial searchSmtMaterial ){
        Page<Object> page = PageHelper.startPage(searchSmtMaterial.getStartPage(),searchSmtMaterial.getPageSize());
        List<SmtMaterial> smtMaterials = smtMaterialServiceImpl.findList(searchSmtMaterial);
        return ControllerUtil.returnDataSuccess(smtMaterials,(int)page.getTotal());
    }

    @ApiOperation("增加物料信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialCode",required = true)@RequestBody SmtMaterial smtMaterial){
        if(StringUtils.isEmpty(
                smtMaterial.getMaterialCode())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtMaterialServiceImpl.insert(smtMaterial));

    }


    @ApiOperation("修改物料信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "物料信息对象，物料信息Id必传",required = true)@RequestBody SmtMaterial smtMaterial){
        if(StringUtils.isEmpty(smtMaterial.getMaterialCode())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtMaterialServiceImpl.updateById(smtMaterial));

    }

    @ApiOperation("删除物料信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "物料对象ID",required = true) @RequestBody List<Long> materialIds){
        if(StringUtils.isEmpty(materialIds)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(smtMaterialServiceImpl.deleteByIds(materialIds));
    }

    @ApiOperation("获取物料详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtMaterial> detail(@ApiParam(value = "物料ID",required = true)@RequestParam Long materialId){
        if(StringUtils.isEmpty(materialId)){
            return ControllerUtil.returnFailByParameError();
        }
        SmtMaterial smtMaterial = smtMaterialServiceImpl.findById(materialId);
        return  ControllerUtil.returnDataSuccess(smtMaterial,StringUtils.isEmpty(smtMaterial)?0:1);
    }


    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出物料excel",notes = "导出物料excel")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                              @RequestBody(required = false)SearchSmtMaterial searchSmtMaterial){
        List<SmtMaterial> list = smtMaterialServiceImpl.findList(searchSmtMaterial);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出物料信息", "物料信息", SmtMaterial.class, "物料信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "获取物料履历列表",notes = "获取物料履历列表")
    @PostMapping("/findHtList")
    public ResponseEntity< List<SmtHtMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtMaterial searchSmtMaterial){
        Page<Object> page = PageHelper.startPage(searchSmtMaterial.getStartPage(),searchSmtMaterial.getPageSize());
        List<SmtHtMaterial> htMaterials = smtHtMaterialServiceImpl.findHtList(searchSmtMaterial);
        return ControllerUtil.returnDataSuccess(htMaterials,(int)page.getTotal());
    }
}
