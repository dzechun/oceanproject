package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.dto.eng.imports.EngPackingOrderSummaryDetImport;
import com.fantechs.common.base.general.entity.eng.EngPackingOrderSummaryDet;
import com.fantechs.common.base.general.entity.eng.search.SearchEngPackingOrderSummaryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.service.EngHtPackingOrderSummaryDetService;
import com.fantechs.provider.guest.eng.service.EngPackingOrderSummaryDetService;
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
@RequestMapping("/engPackingOrderSummaryDet")
@Slf4j
@Validated
public class EngPackingOrderSummaryDetController {

    @Resource
    private EngPackingOrderSummaryDetService engPackingOrderSummaryDetService;
    @Resource
    private EngHtPackingOrderSummaryDetService engHtPackingOrderSummaryDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EngPackingOrderSummaryDetDto engPackingOrderSummaryDetDto) {
        return ControllerUtil.returnCRUD(engPackingOrderSummaryDetService.save(engPackingOrderSummaryDetDto));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<EngPackingOrderSummaryDetDto> engPackingOrderSummaryDetDtos) {
        return ControllerUtil.returnCRUD(engPackingOrderSummaryDetService.batchAdd(engPackingOrderSummaryDetDtos));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(engPackingOrderSummaryDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= EngPackingOrderSummaryDet.update.class) EngPackingOrderSummaryDet engPackingOrderSummaryDet) {
        return ControllerUtil.returnCRUD(engPackingOrderSummaryDetService.update(engPackingOrderSummaryDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EngPackingOrderSummaryDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EngPackingOrderSummaryDet  engPackingOrderSummaryDet = engPackingOrderSummaryDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(engPackingOrderSummaryDet,StringUtils.isEmpty(engPackingOrderSummaryDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EngPackingOrderSummaryDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngPackingOrderSummaryDet searchEngPackingOrderSummaryDet) {
        Page<Object> page = PageHelper.startPage(searchEngPackingOrderSummaryDet.getStartPage(),searchEngPackingOrderSummaryDet.getPageSize());
        List<EngPackingOrderSummaryDetDto> list = engPackingOrderSummaryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrderSummaryDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findListByIds")
    public ResponseEntity<List<EngPackingOrderSummaryDetDto>> findListByIds(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        List<EngPackingOrderSummaryDetDto> list = engPackingOrderSummaryDetService.findListByIds(ids);
        return ControllerUtil.returnDataSuccess(list,0);
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EngHtPackingOrderSummaryDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEngPackingOrderSummaryDet searchEngPackingOrderSummaryDet) {
        Page<Object> page = PageHelper.startPage(searchEngPackingOrderSummaryDet.getStartPage(),searchEngPackingOrderSummaryDet.getPageSize());
        List<EngHtPackingOrderSummaryDetDto> list = engHtPackingOrderSummaryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrderSummaryDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEngPackingOrderSummaryDet searchEngPackingOrderSummaryDet){
    List<EngPackingOrderSummaryDetDto> list = engPackingOrderSummaryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchEngPackingOrderSummaryDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EngPackingOrderSummaryDet信息", EngPackingOrderSummaryDetDto.class, "EngPackingOrderSummaryDet.xls", response);
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
            List<EngPackingOrderSummaryDetImport> engPackingOrderSummaryDetImports = EasyPoiUtils.importExcel(file, 0, 1, EngPackingOrderSummaryDetImport.class);
            Map<String, Object> resultMap = engPackingOrderSummaryDetService.importExcel(engPackingOrderSummaryDetImports,packingOrderSummaryId);
            return ControllerUtil.returnDataSuccess("操作结果集", resultMap);
        }catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

}
