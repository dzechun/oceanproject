package com.fantechs.provider.imes.basic.controller;



import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtMaterialDto;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.history.SmtHtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseProductFamilyDto;
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
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

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
@Validated
public class SmtMaterialController {


    @Autowired
    private SmtMaterialService smtMaterialService;

    @Autowired
    private SmtHtMaterialService smtHtMaterialService;

    @ApiOperation("根据条件查询物料信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtMaterial searchSmtMaterial ){
        Page<Object> page = PageHelper.startPage(searchSmtMaterial.getStartPage(),searchSmtMaterial.getPageSize());
        List<SmtMaterialDto> smtMaterials = smtMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtMaterial));
        return ControllerUtil.returnDataSuccess(smtMaterials,(int)page.getTotal());
    }

    @ApiOperation("增加物料信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialCode、productModelId、transferQuantity",required = true)@RequestBody @Validated SmtMaterial smtMaterial){
        return ControllerUtil.returnCRUD(smtMaterialService.save(smtMaterial));

    }


    @ApiOperation("修改物料信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "物料信息对象，物料信息Id必传",required = true)@RequestBody @Validated(value = SmtMaterial.update.class) SmtMaterial smtMaterial){
        return ControllerUtil.returnCRUD(smtMaterialService.update(smtMaterial));

    }

    @ApiOperation("删除物料信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "物料对象ID",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids){
        return ControllerUtil.returnCRUD(smtMaterialService.batchDelete(ids));
    }

    @ApiOperation("获取物料详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtMaterial> detail(@ApiParam(value = "物料ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long materialId){
        SmtMaterial smtMaterial = smtMaterialService.selectByKey(materialId);
        return  ControllerUtil.returnDataSuccess(smtMaterial,StringUtils.isEmpty(smtMaterial)?0:1);
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出物料excel",notes = "导出物料excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                              @RequestBody(required = false)SearchSmtMaterial searchSmtMaterial){
        List<SmtMaterialDto> list = smtMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtMaterial));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "物料信息表", "物料信息", SmtMaterialDto.class, "物料信息.xls", response);
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
            List<SmtMaterialDto> smtMaterialDtos = EasyPoiUtils.importExcel(file, SmtMaterialDto.class);
            Map<String, Object> resultMap = smtMaterialService.importExcel(smtMaterialDtos);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation(value = "获取物料履历列表",notes = "获取物料履历列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtMaterial searchSmtMaterial){
        Page<Object> page = PageHelper.startPage(searchSmtMaterial.getStartPage(),searchSmtMaterial.getPageSize());
        List<SmtHtMaterial> htMaterials = smtHtMaterialService.findHtList(searchSmtMaterial);
        return ControllerUtil.returnDataSuccess(htMaterials,(int)page.getTotal());
    }

    @ApiOperation("批量新增物料信息")
    @PostMapping("/addList")
    public ResponseEntity addList(@ApiParam(value = "物料信息集合")@RequestBody List<SmtMaterial> smtMaterials){
        return ControllerUtil.returnCRUD(smtMaterialService.batchSave(smtMaterials));
    }

    @ApiOperation("批量更新物料信息")
    @PostMapping("/batchUpdateByCode")
    public ResponseEntity batchUpdateByCode(@ApiParam(value = "物料信息集合",required = true)@RequestBody List<SmtMaterial> smtMaterials){
        return ControllerUtil.returnCRUD(smtMaterialService.batchUpdateByCode(smtMaterials));
    }

    @ApiOperation("批量更新物料信息")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "物料信息集合",required = true)@RequestBody List<SmtMaterial> smtMaterials){
        return ControllerUtil.returnCRUD(smtMaterialService.batchUpdate(smtMaterials));
    }


}
