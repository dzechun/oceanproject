package com.fantechs.provider.base.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.JobRuleDto;
import com.fantechs.common.base.general.dto.basic.PrintBaseStorageCode;
import com.fantechs.common.base.general.dto.basic.StorageRuleDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseFactoryImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageImport;
import com.fantechs.common.base.general.entity.basic.BaseCustomer;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtStorageService;
import com.fantechs.provider.base.service.BaseStorageService;
import com.fantechs.provider.base.util.StorageDistributionRuleUtils;
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
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by wcz on 2020/09/23.
 */
@RestController
@Api(tags = "库位信息管理")
@RequestMapping("/baseStorage")
@Validated
@Slf4j
public class BaseStorageController {

    @Resource
    private BaseStorageService baseStorageService;

    @Resource
    private BaseHtStorageService baseHtStorageService;

    @ApiOperation(value = "批量更新", notes = "批量更新")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "库位集合", required = true) @RequestBody List<BaseStorage> baseStorages) {
        return ControllerUtil.returnCRUD(baseStorageService.batchUpdate(baseStorages));
    }

    @ApiOperation(value = "批量新增", notes = "批量新增")
    @PostMapping("/batchSave")
    public ResponseEntity batchAdd(@ApiParam(value = "库位集合", required = true) @RequestBody List<BaseStorage> baseStorages) {
        return ControllerUtil.returnCRUD(baseStorageService.batchSave(baseStorages));
    }


    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：storageCode、storageName", required = true) @RequestBody @Validated BaseStorage storage) {
        return ControllerUtil.returnCRUD(baseStorageService.save(storage));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseStorageService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BaseStorage.update.class) BaseStorage storage) {
        return ControllerUtil.returnCRUD(baseStorageService.update(storage));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseStorage> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseStorage storage = baseStorageService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(storage, StringUtils.isEmpty(storage) ? 0 : 1);
    }

    @ApiOperation("根据条件查询信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseStorage>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseStorage searchBaseStorage) {
        Page<Object> page = PageHelper.startPage(searchBaseStorage.getStartPage(), searchBaseStorage.getPageSize());
        List<BaseStorage> list = baseStorageService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorage));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("根据条件查询信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtStorage>> findHtList(@ApiParam(value = "查询对象") @RequestBody SearchBaseStorage searchBaseStorage) {
        Page<Object> page = PageHelper.startPage(searchBaseStorage.getStartPage(), searchBaseStorage.getPageSize());
        List<BaseHtStorage> list = baseHtStorageService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseStorage));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    /**
     * 导出数据
     *
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出库位信息excel", notes = "导出库位信息excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseStorage searchBaseStorage) {
        List<BaseStorage> list = baseStorageService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorage));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出库位信息", "库位信息", BaseStorage.class, "库位信息.xls", response);
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
    @ApiOperation(value = "从excel导入厂别信息",notes = "从excel导入厂别信息")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<BaseStorageImport> baseStorageImports = EasyPoiUtils.importExcel(file, 2, 1, BaseStorageImport.class);
            Map<String, Object> resultMap = baseStorageService.importExcel(baseStorageImports);
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

    @ApiOperation("减可放托盘数")
    @PostMapping("/minusSurplusCanPutSalver")
    public ResponseEntity minusSurplusCanPutSalver(@ApiParam(value = "库位id", required = true) @RequestParam @NotNull(message = "库位id")Long storageId,
                                                            @ApiParam(value = "待减可放托盘数", required = true) @RequestParam @NotNull(message = "待减可放托盘数不能为空") Integer num) {
        return ControllerUtil.returnCRUD(baseStorageService.minusSurplusCanPutSalver(storageId,num));
    }

    @ApiOperation("加可放托盘数")
    @PostMapping("/plusSurplusCanPutSalver")
    public ResponseEntity plusSurplusCanPutSalver(@ApiParam(value = "库位id", required = true) @RequestParam @NotNull(message = "库位id")Long storageId,
                                                   @ApiParam(value = "待加可放托盘数", required = true) @RequestParam @NotNull(message = "待减可放托盘数不能为空") Integer num) {
        return ControllerUtil.returnCRUD(baseStorageService.plusSurplusCanPutSalver(storageId,num));
    }

    @ApiOperation("上架分配规则")
    @PostMapping("/JobRule")
    public ResponseEntity<List<StorageRuleDto>> JobRule(@RequestBody JobRuleDto jobRuleDto){
        List<StorageRuleDto> list = StorageDistributionRuleUtils.JobMainRule(jobRuleDto.getPackageQty(), jobRuleDto.getWarehouseId(),jobRuleDto.getMaterialId(),StringUtils.isEmpty(jobRuleDto.getBatchCode())?null:jobRuleDto.getBatchCode(),StringUtils.isEmpty(jobRuleDto.getProDate())?null:jobRuleDto.getProDate());
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity saveByApi(@ApiParam(value = "必传：storageCode",required = true)@RequestBody @Validated BaseStorage baseStorage) {
        return ControllerUtil.returnCRUD(baseStorageService.saveByApi(baseStorage));
    }

    @ApiOperation("库位打印")
    @PostMapping("/printStorageCode")
    public ResponseEntity printStorageCode(@RequestBody List<PrintBaseStorageCode> printBaseStorageCodes){
        return ControllerUtil.returnCRUD(baseStorageService.printStorageCode(printBaseStorageCodes));
    }
}
