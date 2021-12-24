package com.fantechs.provider.lizi.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.provider.lizi.entity.dto.LiziScanBarcodeLogDto;
import com.fantechs.provider.lizi.entity.search.SearchLiziScanBarcodeLog;
import com.fantechs.provider.lizi.service.LiziScanBarcodeLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * Created by mr.lei on 2021/12/16.
 */
@RestController
@Api(tags = "栗子条码记录")
@RequestMapping("/liziScanBarcodeLog")
@Validated
public class LiziScanBarcodeLogController {

    @Resource
    private LiziScanBarcodeLogService liziScanBarcodeLogService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity<LiziScanBarcodeLogDto> add(@ApiParam(value = "必传：",required = true)@RequestParam String sn) {
        return ControllerUtil.returnDataSuccess(liziScanBarcodeLogService.add(sn),1);
    }

//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(liziScanBarcodeLogService.batchDelete(ids));
//    }
//
//    @ApiOperation("修改")
//    @PostMapping("/update")
//    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=LiziScanBarcodeLog.update.class) LiziScanBarcodeLog liziScanBarcodeLog) {
//        return ControllerUtil.returnCRUD(liziScanBarcodeLogService.update(liziScanBarcodeLog));
//    }
//
//    @ApiOperation("获取详情")
//    @PostMapping("/detail")
//    public ResponseEntity<LiziScanBarcodeLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
//        LiziScanBarcodeLog  liziScanBarcodeLog = liziScanBarcodeLogService.selectByKey(id);
//        return  ControllerUtil.returnDataSuccess(liziScanBarcodeLog,StringUtils.isEmpty(liziScanBarcodeLog)?0:1);
//    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<LiziScanBarcodeLogDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchLiziScanBarcodeLog searchLiziScanBarcodeLog) {
        Page<Object> page = PageHelper.startPage(searchLiziScanBarcodeLog.getStartPage(),searchLiziScanBarcodeLog.getPageSize());
        List<LiziScanBarcodeLogDto> list = liziScanBarcodeLogService.findList(ControllerUtil.dynamicConditionByEntity(searchLiziScanBarcodeLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<LiziScanBarcodeLogDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchLiziScanBarcodeLog searchLiziScanBarcodeLog) {
        List<LiziScanBarcodeLogDto> list = liziScanBarcodeLogService.findList(ControllerUtil.dynamicConditionByEntity(searchLiziScanBarcodeLog));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchLiziScanBarcodeLog searchLiziScanBarcodeLog){
    List<LiziScanBarcodeLogDto> list = liziScanBarcodeLogService.findList(ControllerUtil.dynamicConditionByEntity(searchLiziScanBarcodeLog));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "条码记录", "条码记录", LiziScanBarcodeLogDto.class, "条码记录.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
