package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsMrbReviewDto;
import com.fantechs.common.base.general.entity.qms.QmsMrbReview;
import com.fantechs.common.base.general.entity.qms.history.QmsHtMrbReview;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsMrbReview;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsHtMrbReviewService;
import com.fantechs.provider.qms.service.QmsMrbReviewService;
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
 * Created by leifengzhi on 2020/12/24.
 */
@RestController
@Api(tags = "MRB评审")
@RequestMapping("/qmsMrbReview")
@Validated
public class QmsMrbReviewController {

    @Autowired
    private QmsMrbReviewService qmsMrbReviewService;
    @Autowired
    private QmsHtMrbReviewService qmsHtMrbReview;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsMrbReview qmsMrbReview) {
        return ControllerUtil.returnCRUD(qmsMrbReviewService.save(qmsMrbReview));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsMrbReviewService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsMrbReview.update.class) QmsMrbReview qmsMrbReview) {
        return ControllerUtil.returnCRUD(qmsMrbReviewService.update(qmsMrbReview));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsMrbReview> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsMrbReview  qmsMrbReview = qmsMrbReviewService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsMrbReview,StringUtils.isEmpty(qmsMrbReview)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsMrbReviewDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsMrbReview searchQmsMrbReview) {
        Page<Object> page = PageHelper.startPage(searchQmsMrbReview.getStartPage(),searchQmsMrbReview.getPageSize());
        System.out.println(searchQmsMrbReview);
        List<QmsMrbReviewDto> list = qmsMrbReviewService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsMrbReview));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsHtMrbReview>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsMrbReview searchQmsMrbReview) {
        Page<Object> page = PageHelper.startPage(searchQmsMrbReview.getStartPage(),searchQmsMrbReview.getPageSize());
        List<QmsHtMrbReview> list = qmsHtMrbReview.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsMrbReview));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsMrbReview searchQmsMrbReview){
    List<QmsMrbReviewDto> list = qmsMrbReviewService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsMrbReview));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "MRB评审导出信息", "MRB评审信息", QmsMrbReview.class, "MRB评审.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
