package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentJigDto;
import com.fantechs.common.base.general.dto.eam.imports.EamEquipmentJigImport;
import com.fantechs.common.base.general.entity.eam.EamEquipmentJig;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentJig;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentJig;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentJigService;
import com.fantechs.provider.eam.service.EamHtEquipmentJigService;
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

/**
 *
 * Created by leifengzhi on 2021/08/21.
 */
@RestController
@Api(tags = "设备绑定治具")
@RequestMapping("/eamEquipmentJig")
@Validated
@Slf4j
public class EamEquipmentJigController {

    @Resource
    private EamEquipmentJigService eamEquipmentJigService;
    @Resource
    private EamHtEquipmentJigService eamHtEquipmentJigService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentJigDto eamEquipmentJigDto) {
        return ControllerUtil.returnCRUD(eamEquipmentJigService.save(eamEquipmentJigDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentJigService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentJig.update.class) EamEquipmentJigDto eamEquipmentJigDto) {
        return ControllerUtil.returnCRUD(eamEquipmentJigService.update(eamEquipmentJigDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentJig> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentJig  eamEquipmentJig = eamEquipmentJigService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentJig,StringUtils.isEmpty(eamEquipmentJig)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentJigDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentJig searchEamEquipmentJig) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentJig.getStartPage(),searchEamEquipmentJig.getPageSize());
        List<EamEquipmentJigDto> list = eamEquipmentJigService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentJig));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentJig>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentJig searchEamEquipmentJig) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentJig.getStartPage(),searchEamEquipmentJig.getPageSize());
        List<EamHtEquipmentJig> list = eamHtEquipmentJigService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentJig));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentJig searchEamEquipmentJig){
    List<EamEquipmentJigDto> list = eamEquipmentJigService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentJig));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备绑定治具", EamEquipmentJigDto.class, "设备绑定治具.xls", response);
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
    @ApiOperation(value = "从excel导入设备绑定治具信息",notes = "从excel导入设备绑定治具信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<EamEquipmentJigImport> eamEquipmentJigImports = EasyPoiUtils.importExcel(file, 2, 1, EamEquipmentJigImport.class);
            Map<String, Object> resultMap = eamEquipmentJigService.importExcel(eamEquipmentJigImports);
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
