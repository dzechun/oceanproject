package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseSafeStockDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseSafeStockImport;
import com.fantechs.common.base.general.entity.basic.BaseSafeStock;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSafeStock;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseSafeStockService;
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
 * Created by mr.lei on 2021/03/04.
 */
@RestController
@Api(tags = "安全库存")
@RequestMapping("/baseSafeStock")
@Validated
@Slf4j
public class BaseSafeStockController {

    @Resource
    private BaseSafeStockService baseSafeStockService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSafeStock baseSafeStock) {
        return ControllerUtil.returnCRUD(baseSafeStockService.save(baseSafeStock));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSafeStockService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseSafeStock.update.class) BaseSafeStock baseSafeStock) {
        return ControllerUtil.returnCRUD(baseSafeStockService.update(baseSafeStock));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSafeStock> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSafeStock baseSafeStock = baseSafeStockService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSafeStock,StringUtils.isEmpty(baseSafeStock)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSafeStockDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSafeStock searchBaseSafeStock) {
        Page<Object> page = PageHelper.startPage(searchBaseSafeStock.getStartPage(), searchBaseSafeStock.getPageSize());
        List<BaseSafeStockDto> list = baseSafeStockService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSafeStock));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseSafeStockDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSafeStock searchBaseSafeStock) {
        Page<Object> page = PageHelper.startPage(searchBaseSafeStock.getStartPage(), searchBaseSafeStock.getPageSize());
        List<BaseSafeStockDto> list = baseSafeStockService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseSafeStock));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseSafeStock searchBaseSafeStock){
    List<BaseSafeStockDto> list = baseSafeStockService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSafeStock));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "安全库存信息", "安全库存信息.xls", response);
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
            List<BaseSafeStockImport> baseSafeStockImports = EasyPoiUtils.importExcel(file,2,1, BaseSafeStockImport.class);
            Map<String, Object> resultMap = baseSafeStockService.importExcel(baseSafeStockImports);
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

    @GetMapping("/inventeryWarning")
    @ApiOperation("预警")
    public ResponseEntity inventeryWarning(){
        return ControllerUtil.returnCRUD(baseSafeStockService.inventeryWarning());
    }
}
