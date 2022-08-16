package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseLabelDto;
import com.fantechs.common.base.general.entity.basic.BaseLabel;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtLabelService;
import com.fantechs.provider.base.service.BaseLabelService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@RestController
@Api(tags = "标签信息")
@RequestMapping("/baseLabel")
@Validated
public class BaseLabelController {

    @Resource
    private BaseLabelService baseLabelService;
    @Resource
    private BaseHtLabelService baseHtLabelService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@Validated BaseLabel baseLabel, MultipartFile file) {
        return ControllerUtil.returnCRUD(baseLabelService.add(baseLabel,file));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseLabelService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true) @Validated(value= BaseLabel.update.class) BaseLabel baseLabel, BindingResult bindingResult, MultipartFile file) {
        return ControllerUtil.returnCRUD(baseLabelService.update(baseLabel,file));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseLabel> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseLabel baseLabel = baseLabelService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseLabel,StringUtils.isEmpty(baseLabel)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseLabelDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabel searchBaseLabel) {
        Page<Object> page = PageHelper.startPage(searchBaseLabel.getStartPage(), searchBaseLabel.getPageSize());
        List<BaseLabelDto> list = baseLabelService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabel));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("根绝id集合获取列表")
    @PostMapping("/findListByIDs")
    public ResponseEntity<List<BaseLabel>> findListByIDs(@ApiParam(value = "查询对象")@RequestBody List<Long> ids) {
        List<BaseLabel> list = baseLabelService.findListByIDs(ids);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtLabel>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabel searchBaseLabel) {
        Page<Object> page = PageHelper.startPage(searchBaseLabel.getStartPage(), searchBaseLabel.getPageSize());
        List<BaseHtLabel> list = baseHtLabelService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabel));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseLabel searchBaseLabel){
    List<BaseLabelDto> list = baseLabelService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseLabel));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "BcmLabel信息", BaseLabelDto.class, "BcmLabel.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
