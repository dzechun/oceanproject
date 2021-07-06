package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.dto.eam.EamWorkInstructionDto;
import com.fantechs.common.base.general.entity.eam.EamWorkInstruction;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWorkInstruction;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamWorkInstructionService;
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
 * Created by leifengzhi on 2021/07/06.
 */
@RestController
@Api(tags = "电子WI管理")
@RequestMapping("/eamWorkInstruction")
@Validated
public class EamWorkInstructionController {

    @Resource
    private EamWorkInstructionService eamWorkInstructionService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamWorkInstruction eamWorkInstruction) {
        return ControllerUtil.returnCRUD(eamWorkInstructionService.save(eamWorkInstruction));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamWorkInstructionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamWorkInstruction.update.class) EamWorkInstruction eamWorkInstruction) {
        return ControllerUtil.returnCRUD(eamWorkInstructionService.update(eamWorkInstruction));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamWorkInstruction> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamWorkInstruction  eamWorkInstruction = eamWorkInstructionService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamWorkInstruction,StringUtils.isEmpty(eamWorkInstruction)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamWorkInstructionDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamWorkInstruction searchEamWorkInstruction) {
        Page<Object> page = PageHelper.startPage(searchEamWorkInstruction.getStartPage(),searchEamWorkInstruction.getPageSize());
        List<EamWorkInstructionDto> list = eamWorkInstructionService.findList(searchEamWorkInstruction);
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
            List<EamWorkInstructionDto> eamWorkInstructionDtos = EasyPoiUtils.importExcel(file, EamWorkInstructionDto.class);
            Map<String, Object> resultMap = eamWorkInstructionService.importExcel(eamWorkInstructionDtos.get(0));
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        } catch (RuntimeException e) {
            e.printStackTrace();
        //    log.error(e.getMessage());
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
         //   log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
