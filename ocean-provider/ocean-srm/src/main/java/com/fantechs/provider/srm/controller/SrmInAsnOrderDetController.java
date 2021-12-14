package com.fantechs.provider.srm.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.SrmInHtAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmInAsnOrderDetImport;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import com.fantechs.common.base.general.entity.srm.search.SearchSrmInAsnOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.srm.service.SrmInAsnOrderDetService;
import com.fantechs.provider.srm.service.SrmInHtAsnOrderDetService;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/11/25.
 */
@RestController
@Api(tags = "预收货通知单详情")
@RequestMapping("/srmInAsnOrderDet")
@Validated
@Slf4j
public class SrmInAsnOrderDetController {

    @Resource
    private SrmInAsnOrderDetService srmInAsnOrderDetService;
    @Resource
    private SrmInHtAsnOrderDetService srmInHtAsnOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SrmInAsnOrderDet srmInAsnOrderDet) {
        return ControllerUtil.returnCRUD(srmInAsnOrderDetService.save(srmInAsnOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(srmInAsnOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SrmInAsnOrderDet.update.class) SrmInAsnOrderDet srmInAsnOrderDet) {
        return ControllerUtil.returnCRUD(srmInAsnOrderDetService.update(srmInAsnOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SrmInAsnOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SrmInAsnOrderDet  srmInAsnOrderDet = srmInAsnOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(srmInAsnOrderDet,StringUtils.isEmpty(srmInAsnOrderDet)?0:1);
    }

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<SrmInAsnOrderDetDto> list) {
        return ControllerUtil.returnCRUD(srmInAsnOrderDetService.pushDown(list));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmInAsnOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSrmInAsnOrderDet searchSrmInAsnOrderDet) {
        Page<Object> page = PageHelper.startPage(searchSrmInAsnOrderDet.getStartPage(),searchSrmInAsnOrderDet.getPageSize());
        List<SrmInAsnOrderDetDto> list = srmInAsnOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SrmInAsnOrderDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSrmInAsnOrderDet searchSrmInAsnOrderDet) {
        List<SrmInAsnOrderDetDto> list = srmInAsnOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrderDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

   @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SrmInHtAsnOrderDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSrmInAsnOrderDet searchSrmInAsnOrderDet) {
        Page<Object> page = PageHelper.startPage(searchSrmInAsnOrderDet.getStartPage(),searchSrmInAsnOrderDet.getPageSize());
        List<SrmInHtAsnOrderDetDto> list = srmInHtAsnOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmInAsnOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入(不保存,返回给页面)",notes = "从excel导入,返回给页面")
    public ResponseEntity importExcel(@ApiParam(value ="输入excel文件",required = true) @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<SrmInAsnOrderDetImport> srmInAsnOrderDetImports = EasyPoiUtils.importExcel(file, 0, 1, SrmInAsnOrderDetImport.class);
            List<SrmInAsnOrderDetDto> srmInAsnOrderDetDtos = srmInAsnOrderDetService.importExcels(srmInAsnOrderDetImports);
            return ControllerUtil.returnDataSuccess(srmInAsnOrderDetDtos,srmInAsnOrderDetDtos.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
    }

}
