package com.fantechs.provider.eng.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.eng.imports.EngPackingOrderSummaryImport;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummary;
import com.fantechs.common.base.general.entity.eng.search.SearchEngPackingOrderSummary;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eng.service.EngPackingOrderSummaryService;
import com.fantechs.provider.eng.service.EngHtPackingOrderSummaryService;
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
 * Created by leifengzhi on 2021/08/27.
 */
@RestController
@Api(tags = "装箱清单详情")
@RequestMapping("/engPackingOrderSummary")
@Slf4j
@Validated
public class EngPackingOrderSummaryController {

    @Resource
    private EngPackingOrderSummaryService engPackingOrderSummaryService;
    @Resource
    private EngHtPackingOrderSummaryService engHtPackingOrderSummaryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EngPackingOrderSummaryDto engPackingOrderSummaryDto) {
        return ControllerUtil.returnCRUD(engPackingOrderSummaryService.save(engPackingOrderSummaryDto));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<EngPackingOrderSummaryDto> engPackingOrderSummaryDtos) {
        return ControllerUtil.returnCRUD(engPackingOrderSummaryService.batchAdd(engPackingOrderSummaryDtos));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(engPackingOrderSummaryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= EngPackingOrderSummary.update.class) EngPackingOrderSummary engPackingOrderSummary) {
        return ControllerUtil.returnCRUD(engPackingOrderSummaryService.update(engPackingOrderSummary));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EngPackingOrderSummary> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EngPackingOrderSummary  engPackingOrderSummary = engPackingOrderSummaryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(engPackingOrderSummary,StringUtils.isEmpty(engPackingOrderSummary)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EngPackingOrderSummaryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngPackingOrderSummary searchEngPackingOrderSummary) {
        Page<Object> page = PageHelper.startPage(searchEngPackingOrderSummary.getStartPage(),searchEngPackingOrderSummary.getPageSize());
        List<EngPackingOrderSummaryDto> list = engPackingOrderSummaryService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrderSummary));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EngHtPackingOrderSummaryDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEngPackingOrderSummary searchEngPackingOrderSummary) {
        Page<Object> page = PageHelper.startPage(searchEngPackingOrderSummary.getStartPage(),searchEngPackingOrderSummary.getPageSize());
        List<EngHtPackingOrderSummaryDto> list = engHtPackingOrderSummaryService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrderSummary));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEngPackingOrderSummary searchEngPackingOrderSummary){
    List<EngPackingOrderSummaryDto> list = engPackingOrderSummaryService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrderSummary));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EngPackingOrderSummary信息", EngPackingOrderSummaryDto.class, "EngPackingOrderSummary.xls", response);
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
    @ApiOperation(value = "从excel导入装箱清单信息",notes = "从excel导入装箱清单信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file,
                                      @ApiParam(value = "装箱单ID",required = true)@RequestParam  @NotNull(message="装箱单ID不能为空") Long packingOrderId){
        try {
            // 导入操作
            List<EngPackingOrderSummaryImport> engPackingOrderSummaryImports = EasyPoiUtils.importExcel(file, 0, 1, EngPackingOrderSummaryImport.class);
            Map<String, Object> resultMap = engPackingOrderSummaryService.importExcel(engPackingOrderSummaryImports,packingOrderId);
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
