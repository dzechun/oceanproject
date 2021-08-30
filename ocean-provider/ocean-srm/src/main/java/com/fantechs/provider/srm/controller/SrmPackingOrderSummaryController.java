package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseFactoryImport;
import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.srm.SrmPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmPackingOrderSummaryImport;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummary;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummary;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmPackingOrderSummary;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmHtPackingOrderSummaryService;
import com.fantechs.provider.srm.service.SrmPackingOrderSummaryService;
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
@RequestMapping("/srmPackingOrderSummary")
@Slf4j
@Validated
public class SrmPackingOrderSummaryController {

    @Resource
    private SrmPackingOrderSummaryService srmPackingOrderSummaryService;
    @Resource
    private SrmHtPackingOrderSummaryService srmHtPackingOrderSummaryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmPackingOrderSummaryDto srmPackingOrderSummaryDto) {
        return ControllerUtil.returnCRUD(srmPackingOrderSummaryService.save(srmPackingOrderSummaryDto));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<SrmPackingOrderSummaryDto> srmPackingOrderSummaryDtos) {
        return ControllerUtil.returnCRUD(srmPackingOrderSummaryService.batchAdd(srmPackingOrderSummaryDtos));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmPackingOrderSummaryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmPackingOrderSummary.update.class) SrmPackingOrderSummary srmPackingOrderSummary) {
        return ControllerUtil.returnCRUD(srmPackingOrderSummaryService.update(srmPackingOrderSummary));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmPackingOrderSummary> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmPackingOrderSummary  srmPackingOrderSummary = srmPackingOrderSummaryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmPackingOrderSummary,StringUtils.isEmpty(srmPackingOrderSummary)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmPackingOrderSummaryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPackingOrderSummary searchSrmPackingOrderSummary) {
        Page<Object> page = PageHelper.startPage(searchSrmPackingOrderSummary.getStartPage(),searchSrmPackingOrderSummary.getPageSize());
        List<SrmPackingOrderSummaryDto> list = srmPackingOrderSummaryService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPackingOrderSummary));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmHtPackingOrderSummaryDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPackingOrderSummary searchSrmPackingOrderSummary) {
        Page<Object> page = PageHelper.startPage(searchSrmPackingOrderSummary.getStartPage(),searchSrmPackingOrderSummary.getPageSize());
        List<SrmHtPackingOrderSummaryDto> list = srmHtPackingOrderSummaryService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPackingOrderSummary));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSrmPackingOrderSummary searchSrmPackingOrderSummary){
    List<SrmPackingOrderSummaryDto> list = srmPackingOrderSummaryService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPackingOrderSummary));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SrmPackingOrderSummary信息", SrmPackingOrderSummaryDto.class, "SrmPackingOrderSummary.xls", response);
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
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<SrmPackingOrderSummaryImport> srmPackingOrderSummaryImports = EasyPoiUtils.importExcel(file, 0, 1, SrmPackingOrderSummaryImport.class);
            Map<String, Object> resultMap = srmPackingOrderSummaryService.importExcel(srmPackingOrderSummaryImports);
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
