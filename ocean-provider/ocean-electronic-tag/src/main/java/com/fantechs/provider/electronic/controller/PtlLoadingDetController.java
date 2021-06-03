package com.fantechs.provider.electronic.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.electronic.dto.PtlLoadingDetDto;
import com.fantechs.common.base.electronic.entity.PtlLoadingDet;
import com.fantechs.common.base.electronic.entity.search.SearchPtlLoadingDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.PtlLoadingDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@RestController
@Api(tags = "捡料上架物料明细控制器")
@RequestMapping("/smtLoadingDet")
@Validated
public class PtlLoadingDetController {

    @Autowired
    private PtlLoadingDetService ptlLoadingDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    @Transactional
    @LcnTransaction
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated PtlLoadingDet ptlLoadingDet) {
        return ControllerUtil.returnCRUD(ptlLoadingDetService.save(ptlLoadingDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ptlLoadingDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @Transactional
    @LcnTransaction
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= PtlLoadingDet.update.class) PtlLoadingDet ptlLoadingDet) {
        return ControllerUtil.returnCRUD(ptlLoadingDetService.update(ptlLoadingDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<PtlLoadingDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        PtlLoadingDet ptlLoadingDet = ptlLoadingDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(ptlLoadingDet,StringUtils.isEmpty(ptlLoadingDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<PtlLoadingDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchPtlLoadingDet searchPtlLoadingDet) {
        Page<Object> page = PageHelper.startPage(searchPtlLoadingDet.getStartPage(), searchPtlLoadingDet.getPageSize());
        List<PtlLoadingDetDto> list = ptlLoadingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlLoadingDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<SmtLoadingDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtLoadingDet searchSmtLoadingDet) {
//        Page<Object> page = PageHelper.startPage(searchSmtLoadingDet.getStartPage(),searchSmtLoadingDet.getPageSize());
//        List<SmtLoadingDetDto> list = smtLoadingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtLoadingDet));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchPtlLoadingDet searchPtlLoadingDet){
    List<PtlLoadingDetDto> list = ptlLoadingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlLoadingDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "捡料上架明细", "捡料上架明细信息", PtlLoadingDetDto.class, "捡料上架明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
