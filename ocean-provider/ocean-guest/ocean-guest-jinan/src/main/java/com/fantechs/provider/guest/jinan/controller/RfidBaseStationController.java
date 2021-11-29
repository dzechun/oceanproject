package com.fantechs.provider.guest.jinan.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.jinan.Import.RfidBaseStationImport;
import com.fantechs.common.base.general.entity.jinan.RfidBaseStation;
import com.fantechs.common.base.general.entity.jinan.history.RfidHtBaseStation;
import com.fantechs.common.base.general.entity.jinan.search.SearchRfidBaseStation;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.jinan.service.RfidBaseStationService;
import com.fantechs.provider.guest.jinan.service.RfidHtBaseStationService;
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
@Api(tags = "RFID基站信息管理")
@RequestMapping("/rfidBaseStation")
@Validated
@Slf4j
public class RfidBaseStationController {

    @Resource
    private RfidBaseStationService rfidBaseStationService;
    @Resource
    private RfidHtBaseStationService rfidHtBaseStationService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated RfidBaseStation rfidBaseStation) {
        return ControllerUtil.returnCRUD(rfidBaseStationService.save(rfidBaseStation));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(rfidBaseStationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=RfidBaseStation.update.class) RfidBaseStation rfidBaseStation) {
        return ControllerUtil.returnCRUD(rfidBaseStationService.update(rfidBaseStation));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<RfidBaseStation> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        RfidBaseStation  rfidBaseStation = rfidBaseStationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(rfidBaseStation,StringUtils.isEmpty(rfidBaseStation)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<RfidBaseStation>> findList(@ApiParam(value = "查询对象")@RequestBody SearchRfidBaseStation searchRfidBaseStation) {
        Page<Object> page = PageHelper.startPage(searchRfidBaseStation.getStartPage(),searchRfidBaseStation.getPageSize());
        List<RfidBaseStation> list = rfidBaseStationService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidBaseStation));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<RfidBaseStation>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchRfidBaseStation searchRfidBaseStation) {
        List<RfidBaseStation> list = rfidBaseStationService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidBaseStation));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<RfidHtBaseStation>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchRfidBaseStation searchRfidBaseStation) {
        Page<Object> page = PageHelper.startPage(searchRfidBaseStation.getStartPage(),searchRfidBaseStation.getPageSize());
        List<RfidHtBaseStation> list = rfidHtBaseStationService.findHtList(ControllerUtil.dynamicConditionByEntity(searchRfidBaseStation));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchRfidBaseStation searchRfidBaseStation){
    List<RfidBaseStation> list = rfidBaseStationService.findList(ControllerUtil.dynamicConditionByEntity(searchRfidBaseStation));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "基站信息", RfidBaseStation.class, "基站信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<RfidBaseStationImport> rfidBaseStationImports = EasyPoiUtils.importExcel(file, 2, 1, RfidBaseStationImport.class);
            Map<String, Object> resultMap = rfidBaseStationService.importExcel(rfidBaseStationImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }
}
