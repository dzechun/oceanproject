package com.fantechs.provider.base.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.imports.BaseWarehouseImport;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.base.service.BaseHtWarehouseService;
import com.fantechs.provider.base.service.BaseWarehouseService;
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
 * Created by wcz on 2020/09/23.
 */
@RestController
@Api(tags = "仓库信息管理")
@RequestMapping("/baseWarehouse")
@Validated
@Slf4j
public class BaseWarehouseController {

    @Resource
    private BaseWarehouseService baseWarehouseService;

    @Resource
    private BaseHtWarehouseService baseHtWarehouseService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：warehouseCode、warehouseName",required = true)@RequestBody @Validated BaseWarehouse baseWarehouse) {
        return ControllerUtil.returnCRUD(baseWarehouseService.save(baseWarehouse));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseWarehouseService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = BaseWarehouse.update.class) BaseWarehouse baseWarehouse) {
        return ControllerUtil.returnCRUD(baseWarehouseService.update(baseWarehouse));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseWarehouse> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseWarehouse warehouse = baseWarehouseService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(warehouse,StringUtils.isEmpty(warehouse)?0:1);
    }

    @ApiOperation("根据条件查询信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseWarehouse>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWarehouse searchBaseWarehouse) {
        Page<Object> page = PageHelper.startPage(searchBaseWarehouse.getStartPage(), searchBaseWarehouse.getPageSize());
        List<BaseWarehouse> list = baseWarehouseService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWarehouse));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("根据条件查询信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtWarehouse>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWarehouse searchBaseWarehouse) {
        Page<Object> page = PageHelper.startPage(searchBaseWarehouse.getStartPage(), searchBaseWarehouse.getPageSize());
        List<BaseHtWarehouse> list = baseHtWarehouseService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseWarehouse));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出仓库信息excel",notes = "导出仓库信息excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                              @RequestBody(required = false) SearchBaseWarehouse searchBaseWarehouse){
        List<BaseWarehouse> list = baseWarehouseService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWarehouse));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        try {
            // 自定义导出操作
            EasyPoiUtils.customExportExcel(list, customExportParamList, "导出仓库信息", "仓库信息", "仓库信息.xls", response);
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
            List<BaseWarehouseImport> baseWarehouseImports = EasyPoiUtils.importExcel(file, 2, 1, BaseWarehouseImport.class);
            Map<String, Object> resultMap = baseWarehouseService.importExcel(baseWarehouseImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation("根据仓库编码批量更新")
    @PostMapping("/batchUpdateByCode")
    public ResponseEntity batchUpdateWarehouseByCode(@ApiParam(value = "编码必传")@RequestBody List<BaseWarehouse> baseWarehouses) {
         return ControllerUtil.returnDataSuccess("", baseWarehouseService.batchUpdateByCode(baseWarehouses));
    }

    @ApiOperation("批量新增")
    @PostMapping("/batchSave")
    public ResponseEntity batchSave(@ApiParam(value = "批量新增")@RequestBody List<BaseWarehouse> baseWarehouses) {
        return ControllerUtil.returnDataSuccess("", baseWarehouseService.batchSave(baseWarehouses));
    }

}
