package com.fantechs.provider.imes.basic.controller;


import com.fantechs.common.base.entity.basic.history.SmtHtStorage;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtHtStorageService;
import com.fantechs.provider.imes.basic.service.SmtStorageService;
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
 *
 * Created by wcz on 2020/09/23.
 */
@RestController
@Api(tags = "储位信息")
@RequestMapping("/smtStorage")
@Validated
public class SmtStorageController {

    @Autowired
    private SmtStorageService smtStorageService;

    @Autowired
    private SmtHtStorageService smtHtStorageService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：storageCode、storageName",required = true)@RequestBody @Validated SmtStorage storage) {
        return ControllerUtil.returnCRUD(smtStorageService.save(storage));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtStorageService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = SmtStorage.update.class) SmtStorage storage) {
        return ControllerUtil.returnCRUD(smtStorageService.update(storage));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtStorage> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        SmtStorage storage = smtStorageService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(storage,StringUtils.isEmpty(storage)?0:1);
    }

    @ApiOperation("根据条件查询信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtStorage>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStorage searchSmtStorage) {
        Page<Object> page = PageHelper.startPage(searchSmtStorage.getStartPage(),searchSmtStorage.getPageSize());
        List<SmtStorage> list = smtStorageService.findList(searchSmtStorage);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("根据条件查询信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtStorage>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtStorage searchSmtStorage) {
        Page<Object> page = PageHelper.startPage(searchSmtStorage.getStartPage(),searchSmtStorage.getPageSize());
        List<SmtHtStorage> list = smtHtStorageService.findHtList(searchSmtStorage);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出储位信息excel",notes = "导出储位信息excel")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                              @RequestBody(required = false) SearchSmtStorage searchSmtStorage){
        List<SmtStorage> list = smtStorageService.findList(searchSmtStorage);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出储位信息", "储位信息", SmtStorage.class, "储位信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
