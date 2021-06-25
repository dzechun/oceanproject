package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamStandingBook;
import com.fantechs.common.base.general.entity.eam.history.EamHtStandingBook;
import com.fantechs.common.base.general.entity.eam.search.SearchEamStandingBook;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamStandingBookService;
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
 * Created by leifengzhi on 2021/06/25.
 */
@RestController
@Api(tags = "台账管理")
@RequestMapping("/eamStandingBook")
@Validated
public class EamStandingBookController {

    @Resource
    private EamStandingBookService eamStandingBookService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamStandingBook eamStandingBook) {
        return ControllerUtil.returnCRUD(eamStandingBookService.save(eamStandingBook));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamStandingBookService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamStandingBook.update.class) EamStandingBook eamStandingBook) {
        return ControllerUtil.returnCRUD(eamStandingBookService.update(eamStandingBook));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamStandingBook> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamStandingBook  eamStandingBook = eamStandingBookService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamStandingBook,StringUtils.isEmpty(eamStandingBook)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamStandingBookDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamStandingBook searchEamStandingBook) {
        Page<Object> page = PageHelper.startPage(searchEamStandingBook.getStartPage(),searchEamStandingBook.getPageSize());
        List<EamStandingBookDto> list = eamStandingBookService.findList(ControllerUtil.dynamicConditionByEntity(searchEamStandingBook));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtStandingBook>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamStandingBook searchEamStandingBook) {
        Page<Object> page = PageHelper.startPage(searchEamStandingBook.getStartPage(),searchEamStandingBook.getPageSize());
        List<EamHtStandingBook> list = eamStandingBookService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamStandingBook));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamStandingBook searchEamStandingBook){
    List<EamStandingBookDto> list = eamStandingBookService.findList(ControllerUtil.dynamicConditionByEntity(searchEamStandingBook));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "台账管理", EamStandingBookDto.class, "台账管理.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
