package com.fantechs.provider.bcm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.BcmLabelCategoryDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabelCategory;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabelCategory;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelCategory;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.bcm.service.BcmHtLabelCategoryService;
import com.fantechs.provider.bcm.service.BcmLabelCategoryService;
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
@Api(tags = "标签类别")
@RequestMapping("/bcmLabelCategory")
@Validated
public class BcmLabelCategoryController {

    @Autowired
    private BcmLabelCategoryService bcmLabelCategoryService;
    @Autowired
    private BcmHtLabelCategoryService bcmHtLabelCategoryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BcmLabelCategory bcmLabelCategory) {
        return ControllerUtil.returnCRUD(bcmLabelCategoryService.save(bcmLabelCategory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(bcmLabelCategoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BcmLabelCategory.update.class) BcmLabelCategory bcmLabelCategory) {
        return ControllerUtil.returnCRUD(bcmLabelCategoryService.update(bcmLabelCategory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BcmLabelCategory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BcmLabelCategory bcmLabelCategory = bcmLabelCategoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(bcmLabelCategory,StringUtils.isEmpty(bcmLabelCategory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BcmLabelCategoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBcmLabelCategory searchBcmLabelCategory) {
        Page<Object> page = PageHelper.startPage(searchBcmLabelCategory.getStartPage(),searchBcmLabelCategory.getPageSize());
        List<BcmLabelCategoryDto> list = bcmLabelCategoryService.findList(searchBcmLabelCategory);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BcmHtLabelCategory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBcmLabelCategory searchBcmLabelCategory) {
        Page<Object> page = PageHelper.startPage(searchBcmLabelCategory.getStartPage(),searchBcmLabelCategory.getPageSize());
        List<BcmHtLabelCategory> list = bcmHtLabelCategoryService.findList(searchBcmLabelCategory);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBcmLabelCategory searchBcmLabelCategory){
    List<BcmLabelCategoryDto> list = bcmLabelCategoryService.findList(searchBcmLabelCategory);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BcmLabelCategory信息", BcmLabelCategory.class, "BcmLabelCategory.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
