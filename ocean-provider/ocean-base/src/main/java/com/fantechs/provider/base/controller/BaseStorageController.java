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
@Api(tags = "??????????????????")
@RequestMapping("/baseStorage")
@Validated
@Slf4j
public class BaseStorageController {

    @Resource
    private BaseStorageService baseStorageService;

    @Resource
    private BaseHtStorageService baseHtStorageService;

    @ApiOperation(value = "????????????", notes = "????????????")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "????????????", required = true) @RequestBody List<BaseStorage> baseStorages) {
        return ControllerUtil.returnCRUD(baseStorageService.batchUpdate(baseStorages));
    }

    @ApiOperation(value = "????????????", notes = "????????????")
    @PostMapping("/batchSave")
    public ResponseEntity batchAdd(@ApiParam(value = "????????????", required = true) @RequestBody List<BaseStorage> baseStorages) {
        return ControllerUtil.returnCRUD(baseStorageService.batchSave(baseStorages));
    }


    @ApiOperation(value = "??????", notes = "??????")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "?????????storageCode???storageName", required = true) @RequestBody @Validated BaseStorage storage) {
        return ControllerUtil.returnCRUD(baseStorageService.save(storage));
    }

    @ApiOperation("??????")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "??????ID???????????????????????????", required = true) @RequestParam @NotBlank(message = "ids????????????") String ids) {
        return ControllerUtil.returnCRUD(baseStorageService.batchDelete(ids));
    }

    @ApiOperation("??????")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "?????????Id??????", required = true) @RequestBody @Validated(value = BaseStorage.update.class) BaseStorage storage) {
        return ControllerUtil.returnCRUD(baseStorageService.update(storage));
    }

    @ApiOperation("????????????")
    @PostMapping("/detail")
    public ResponseEntity<BaseStorage> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id????????????") Long id) {
        BaseStorage storage = baseStorageService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(storage, StringUtils.isEmpty(storage) ? 0 : 1);
    }

    @ApiOperation("??????????????????????????????")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseStorage>> findList(@ApiParam(value = "????????????") @RequestBody SearchBaseStorage searchBaseStorage) {
        Page<Object> page = PageHelper.startPage(searchBaseStorage.getStartPage(), searchBaseStorage.getPageSize());
        List<BaseStorage> list = baseStorageService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorage));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("????????????????????????????????????")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtStorage>> findHtList(@ApiParam(value = "????????????") @RequestBody SearchBaseStorage searchBaseStorage) {
        Page<Object> page = PageHelper.startPage(searchBaseStorage.getStartPage(), searchBaseStorage.getPageSize());
        List<BaseHtStorage> list = baseHtStorageService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseStorage));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    /**
     * ????????????
     *
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "??????????????????excel", notes = "??????????????????excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "????????????")
    @RequestBody(required = false) SearchBaseStorage searchBaseStorage) {
        List<BaseStorage> list = baseStorageService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorage));
        try {
            // ????????????
            EasyPoiUtils.exportExcel(list, "??????????????????", "????????????", BaseStorage.class, "????????????.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    /**
     * ???excel????????????
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "???excel??????????????????",notes = "???excel??????????????????")
    public ResponseEntity importExcel(@ApiParam(value ="??????excel??????",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // ????????????
            List<BaseStorageImport> baseStorageImports = EasyPoiUtils.importExcel(file, 2, 1, BaseStorageImport.class);
            Map<String, Object> resultMap = baseStorageService.importExcel(baseStorageImports);
            return ControllerUtil.returnDataSuccess("???????????????", resultMap);
        }catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail("??????????????????", ErrorCodeEnum.OPT20012002.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation("??????????????????")
    @PostMapping("/minusSurplusCanPutSalver")
    public ResponseEntity minusSurplusCanPutSalver(@ApiParam(value = "??????id", required = true) @RequestParam @NotNull(message = "??????id")Long storageId,
                                                            @ApiParam(value = "?????????????????????", required = true) @RequestParam @NotNull(message = "?????????????????????????????????") Integer num) {
        return ControllerUtil.returnCRUD(baseStorageService.minusSurplusCanPutSalver(storageId,num));
    }

    @ApiOperation("??????????????????")
    @PostMapping("/plusSurplusCanPutSalver")
    public ResponseEntity plusSurplusCanPutSalver(@ApiParam(value = "??????id", required = true) @RequestParam @NotNull(message = "??????id")Long storageId,
                                                   @ApiParam(value = "?????????????????????", required = true) @RequestParam @NotNull(message = "?????????????????????????????????") Integer num) {
        return ControllerUtil.returnCRUD(baseStorageService.plusSurplusCanPutSalver(storageId,num));
    }

    @ApiOperation("??????????????????")
    @PostMapping("/JobRule")
    public ResponseEntity<List<StorageRuleDto>> JobRule(@RequestBody JobRuleDto jobRuleDto){
        List<StorageRuleDto> list = StorageDistributionRuleUtils.JobMainRule(jobRuleDto.getPackageQty(), jobRuleDto.getWarehouseId(),jobRuleDto.getMaterialId(),StringUtils.isEmpty(jobRuleDto.getBatchCode())?null:jobRuleDto.getBatchCode(),StringUtils.isEmpty(jobRuleDto.getProDate())?null:jobRuleDto.getProDate());
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation(value = "?????????????????????",notes = "?????????????????????")
    @PostMapping("/saveByApi")
    public ResponseEntity saveByApi(@ApiParam(value = "?????????storageCode",required = true)@RequestBody @Validated BaseStorage baseStorage) {
        return ControllerUtil.returnCRUD(baseStorageService.saveByApi(baseStorage));
    }

    @ApiOperation("????????????")
    @PostMapping("/printStorageCode")
    public ResponseEntity printStorageCode(@RequestBody List<PrintBaseStorageCode> printBaseStorageCodes){
        return ControllerUtil.returnCRUD(baseStorageService.printStorageCode(printBaseStorageCodes));
    }
}
