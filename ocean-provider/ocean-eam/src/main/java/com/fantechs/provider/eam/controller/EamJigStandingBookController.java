package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigStandingBookDto;
import com.fantechs.common.base.general.entity.eam.EamJigStandingBook;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigStandingBook;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigStandingBook;
import com.fantechs.provider.eam.service.EamHtJigStandingBookService;
import com.fantechs.provider.eam.service.EamJigStandingBookService;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */
@RestController
@Api(tags = "eamJigStandingBook控制器")
@RequestMapping("/eamJigStandingBook")
@Validated
public class EamJigStandingBookController {

    @Resource
    private EamJigStandingBookService eamJigStandingBookService;

    @Resource
    private EamHtJigStandingBookService eamHtJigStandingBookService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigStandingBook eamJigStandingBook) {
        return ControllerUtil.returnCRUD(eamJigStandingBookService.save(eamJigStandingBook));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigStandingBookService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigStandingBook.update.class) EamJigStandingBook eamJigStandingBook) {
        return ControllerUtil.returnCRUD(eamJigStandingBookService.update(eamJigStandingBook));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigStandingBook> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigStandingBook  eamJigStandingBook = eamJigStandingBookService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigStandingBook,StringUtils.isEmpty(eamJigStandingBook)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigStandingBookDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigStandingBook searchEamJigStandingBook) {
        Page<Object> page = PageHelper.startPage(searchEamJigStandingBook.getStartPage(),searchEamJigStandingBook.getPageSize());
        List<EamJigStandingBookDto> list = eamJigStandingBookService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigStandingBook));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigStandingBook>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigStandingBook searchEamJigStandingBook) {
        Page<Object> page = PageHelper.startPage(searchEamJigStandingBook.getStartPage(),searchEamJigStandingBook.getPageSize());
        List<EamHtJigStandingBook> list = eamHtJigStandingBookService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigStandingBook));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigStandingBook searchEamJigStandingBook){
    List<EamJigStandingBookDto> list = eamJigStandingBookService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigStandingBook));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EamJigStandingBook信息", EamJigStandingBookDto.class, "EamJigStandingBook.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
