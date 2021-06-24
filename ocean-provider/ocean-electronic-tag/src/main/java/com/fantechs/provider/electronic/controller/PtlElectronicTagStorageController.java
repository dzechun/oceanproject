package com.fantechs.provider.electronic.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlElectronicTagStorageDto;
import com.fantechs.common.base.electronic.entity.PtlElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchPtlElectronicTagStorage;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.PtlElectronicTagStorageService;
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
 * Created by leifengzhi on 2020/11/17.
 */
@RestController
@Api(tags = "电子标签绑定储位")
@RequestMapping("/smtElectronicTagStorage")
@Validated
@Slf4j
public class PtlElectronicTagStorageController {

    @Autowired
    private PtlElectronicTagStorageService ptlElectronicTagStorageService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：storageId、equipmentId、electronicTagId、warehouseId、warehouseAreaId、materialId", required = true) @RequestBody @Validated PtlElectronicTagStorage ptlElectronicTagStorage) {
        return ControllerUtil.returnCRUD(ptlElectronicTagStorageService.save(ptlElectronicTagStorage));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ptlElectronicTagStorageService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = PtlElectronicTagStorage.update.class) PtlElectronicTagStorage ptlElectronicTagStorage) {
        return ControllerUtil.returnCRUD(ptlElectronicTagStorageService.update(ptlElectronicTagStorage));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<PtlElectronicTagStorage> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        PtlElectronicTagStorage ptlElectronicTagStorage = ptlElectronicTagStorageService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(ptlElectronicTagStorage, StringUtils.isEmpty(ptlElectronicTagStorage) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<PtlElectronicTagStorageDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchPtlElectronicTagStorage searchPtlElectronicTagStorage) {
        Page<Object> page = PageHelper.startPage(searchPtlElectronicTagStorage.getStartPage(), searchPtlElectronicTagStorage.getPageSize());
        List<PtlElectronicTagStorageDto> list = ptlElectronicTagStorageService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlElectronicTagStorage));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchPtlElectronicTagStorage searchPtlElectronicTagStorage) {
        List<PtlElectronicTagStorageDto> list = ptlElectronicTagStorageService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlElectronicTagStorage));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "SmtElectronicTagStorage信息", PtlElectronicTagStorageDto.class, "SmtElectronicTagStorage.xls", response);
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
    public ResponseEntity importElectronicTagController(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtos = EasyPoiUtils.importExcel(file, 1,1, PtlElectronicTagStorageDto.class);
            Map<String, Object> resultMap = ptlElectronicTagStorageService.importElectronicTagController(ptlElectronicTagStorageDtos);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

}
