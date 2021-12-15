package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSelectToUseBarcodeDto;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSelectToUseSubmitDto;
import com.fantechs.common.base.general.entity.qms.QmsBadnessManage;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsBadnessManage;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsBadnessManageService;
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
@Api(tags = "不良品处理")
@RequestMapping("/qmsBadnessManage")
@Validated
public class QmsBadnessManageController {

    @Resource
    private QmsBadnessManageService qmsBadnessManageService;


    @ApiOperation(value = "PDA条码校验",notes = "PDA条码校验")
    @PostMapping("/checkBarcode")
    public ResponseEntity<PdaIncomingSelectToUseBarcodeDto> checkBarcode(@ApiParam(value = "条码",required = true)@RequestParam  @NotBlank(message="条码不能为空") String barcode,
                                                                         @ApiParam(value = "检验单id",required = true)@RequestParam  @NotNull(message="检验单id不能为空") Long incomingInspectionOrderId) {
        PdaIncomingSelectToUseBarcodeDto pdaIncomingSelectToUseBarcodeDto = qmsBadnessManageService.checkBarcode(barcode,incomingInspectionOrderId);
        return ControllerUtil.returnDataSuccess(pdaIncomingSelectToUseBarcodeDto,StringUtils.isEmpty(pdaIncomingSelectToUseBarcodeDto)?0:1);
    }

    @ApiOperation(value = "PDA挑选使用提交",notes = "PDA挑选使用提交")
    @PostMapping("/submit")
    public ResponseEntity submit(@ApiParam(value = "必传：",required = true)@RequestBody @Validated PdaIncomingSelectToUseSubmitDto pdaIncomingSelectToUseSubmitDto) {
        return ControllerUtil.returnCRUD(qmsBadnessManageService.submit(pdaIncomingSelectToUseSubmitDto));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsBadnessManage qmsBadnessManage) {
        return ControllerUtil.returnCRUD(qmsBadnessManageService.save(qmsBadnessManage));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsBadnessManageService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsBadnessManage.update.class) QmsBadnessManage qmsBadnessManage) {
        return ControllerUtil.returnCRUD(qmsBadnessManageService.update(qmsBadnessManage));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsBadnessManage> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsBadnessManage  qmsBadnessManage = qmsBadnessManageService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsBadnessManage,StringUtils.isEmpty(qmsBadnessManage)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsBadnessManage>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsBadnessManage searchQmsBadnessManage) {
        Page<Object> page = PageHelper.startPage(searchQmsBadnessManage.getStartPage(),searchQmsBadnessManage.getPageSize());
        List<QmsBadnessManage> list = qmsBadnessManageService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsBadnessManage));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<QmsBadnessManage>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchQmsBadnessManage searchQmsBadnessManage) {
        List<QmsBadnessManage> list = qmsBadnessManageService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsBadnessManage));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsBadnessManage searchQmsBadnessManage){
    List<QmsBadnessManage> list = qmsBadnessManageService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsBadnessManage));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "不良品处理", QmsBadnessManage.class, "不良品处理.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
