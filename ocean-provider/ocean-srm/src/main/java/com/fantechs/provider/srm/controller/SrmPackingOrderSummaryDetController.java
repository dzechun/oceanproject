package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseFactoryImport;
import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.srm.SrmPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.srm.SrmPackingOrderSummaryDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmPackingOrderSummaryDetImport;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmPackingOrderSummaryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmHtPackingOrderSummaryDetService;
import com.fantechs.provider.srm.service.SrmPackingOrderSummaryDetService;
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
@Api(tags = "装箱清单详情子表")
@RequestMapping("/srmPackingOrderSummaryDet")
@Slf4j
@Validated
public class SrmPackingOrderSummaryDetController {

    @Resource
    private SrmPackingOrderSummaryDetService srmPackingOrderSummaryDetService;
    @Resource
    private SrmHtPackingOrderSummaryDetService srmHtPackingOrderSummaryDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmPackingOrderSummaryDetDto srmPackingOrderSummaryDetDto) {
        return ControllerUtil.returnCRUD(srmPackingOrderSummaryDetService.save(srmPackingOrderSummaryDetDto));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<SrmPackingOrderSummaryDetDto> srmPackingOrderSummaryDetDtos) {
        return ControllerUtil.returnCRUD(srmPackingOrderSummaryDetService.batchAdd(srmPackingOrderSummaryDetDtos));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmPackingOrderSummaryDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmPackingOrderSummaryDet.update.class) SrmPackingOrderSummaryDet srmPackingOrderSummaryDet) {
        return ControllerUtil.returnCRUD(srmPackingOrderSummaryDetService.update(srmPackingOrderSummaryDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmPackingOrderSummaryDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmPackingOrderSummaryDet  srmPackingOrderSummaryDet = srmPackingOrderSummaryDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmPackingOrderSummaryDet,StringUtils.isEmpty(srmPackingOrderSummaryDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmPackingOrderSummaryDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPackingOrderSummaryDet searchSrmPackingOrderSummaryDet) {
        Page<Object> page = PageHelper.startPage(searchSrmPackingOrderSummaryDet.getStartPage(),searchSrmPackingOrderSummaryDet.getPageSize());
        List<SrmPackingOrderSummaryDetDto> list = srmPackingOrderSummaryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPackingOrderSummaryDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmHtPackingOrderSummaryDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmPackingOrderSummaryDet searchSrmPackingOrderSummaryDet) {
        Page<Object> page = PageHelper.startPage(searchSrmPackingOrderSummaryDet.getStartPage(),searchSrmPackingOrderSummaryDet.getPageSize());
        List<SrmHtPackingOrderSummaryDetDto> list = srmHtPackingOrderSummaryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPackingOrderSummaryDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSrmPackingOrderSummaryDet searchSrmPackingOrderSummaryDet){
    List<SrmPackingOrderSummaryDetDto> list = srmPackingOrderSummaryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmPackingOrderSummaryDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SrmPackingOrderSummaryDet信息", SrmPackingOrderSummaryDetDto.class, "SrmPackingOrderSummaryDet.xls", response);
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
                                      @ApiParam(value = "装箱汇总ID",required = true)@RequestParam  @NotNull(message="装箱汇总ID不能为空") Long packingOrderSummaryId){
        try {
            // 导入操作
            List<SrmPackingOrderSummaryDetImport> srmPackingOrderSummaryDetImports = EasyPoiUtils.importExcel(file, 0, 1, SrmPackingOrderSummaryDetImport.class);
            Map<String, Object> resultMap = srmPackingOrderSummaryDetService.importExcel(srmPackingOrderSummaryDetImports,packingOrderSummaryId);
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
