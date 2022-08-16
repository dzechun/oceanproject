package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsHtIpqcInspectionOrderService;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import feign.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */
@RestController
@Api(tags = "IPQC检验单")
@RequestMapping("/qmsIpqcInspectionOrder")
@Validated
public class QmsIpqcInspectionOrderController {

    @Resource
    private QmsIpqcInspectionOrderService qmsIpqcInspectionOrderService;
    @Resource
    private QmsHtIpqcInspectionOrderService qmsHtIpqcInspectionOrderService;

    @ApiOperation("PDA提交")
    @PostMapping("/PDASubmit")
    public ResponseEntity PDASubmit(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsIpqcInspectionOrder.update.class) QmsIpqcInspectionOrder qmsIpqcInspectionOrder) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderService.PDASubmit(qmsIpqcInspectionOrder));
    }

    @ApiOperation(value = "创建检验单据",notes = "创建检验单据")
    @PostMapping("/createOrder")
    public ResponseEntity<QmsIpqcInspectionOrder> createOrder(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder) {
        QmsIpqcInspectionOrder  ipqcInspectionOrder = qmsIpqcInspectionOrderService.createOrder(searchQmsIpqcInspectionOrder);
        return  ControllerUtil.returnDataSuccess(ipqcInspectionOrder,StringUtils.isEmpty(ipqcInspectionOrder)?0:1);
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsIpqcInspectionOrder qmsIpqcInspectionOrder) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderService.save(qmsIpqcInspectionOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsIpqcInspectionOrder.update.class) QmsIpqcInspectionOrder qmsIpqcInspectionOrder) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderService.update(qmsIpqcInspectionOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsIpqcInspectionOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsIpqcInspectionOrder  qmsIpqcInspectionOrder = qmsIpqcInspectionOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsIpqcInspectionOrder,StringUtils.isEmpty(qmsIpqcInspectionOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsIpqcInspectionOrder>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchQmsIpqcInspectionOrder.getStartPage(),searchQmsIpqcInspectionOrder.getPageSize());
        List<QmsIpqcInspectionOrder> list = qmsIpqcInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsHtIpqcInspectionOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchQmsIpqcInspectionOrder.getStartPage(),searchQmsIpqcInspectionOrder.getPageSize());
        List<QmsHtIpqcInspectionOrder> list = qmsHtIpqcInspectionOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("文件上传")
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@ApiParam(value = "文件必传",required = true) @RequestPart(value = "file") MultipartFile file) {
        String path = qmsIpqcInspectionOrderService.uploadFile(file);
        return  ControllerUtil.returnDataSuccess(path,1);
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder){
    List<QmsIpqcInspectionOrder> list = qmsIpqcInspectionOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "IPQC检验单", QmsIpqcInspectionOrder.class, "IPQC检验单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
