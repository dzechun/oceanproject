package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.eam.EamHtWiReleaseDto;
import com.fantechs.common.base.general.dto.eam.EamHtWorkInstructionDto;
import com.fantechs.common.base.general.dto.eam.EamWorkInstructionDto;
import com.fantechs.common.base.general.entity.eam.EamWiBom;
import com.fantechs.common.base.general.entity.eam.EamWiQualityStandards;
import com.fantechs.common.base.general.entity.eam.EamWorkInstruction;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWiRelease;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWorkInstruction;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.fileserver.service.FileFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.eam.service.EamHtWorkInstructionService;
import com.fantechs.provider.eam.service.EamWorkInstructionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */
@RestController
@Api(tags = "电子WI管理")
@RequestMapping("/eamWorkInstruction")
@Validated
public class EamWorkInstructionController {

    @Resource
    private EamWorkInstructionService eamWorkInstructionService;
    @Resource
    private EamHtWorkInstructionService eamHtWorkInstructionService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamWorkInstructionDto eamWorkInstructionDto) {
        return ControllerUtil.returnCRUD(eamWorkInstructionService.save(eamWorkInstructionDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamWorkInstructionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamWorkInstruction.update.class) EamWorkInstructionDto eamWorkInstructionDto) {
        return ControllerUtil.returnCRUD(eamWorkInstructionService.update(eamWorkInstructionDto));
    }

   @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamWorkInstruction> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamWorkInstruction  eamWorkInstruction = eamWorkInstructionService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamWorkInstruction,StringUtils.isEmpty(eamWorkInstruction)?0:1);
    }

    @ApiOperation("设备ip获取详情")
    @PostMapping("/detailByEquipmentIp")
    public ResponseEntity<EamWorkInstructionDto> detailByEquipmentIp(@ApiParam(value = "查询对象")@RequestBody SearchEamWorkInstruction searchEamWorkInstruction) {
        EamWorkInstructionDto  eamWorkInstructionDto = eamWorkInstructionService.findByEquipmentIp(searchEamWorkInstruction);
        return  ControllerUtil.returnDataSuccess(eamWorkInstructionDto,StringUtils.isEmpty(eamWorkInstructionDto)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamWorkInstructionDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamWorkInstruction searchEamWorkInstruction) {
        Page<Object> page = PageHelper.startPage(searchEamWorkInstruction.getStartPage(),searchEamWorkInstruction.getPageSize());
        List<EamWorkInstructionDto> list = eamWorkInstructionService.findList(searchEamWorkInstruction);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtWorkInstructionDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamWorkInstruction searchEamWorkInstruction) {
        Page<Object> page = PageHelper.startPage(searchEamWorkInstruction.getStartPage(),searchEamWorkInstruction.getPageSize());
        List<EamHtWorkInstructionDto> list = eamHtWorkInstructionService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamWorkInstruction));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamWorkInstruction searchEamWorkInstruction){
    List<EamWorkInstructionDto> list = eamWorkInstructionService.findList(searchEamWorkInstruction);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EamWorkInstruction信息", EamWorkInstructionDto.class, "EamWorkInstruction.xls", response);
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
    @ApiOperation(value = "从excel导入WI信息",notes = "从excel导入WI信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            EamWorkInstructionDto eamWorkInstructionDto = eamWorkInstructionService.importExcel(file);
          //  return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
            return ControllerUtil.returnDataSuccess(eamWorkInstructionDto,(int)0);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ControllerUtil.returnFail("导入失败", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }


    @ApiOperation("下载模板地址")
    @PostMapping("/download")
    public ResponseEntity downExcel(HttpServletResponse response)throws IOException {
        String fileUrl = eamWorkInstructionService.download(response);
        return ControllerUtil.returnDataSuccess(fileUrl,0);
    }


    @ApiOperation("审核")
    @PostMapping("/censor")
    public ResponseEntity censor(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamWorkInstruction.update.class) EamWorkInstructionDto eamWorkInstructionDto) {
        return ControllerUtil.returnCRUD(eamWorkInstructionService.censor(eamWorkInstructionDto));
    }

}
