package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsBadItemDetDto;
import com.fantechs.common.base.general.entity.qms.QmsBadItemDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsBadItemDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsBadItemDetService;
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
 * Created by leifengzhi on 2021/01/18.
 */
@RestController
@Api(tags = "不良项目明细")
@RequestMapping("/qmsBadItemDet")
@Validated
public class QmsBadItemDetController {

    @Resource
    private QmsBadItemDetService qmsBadItemDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsBadItemDet qmsBadItemDet) {
        return ControllerUtil.returnCRUD(qmsBadItemDetService.save(qmsBadItemDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsBadItemDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsBadItemDet.update.class) QmsBadItemDet qmsBadItemDet) {
        return ControllerUtil.returnCRUD(qmsBadItemDetService.update(qmsBadItemDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsBadItemDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsBadItemDet  qmsBadItemDet = qmsBadItemDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsBadItemDet,StringUtils.isEmpty(qmsBadItemDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsBadItemDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsBadItemDet searchQmsBadItemDet) {
        Page<Object> page = PageHelper.startPage(searchQmsBadItemDet.getStartPage(),searchQmsBadItemDet.getPageSize());
        List<QmsBadItemDetDto> list = qmsBadItemDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsBadItemDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<QmsBadItemDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsBadItemDet searchQmsBadItemDet) {
//        Page<Object> page = PageHelper.startPage(searchQmsBadItemDet.getStartPage(),searchQmsBadItemDet.getPageSize());
//        List<QmsBadItemDet> list = qmsBadItemDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsBadItemDet));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }
//
//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchQmsBadItemDet searchQmsBadItemDet){
//    List<QmsBadItemDetDto> list = qmsBadItemDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsBadItemDet));
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "QmsBadItemDet信息", QmsBadItemDetDto.class, "QmsBadItemDet.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }
}
