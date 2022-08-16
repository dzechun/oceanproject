package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseCurrencyDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseCurrencyImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseProcessCategoryImport;
import com.fantechs.common.base.general.entity.basic.BaseCurrency;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseCurrency;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseCurrencyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/13.
 */
@RestController
@Api(tags = "币别信息管理")
@RequestMapping("/baseCurrency")
@Validated
@Slf4j
public class BaseCurrencyController {

    @Autowired
    private BaseCurrencyService baseCurrencyService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：currencyCode,currencyName",required = true)@RequestBody @Validated BaseCurrency baseCurrency) {
        return ControllerUtil.returnCRUD(baseCurrencyService.save(baseCurrency));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseCurrencyService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseCurrency.update.class) BaseCurrency baseCurrency) {
        return ControllerUtil.returnCRUD(baseCurrencyService.update(baseCurrency));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseCurrency> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseCurrency baseCurrency = baseCurrencyService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseCurrency,StringUtils.isEmpty(baseCurrency)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseCurrencyDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseCurrency searchBaseCurrency) {
        Page<Object> page = PageHelper.startPage(searchBaseCurrency.getStartPage(), searchBaseCurrency.getPageSize());
        List<BaseCurrencyDto> list = baseCurrencyService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseCurrency));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseCurrency searchBaseCurrency){
    List<BaseCurrencyDto> list = baseCurrencyService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseCurrency));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtCurrency信息", BaseCurrencyDto.class, "SmtCurrency.xls", response);
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
    @ApiOperation(value = "从excel导入电子标签信息",notes = "从excel导入电子标签信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseCurrencyImport> baseCurrencyImports = EasyPoiUtils.importExcel(file,2,1, BaseCurrencyImport.class);
            Map<String, Object> resultMap = baseCurrencyService.importExcel(baseCurrencyImports);
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
