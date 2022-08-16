package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseFile;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseFile;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseFileService;
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
 * Created by leifengzhi on 2021/09/13.
 */
@RestController
@Api(tags = "文件信息")
@RequestMapping("/baseFile")
@Validated
public class BaseFileController {

    @Resource
    private BaseFileService baseFileService;

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAddFile")
    public ResponseEntity batchAddFile(@ApiParam(value = "必传：",required = true)@RequestBody List<BaseFile> list) {
        return ControllerUtil.returnCRUD(baseFileService.batchAddFile(list));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseFile baseFile) {
        return ControllerUtil.returnCRUD(baseFileService.save(baseFile));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseFileService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseFile.update.class) BaseFile baseFile) {
        return ControllerUtil.returnCRUD(baseFileService.update(baseFile));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseFile> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseFile  baseFile = baseFileService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseFile,StringUtils.isEmpty(baseFile)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseFile>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseFile searchBaseFile) {
        Page<Object> page = PageHelper.startPage(searchBaseFile.getStartPage(),searchBaseFile.getPageSize());
        List<BaseFile> list = baseFileService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseFile));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseFile searchBaseFile){
    List<BaseFile> list = baseFileService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseFile));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "文件信息", BaseFile.class, "文件信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
