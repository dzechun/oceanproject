package com.fantechs.provider.guest.jinan.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.jinan.Import.RfidAssetImport;
import com.fantechs.common.base.general.entity.jinan.RfidAsset;
import com.fantechs.common.base.general.entity.jinan.history.RfidHtAsset;
import com.fantechs.common.base.general.entity.jinan.search.SearchRfidAsset;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.service.RfidAssetService;
import com.fantechs.provider.guest.jinan.service.RfidHtAssetService;
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
 * Created by leifengzhi on 2021/11/29.
 */
@RestController
@Api(tags = "RFID资产管理")
@RequestMapping("/rfidAsset")
@Validated
@Slf4j
public class RfidAssetController {

    @Resource
    private RfidAssetService rfidAssetService;
    @Resource
    private RfidHtAssetService rfidHtAssetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated(value= RfidAsset.add.class) RfidAsset rfidAsset) {
        return ControllerUtil.returnCRUD(rfidAssetService.save(rfidAsset));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(rfidAssetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=RfidAsset.update.class) RfidAsset rfidAsset) {
        return ControllerUtil.returnCRUD(rfidAssetService.update(rfidAsset));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<RfidAsset> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        RfidAsset  rfidAsset = rfidAssetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(rfidAsset,StringUtils.isEmpty(rfidAsset)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<RfidAsset>> findList(@ApiParam(value = "查询对象")@RequestBody SearchRfidAsset searchRfidAsset) {
        Page<Object> page = PageHelper.startPage(searchRfidAsset.getStartPage(),searchRfidAsset.getPageSize());
        List<RfidAsset> list = rfidAssetService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidAsset));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<RfidAsset>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchRfidAsset searchRfidAsset) {
        List<RfidAsset> list = rfidAssetService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidAsset));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<RfidHtAsset>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchRfidAsset searchRfidAsset) {
        Page<Object> page = PageHelper.startPage(searchRfidAsset.getStartPage(),searchRfidAsset.getPageSize());
        List<RfidHtAsset> list = rfidHtAssetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchRfidAsset));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchRfidAsset searchRfidAsset){
    List<RfidAsset> list = rfidAssetService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidAsset));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "资产信息", RfidAsset.class, "资产信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<RfidAssetImport> rfidAssetImports = EasyPoiUtils.importExcel(file, 2, 1, RfidAssetImport.class);
            Map<String, Object> resultMap = rfidAssetService.importExcel(rfidAssetImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
