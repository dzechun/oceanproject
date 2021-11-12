package com.fantechs.provider.esop.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopHtWorkInstructionDto;
import com.fantechs.common.base.general.dto.esop.EsopWorkInstructionDto;
import com.fantechs.common.base.general.dto.esop.EsopWorkInstructionTreeDto;
import com.fantechs.common.base.general.dto.esop.imports.EsopWorkInstructionImport;
import com.fantechs.common.base.general.entity.esop.EsopWorkInstruction;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWorkInstruction;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.esop.service.EsopHtWorkInstructionService;
import com.fantechs.provider.esop.service.EsopWorkInstructionService;
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
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */
@RestController
@Api(tags = "电子WI管理")
@RequestMapping("/esopWorkInstruction")
@Validated
@Slf4j
public class EsopWorkInstructionController {

    @Resource
    private EsopWorkInstructionService esopWorkInstructionService;
    @Resource
    private EsopHtWorkInstructionService esopHtWorkInstructionService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EsopWorkInstructionDto esopWorkInstructionDto) {
        return ControllerUtil.returnCRUD(esopWorkInstructionService.save(esopWorkInstructionDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(esopWorkInstructionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EsopWorkInstruction.update.class) EsopWorkInstructionDto esopWorkInstructionDto) {
        return ControllerUtil.returnCRUD(esopWorkInstructionService.update(esopWorkInstructionDto));
    }

   @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EsopWorkInstruction> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EsopWorkInstruction  esopWorkInstruction = esopWorkInstructionService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(esopWorkInstruction,StringUtils.isEmpty(esopWorkInstruction)?0:1);
    }

    @ApiOperation("设备ip获取详情")
    @PostMapping("/detailByEquipmentIp")
    public ResponseEntity<EsopWorkInstructionDto> detailByEquipmentIp(@ApiParam(value = "查询对象")@RequestBody SearchEsopWorkInstruction searchEsopWorkInstruction) {
        EsopWorkInstructionDto  esopWorkInstructionDto = esopWorkInstructionService.findByEquipmentIp(searchEsopWorkInstruction);
        return  ControllerUtil.returnDataSuccess(esopWorkInstructionDto,StringUtils.isEmpty(esopWorkInstructionDto)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EsopWorkInstructionDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEsopWorkInstruction searchEsopWorkInstruction) {
        Page<Object> page = PageHelper.startPage(searchEsopWorkInstruction.getStartPage(),searchEsopWorkInstruction.getPageSize());
        List<EsopWorkInstructionDto> list = esopWorkInstructionService.findList(searchEsopWorkInstruction);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EsopHtWorkInstructionDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEsopWorkInstruction searchEsopWorkInstruction) {
        Page<Object> page = PageHelper.startPage(searchEsopWorkInstruction.getStartPage(),searchEsopWorkInstruction.getPageSize());
        List<EsopHtWorkInstructionDto> list = esopHtWorkInstructionService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEsopWorkInstruction));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stresop")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEsopWorkInstruction searchEsopWorkInstruction){
    List<EsopWorkInstructionDto> list = esopWorkInstructionService.findList(searchEsopWorkInstruction);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EsopWorkInstruction信息", EsopWorkInstructionDto.class, "EsopWorkInstruction.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }


    /**
     * 从excel导入设备、物料数据
     * @return
     * @throws
     */
/*    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入WI信息",notes = "从excel导入WI信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            EsopWorkInstructionDto esopWorkInstructionDto = esopWorkInstructionService.importExcel(file);
          //  return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
            return ControllerUtil.returnDataSuccess(esopWorkInstructionDto,(int)0);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ControllerUtil.returnFail("导入失败", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }*/

    /**
     * 从excel导入数据
     *
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入作业指导书", notes = "从excel导入作业指导书")
    public ResponseEntity importExcel(@ApiParam(value = "输入excel文件", required = true)
                                      @RequestPart(value = "file") MultipartFile file) {
        try {
            // 导入操作
            List<EsopWorkInstructionImport> esopWorkInstructionImports = EasyPoiUtils.importExcel(file, 0, 1, EsopWorkInstructionImport.class);
            Map<String, Object> resultMap = esopWorkInstructionService.importExcel(esopWorkInstructionImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation("下载模板地址")
    @PostMapping("/download")
    public ResponseEntity downExcel(HttpServletResponse response)throws IOException {
        String fileUrl = esopWorkInstructionService.download(response);
        return ControllerUtil.returnDataSuccess(fileUrl,0);
    }


    @ApiOperation("审核")
    @PostMapping("/censor")
    public ResponseEntity censor(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EsopWorkInstruction.update.class) EsopWorkInstructionDto esopWorkInstructionDto) {
        return ControllerUtil.returnCRUD(esopWorkInstructionService.censor(esopWorkInstructionDto));
    }

    @ApiOperation("树状图列表")
    @PostMapping("/findTreeList")
    public ResponseEntity<List<EsopWorkInstructionTreeDto>> findTreeList(@ApiParam(value = "查询对象")@RequestBody SearchEsopWorkInstruction searchEsopWorkInstruction) {
        Page<Object> page = PageHelper.startPage(searchEsopWorkInstruction.getStartPage(),searchEsopWorkInstruction.getPageSize());
        List<EsopWorkInstructionTreeDto> list = esopWorkInstructionService.findTreeList(searchEsopWorkInstruction);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
