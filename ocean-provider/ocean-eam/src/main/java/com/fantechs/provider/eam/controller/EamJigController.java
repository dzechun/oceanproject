package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigDto;
import com.fantechs.common.base.general.dto.eam.imports.EamJigImport;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.history.EamHtJig;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJig;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtJigService;
import com.fantechs.provider.eam.service.EamJigService;
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
 * Created by leifengzhi on 2021/07/28.
 */
@RestController
@Api(tags = "治具信息")
@RequestMapping("/eamJig")
@Validated
@Slf4j
public class EamJigController {

    @Resource
    private EamJigService eamJigService;
    @Resource
    private EamHtJigService eamHtJigService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJig eamJig) {
        return ControllerUtil.returnCRUD(eamJigService.save(eamJig));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJig.update.class) EamJig eamJig) {
        return ControllerUtil.returnCRUD(eamJigService.update(eamJig));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJig> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJig eamJig = eamJigService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJig,StringUtils.isEmpty(eamJig)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJig searchEamJig) {
        Page<Object> page = PageHelper.startPage(searchEamJig.getStartPage(),searchEamJig.getPageSize());
        List<EamJigDto> list = eamJigService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJig));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJig>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJig searchEamJig) {
        Page<Object> page = PageHelper.startPage(searchEamJig.getStartPage(),searchEamJig.getPageSize());
        List<EamHtJig> list = eamHtJigService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJig));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJig searchEamJig){
    List<EamJigDto> list = eamJigService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJig));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "治具信息", EamJigDto.class, "治具信息.xls", response);
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
    @ApiOperation(value = "从excel导入信息",notes = "从excel导入信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<EamJigImport> eamJigImports = EasyPoiUtils.importExcel(file, 2, 1, EamJigImport.class);
            Map<String, Object> resultMap = eamJigService.importExcel(eamJigImports);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        } catch (RuntimeException e) {
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
