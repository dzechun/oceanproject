package com.fantechs.provider.bcm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.BcmLabelMaterialDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabelMaterial;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabelMaterial;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelMaterial;
import com.fantechs.provider.bcm.service.BcmHtLabelMaterialService;
import com.fantechs.provider.bcm.service.BcmLabelMaterialService;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@RestController
@Api(tags = "产品标签关联信息")
@RequestMapping("/bcmLabelMaterial")
@Validated
public class BcmLabelMaterialController {

    @Autowired
    private BcmLabelMaterialService bcmLabelMaterialService;
    @Autowired
    private BcmHtLabelMaterialService bcmHtLabelMaterialService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BcmLabelMaterial bcmLabelMaterial) {
        return ControllerUtil.returnCRUD(bcmLabelMaterialService.save(bcmLabelMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(bcmLabelMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BcmLabelMaterial.update.class) BcmLabelMaterial bcmLabelMaterial) {
        return ControllerUtil.returnCRUD(bcmLabelMaterialService.update(bcmLabelMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BcmLabelMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BcmLabelMaterial  bcmLabelMaterial = bcmLabelMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(bcmLabelMaterial,StringUtils.isEmpty(bcmLabelMaterial)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BcmLabelMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBcmLabelMaterial searchBcmLabelMaterial) {
        Page<Object> page = PageHelper.startPage(searchBcmLabelMaterial.getStartPage(),searchBcmLabelMaterial.getPageSize());
        List<BcmLabelMaterialDto> list = bcmLabelMaterialService.findList(searchBcmLabelMaterial);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BcmHtLabelMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBcmLabelMaterial searchBcmLabelMaterial) {
        Page<Object> page = PageHelper.startPage(searchBcmLabelMaterial.getStartPage(),searchBcmLabelMaterial.getPageSize());
        List<BcmHtLabelMaterial> list = bcmHtLabelMaterialService.findList(searchBcmLabelMaterial);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBcmLabelMaterial searchBcmLabelMaterial){
    List<BcmLabelMaterialDto> list = bcmLabelMaterialService.findList(searchBcmLabelMaterial);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BcmLabelMaterial信息", BcmLabelMaterialDto.class, "BcmLabelMaterial.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
