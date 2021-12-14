package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.qms.imports.QmsIncomingInspectionOrderImport;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIncomingInspectionOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderService;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */
@RestController
@Api(tags = "来料检验单")
@RequestMapping("/qmsIncomingInspectionOrder")
@Validated
@Slf4j
public class QmsIncomingInspectionOrderController {

    @Resource
    private QmsIncomingInspectionOrderService qmsIncomingInspectionOrderService;

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<QmsIncomingInspectionOrderDto> list) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderService.batchAdd(list));
    }

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "来料检验单ID列表，多个逗号分隔",required = true)@RequestParam  @NotBlank(message="来料检验单ID不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderService.pushDown(ids));
    }

    @ApiOperation(value = "MRB评审",notes = "MRB评审")
    @PostMapping("/MRBReview")
    public ResponseEntity MRBReview(@ApiParam(value = "来料检验单ID",required = true)@RequestParam  @NotNull(message="来料检验单ID不能为空") Long incomingInspectionOrderId,
                                    @ApiParam(value = "MRB评审结果",required = true)@RequestParam  @NotNull(message="MRB评审结果能为空") Byte mrbResult) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderService.MRBReview(incomingInspectionOrderId,mrbResult));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsIncomingInspectionOrderDto qmsIncomingInspectionOrder) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderService.save(qmsIncomingInspectionOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsIncomingInspectionOrder.update.class) QmsIncomingInspectionOrderDto qmsIncomingInspectionOrder) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderService.update(qmsIncomingInspectionOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsIncomingInspectionOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsIncomingInspectionOrder  qmsIncomingInspectionOrder = qmsIncomingInspectionOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsIncomingInspectionOrder,StringUtils.isEmpty(qmsIncomingInspectionOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsIncomingInspectionOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIncomingInspectionOrder searchQmsIncomingInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchQmsIncomingInspectionOrder.getStartPage(),searchQmsIncomingInspectionOrder.getPageSize());
        List<QmsIncomingInspectionOrderDto> list = qmsIncomingInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<QmsIncomingInspectionOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchQmsIncomingInspectionOrder searchQmsIncomingInspectionOrder) {
        List<QmsIncomingInspectionOrderDto> list = qmsIncomingInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsHtIncomingInspectionOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIncomingInspectionOrder searchQmsIncomingInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchQmsIncomingInspectionOrder.getStartPage(),searchQmsIncomingInspectionOrder.getPageSize());
        List<QmsHtIncomingInspectionOrder> list = qmsIncomingInspectionOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsIncomingInspectionOrder searchQmsIncomingInspectionOrder){
    List<QmsIncomingInspectionOrderDto> list = qmsIncomingInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "来料检验单", QmsIncomingInspectionOrderDto.class, "来料检验单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入",notes = "从excel导入")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<QmsIncomingInspectionOrderImport> qmsIncomingInspectionOrderImports = EasyPoiUtils.importExcel(file, 2, 1, QmsIncomingInspectionOrderImport.class);
            Map<String, Object> resultMap = qmsIncomingInspectionOrderService.importExcel(qmsIncomingInspectionOrderImports);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

    @ApiOperation("修改")
    @PostMapping("/updateIfAllIssued")
    public ResponseEntity updateIfAllIssued(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsIncomingInspectionOrder.update.class) QmsIncomingInspectionOrder qmsIncomingInspectionOrder) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderService.updateIfAllIssued(qmsIncomingInspectionOrder));
    }
}
