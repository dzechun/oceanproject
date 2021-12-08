package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.QmsBadnessManageBarcode;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsBadnessManageBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsBadnessManageBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@RestController
@Api(tags = "不良品处理条码")
@RequestMapping("/qmsBadnessManageBarcode")
@Validated
public class QmsBadnessManageBarcodeController {

    @Resource
    private QmsBadnessManageBarcodeService qmsBadnessManageBarcodeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsBadnessManageBarcode qmsBadnessManageBarcode) {
        return ControllerUtil.returnCRUD(qmsBadnessManageBarcodeService.save(qmsBadnessManageBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsBadnessManageBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsBadnessManageBarcode.update.class) QmsBadnessManageBarcode qmsBadnessManageBarcode) {
        return ControllerUtil.returnCRUD(qmsBadnessManageBarcodeService.update(qmsBadnessManageBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsBadnessManageBarcode> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsBadnessManageBarcode  qmsBadnessManageBarcode = qmsBadnessManageBarcodeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsBadnessManageBarcode,StringUtils.isEmpty(qmsBadnessManageBarcode)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsBadnessManageBarcode>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsBadnessManageBarcode searchQmsBadnessManageBarcode) {
        Page<Object> page = PageHelper.startPage(searchQmsBadnessManageBarcode.getStartPage(),searchQmsBadnessManageBarcode.getPageSize());
        List<QmsBadnessManageBarcode> list = qmsBadnessManageBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsBadnessManageBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<QmsBadnessManageBarcode>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchQmsBadnessManageBarcode searchQmsBadnessManageBarcode) {
        List<QmsBadnessManageBarcode> list = qmsBadnessManageBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsBadnessManageBarcode));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsBadnessManageBarcode searchQmsBadnessManageBarcode){
    List<QmsBadnessManageBarcode> list = qmsBadnessManageBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsBadnessManageBarcode));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "不良品处理条码", QmsBadnessManageBarcode.class, "不良品处理条码.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
