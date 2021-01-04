package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsRejectsMrbReviewDto;
import com.fantechs.common.base.general.entity.qms.QmsRejectsMrbReview;
import com.fantechs.common.base.general.entity.qms.history.QmsHtRejectsMrbReview;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsRejectsMrbReview;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsHtRejectsMrbReviewService;
import com.fantechs.provider.qms.service.QmsRejectsMrbReviewService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/28.
 */
@RestController
@Api(tags = "不良品MRB评审")
@RequestMapping("/qmsRejectsMrbReview")
@Validated
public class QmsRejectsMrbReviewController {

    @Autowired
    private QmsRejectsMrbReviewService qmsRejectsMrbReviewService;
    @Autowired
    private QmsHtRejectsMrbReviewService qmsHtRejectsMrbReviewService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsRejectsMrbReview qmsRejectsMrbReview) {
        return ControllerUtil.returnCRUD(qmsRejectsMrbReviewService.save(qmsRejectsMrbReview));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsRejectsMrbReviewService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsRejectsMrbReview.update.class) QmsRejectsMrbReview qmsRejectsMrbReview) {
        return ControllerUtil.returnCRUD(qmsRejectsMrbReviewService.update(qmsRejectsMrbReview));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsRejectsMrbReview> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsRejectsMrbReview  qmsRejectsMrbReview = qmsRejectsMrbReviewService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsRejectsMrbReview,StringUtils.isEmpty(qmsRejectsMrbReview)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsRejectsMrbReviewDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsRejectsMrbReview searchQmsRejectsMrbReview) {
        Page<Object> page = PageHelper.startPage(searchQmsRejectsMrbReview.getStartPage(),searchQmsRejectsMrbReview.getPageSize());
        List<QmsRejectsMrbReviewDto> list = qmsRejectsMrbReviewService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsRejectsMrbReview));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsHtRejectsMrbReview>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsRejectsMrbReview searchQmsRejectsMrbReview) {
        Page<Object> page = PageHelper.startPage(searchQmsRejectsMrbReview.getStartPage(),searchQmsRejectsMrbReview.getPageSize());
        List<QmsHtRejectsMrbReview> list = qmsHtRejectsMrbReviewService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsRejectsMrbReview));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsRejectsMrbReview searchQmsRejectsMrbReview){
    List<QmsRejectsMrbReviewDto> list = qmsRejectsMrbReviewService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsRejectsMrbReview));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "不良品MRB评审导出信息", "不良品MRB评审信息", QmsRejectsMrbReview.class, "不良品MRB评审.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
