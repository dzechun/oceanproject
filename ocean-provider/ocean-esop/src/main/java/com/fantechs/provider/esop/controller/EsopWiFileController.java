package com.fantechs.provider.esop.controller;

import com.fantechs.common.base.general.entity.esop.EsopWiFile;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWiFile;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.esop.service.EsopWiFileService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/06.
 */
@RestController
@Api(tags = "电子WI附件上传")
@RequestMapping("/esopWiFile")
@Validated
public class EsopWiFileController {

    @Resource
    private EsopWiFileService esopWiFileService;

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(esopWiFileService.batchDelete(ids));
    }

    @ApiOperation("文件上传")
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@ApiParam(value = "文件必传",required = true) @RequestPart(value = "file") MultipartFile file) {
        String path = esopWiFileService.uploadFile(file);
        return  ControllerUtil.returnDataSuccess(path,1);
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：accessUrl",required = true)@RequestBody List<EsopWiFile> esopWiFiles) {
        return ControllerUtil.returnCRUD(esopWiFileService.batchAdd(esopWiFiles));
    }

    @ApiOperation(value = "批量更新",notes = "批量更新")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "必传：",required = true)@RequestBody List<EsopWiFile> esopWiFiles) {
        return ControllerUtil.returnCRUD(esopWiFileService.batchUpdate(esopWiFiles));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EsopWiFile>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEsopWiFile searchEsopWiFile) {
        Page<Object> page = PageHelper.startPage(searchEsopWiFile.getStartPage(),searchEsopWiFile.getPageSize());
        List<EsopWiFile> list = esopWiFileService.findList(ControllerUtil.dynamicConditionByEntity(searchEsopWiFile));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


}
