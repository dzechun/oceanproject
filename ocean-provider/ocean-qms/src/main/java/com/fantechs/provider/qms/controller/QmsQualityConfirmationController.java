package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsQualityConfirmation;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsQualityConfirmationService;
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
 * Created by leifengzhi on 2021/01/19.
 */
@RestController
@Api(tags = "品质确认")
@RequestMapping("/qmsQualityConfirmation")
@Validated
public class QmsQualityConfirmationController {

    @Resource
    private QmsQualityConfirmationService qmsQualityConfirmationService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsQualityConfirmation qmsQualityConfirmation) {
        return ControllerUtil.returnCRUD(qmsQualityConfirmationService.save(qmsQualityConfirmation));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsQualityConfirmationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsQualityConfirmation.update.class) QmsQualityConfirmation qmsQualityConfirmation) {
        return ControllerUtil.returnCRUD(qmsQualityConfirmationService.update(qmsQualityConfirmation));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsQualityConfirmation> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsQualityConfirmation  qmsQualityConfirmation = qmsQualityConfirmationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsQualityConfirmation,StringUtils.isEmpty(qmsQualityConfirmation)?0:1);
    }

    @ApiOperation("解析流程单")
    @PostMapping("/analysis")
    public ResponseEntity<QmsQualityConfirmationDto> analysis(@ApiParam(value = "code",required = true)@RequestParam String code,
                                                              @ApiParam(value = "type",required = true)@RequestParam Byte type) {
        QmsQualityConfirmationDto  qmsQualityConfirmation = qmsQualityConfirmationService.analysis(code,type);
        return  ControllerUtil.returnDataSuccess(qmsQualityConfirmation,StringUtils.isEmpty(qmsQualityConfirmation)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsQualityConfirmationDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsQualityConfirmation searchQmsQualityConfirmation) {
        Page<Object> page = PageHelper.startPage(searchQmsQualityConfirmation.getStartPage(),searchQmsQualityConfirmation.getPageSize());
        List<QmsQualityConfirmationDto> list = qmsQualityConfirmationService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsQualityConfirmation));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<QmsQualityConfirmation>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsQualityConfirmation searchQmsQualityConfirmation) {
//        Page<Object> page = PageHelper.startPage(searchQmsQualityConfirmation.getStartPage(),searchQmsQualityConfirmation.getPageSize());
//        List<QmsQualityConfirmation> list = qmsQualityConfirmationService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsQualityConfirmation));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsQualityConfirmation searchQmsQualityConfirmation){
    List<QmsQualityConfirmationDto> list = qmsQualityConfirmationService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsQualityConfirmation));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "品质确认导出信息", "品质确认信息", QmsQualityConfirmationDto.class, "品质确认.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
