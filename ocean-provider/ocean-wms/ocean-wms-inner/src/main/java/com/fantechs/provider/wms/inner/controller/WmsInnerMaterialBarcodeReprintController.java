package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReprintDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReprint;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcodeReprint;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeReprintService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
 * Created by leifengzhi on 2021/12/16.
 */
@RestController
@Api(tags = "补打记录控制器")
@RequestMapping("/wmsInnerMaterialBarcodeReprint")
@Validated
public class WmsInnerMaterialBarcodeReprintController {

    @Resource
    private WmsInnerMaterialBarcodeReprintService wmsInnerMaterialBarcodeReprintService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerMaterialBarcodeReprint wmsInnerMaterialBarcodeReprint) {
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeReprintService.save(wmsInnerMaterialBarcodeReprint));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeReprintService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerMaterialBarcodeReprint.update.class) WmsInnerMaterialBarcodeReprint wmsInnerMaterialBarcodeReprint) {
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeReprintService.update(wmsInnerMaterialBarcodeReprint));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerMaterialBarcodeReprint> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerMaterialBarcodeReprint  wmsInnerMaterialBarcodeReprint = wmsInnerMaterialBarcodeReprintService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerMaterialBarcodeReprint,StringUtils.isEmpty(wmsInnerMaterialBarcodeReprint)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerMaterialBarcodeReprintDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerMaterialBarcodeReprint searchWmsInnerMaterialBarcodeReprint) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerMaterialBarcodeReprint.getStartPage(),searchWmsInnerMaterialBarcodeReprint.getPageSize());
        List<WmsInnerMaterialBarcodeReprintDto> list = wmsInnerMaterialBarcodeReprintService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerMaterialBarcodeReprint));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInnerMaterialBarcodeReprintDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerMaterialBarcodeReprint searchWmsInnerMaterialBarcodeReprint) {
        List<WmsInnerMaterialBarcodeReprintDto> list = wmsInnerMaterialBarcodeReprintService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerMaterialBarcodeReprint));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

}
