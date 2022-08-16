package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseUnitPriceDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseUnitPriceImport;
import com.fantechs.common.base.general.entity.basic.BaseUnitPrice;
import com.fantechs.common.base.general.entity.basic.history.BaseHtUnitPrice;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseUnitPrice;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtUnitPriceService;
import com.fantechs.provider.base.service.BaseUnitPriceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * Created by leifengzhi on 2021/01/27.
 */
@RestController
@Api(tags = "单价信息管理")
@RequestMapping("/baseUnitPrice")
@Validated
@Slf4j
public class BaseUnitPriceController {

    @Resource
    private BaseUnitPriceService baseUnitPriceService;
    @Resource
    private BaseHtUnitPriceService baseHtUnitPriceService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialId、unitPrice、processId",required = true)@RequestBody @Validated BaseUnitPrice baseUnitPrice) {
        return ControllerUtil.returnCRUD(baseUnitPriceService.save(baseUnitPrice));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseUnitPriceService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseUnitPrice.update.class) BaseUnitPrice baseUnitPrice) {
        return ControllerUtil.returnCRUD(baseUnitPriceService.update(baseUnitPrice));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseUnitPrice> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseUnitPrice  baseUnitPrice = baseUnitPriceService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseUnitPrice,StringUtils.isEmpty(baseUnitPrice)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseUnitPriceDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseUnitPrice searchBaseUnitPrice) {
        Page<Object> page = PageHelper.startPage(searchBaseUnitPrice.getStartPage(),searchBaseUnitPrice.getPageSize());
        List<BaseUnitPriceDto> list = baseUnitPriceService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseUnitPrice));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtUnitPrice>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseUnitPrice searchBaseUnitPrice) {
        Page<Object> page = PageHelper.startPage(searchBaseUnitPrice.getStartPage(),searchBaseUnitPrice.getPageSize());
        List<BaseHtUnitPrice> list = baseHtUnitPriceService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseUnitPrice));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseUnitPrice searchBaseUnitPrice){
    List<BaseUnitPriceDto> list = baseUnitPriceService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseUnitPrice));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BaseUnitPrice信息", BaseUnitPriceDto.class, "BaseUnitPrice.xls", response);
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
    @ApiOperation(value = "从excel导入单价信息",notes = "从excel导入单价信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseUnitPriceImport> baseUnitPriceImports = EasyPoiUtils.importExcel(file, 2, 1, BaseUnitPriceImport.class);
            Map<String, Object> resultMap = baseUnitPriceService.importExcel(baseUnitPriceImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

}
