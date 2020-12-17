package com.fantechs.controller.ht;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.entity.BcmHtLabelMaterial;
import com.fantechs.service.BcmHtLabelMaterialService;
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
@Api(tags = "bcmHtLabelMaterial控制器")
@RequestMapping("/bcmHtLabelMaterial")
@Validated
public class BcmHtLabelMaterialController {

    @Autowired
    private BcmHtLabelMaterialService bcmHtLabelMaterialService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BcmHtLabelMaterial bcmHtLabelMaterial) {
        return ControllerUtil.returnCRUD(bcmHtLabelMaterialService.save(bcmHtLabelMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(bcmHtLabelMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BcmHtLabelMaterial.update.class) BcmHtLabelMaterial bcmHtLabelMaterial) {
        return ControllerUtil.returnCRUD(bcmHtLabelMaterialService.update(bcmHtLabelMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BcmHtLabelMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BcmHtLabelMaterial  bcmHtLabelMaterial = bcmHtLabelMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(bcmHtLabelMaterial,StringUtils.isEmpty(bcmHtLabelMaterial)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BcmHtLabelMaterial>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBcmHtLabelMaterial searchBcmHtLabelMaterial) {
        Page<Object> page = PageHelper.startPage(searchBcmHtLabelMaterial.getStartPage(),searchBcmHtLabelMaterial.getPageSize());
        List<BcmHtLabelMaterial> list = bcmHtLabelMaterialService.findList(searchBcmHtLabelMaterial);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BcmHtLabelMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBcmHtLabelMaterial searchBcmHtLabelMaterial) {
        Page<Object> page = PageHelper.startPage(searchBcmHtLabelMaterial.getStartPage(),searchBcmHtLabelMaterial.getPageSize());
        List<BcmHtLabelMaterial> list = bcmHtLabelMaterialService.findList(searchBcmHtLabelMaterial);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBcmHtLabelMaterial searchBcmHtLabelMaterial){
    List<BcmHtLabelMaterial> list = bcmHtLabelMaterialService.findList(searchBcmHtLabelMaterial);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BcmHtLabelMaterial信息", BcmHtLabelMaterial.class, "BcmHtLabelMaterial.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
