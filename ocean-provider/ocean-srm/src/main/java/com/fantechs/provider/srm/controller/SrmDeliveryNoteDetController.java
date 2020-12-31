package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.srm.SrmDeliveryNoteDetDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryNoteDet;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmDeliveryNoteDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmDeliveryNoteDetService;
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
 * Created by leifengzhi on 2020/12/29.
 */
@RestController
@Api(tags = "送货通知单ASN明细")
@RequestMapping("/srmDeliveryNoteDet")
@Validated
public class SrmDeliveryNoteDetController {

    @Autowired
    private SrmDeliveryNoteDetService srmDeliveryNoteDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<SrmDeliveryNoteDet> srmDeliveryNoteDets) {
        return ControllerUtil.returnCRUD(srmDeliveryNoteDetService.batchSave(srmDeliveryNoteDets));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmDeliveryNoteDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmDeliveryNoteDet.update.class) List<SrmDeliveryNoteDet> srmDeliveryNoteDets) {
        return ControllerUtil.returnCRUD(srmDeliveryNoteDetService.batchUpdate(srmDeliveryNoteDets));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmDeliveryNoteDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmDeliveryNoteDet  srmDeliveryNoteDet = srmDeliveryNoteDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmDeliveryNoteDet,StringUtils.isEmpty(srmDeliveryNoteDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmDeliveryNoteDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmDeliveryNoteDet searchSrmDeliveryNoteDet) {
        Page<Object> page = PageHelper.startPage(searchSrmDeliveryNoteDet.getStartPage(),searchSrmDeliveryNoteDet.getPageSize());
        List<SrmDeliveryNoteDetDto> list = srmDeliveryNoteDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmDeliveryNoteDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSrmDeliveryNoteDet searchSrmDeliveryNoteDet){
    List<SrmDeliveryNoteDetDto> list = srmDeliveryNoteDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmDeliveryNoteDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SrmDeliveryNoteDet信息", SrmDeliveryNoteDet.class, "SrmDeliveryNoteDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
