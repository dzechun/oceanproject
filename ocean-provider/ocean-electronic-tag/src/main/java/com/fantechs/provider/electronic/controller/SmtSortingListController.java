package com.fantechs.provider.electronic.controller;

import com.fantechs.common.base.electronic.entity.SmtSortingList;
import com.fantechs.common.base.electronic.entity.search.SearchSmtSortingList;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.SmtSortingListService;
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
 * Created by leifengzhi on 2020/12/08.
 */
@RestController
@Api(tags = "分拣单管理")
@RequestMapping("/smtSortingList")
@Validated
public class SmtSortingListController {

    @Autowired
    private SmtSortingListService smtSortingListService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtSortingList smtSortingList) {
        return ControllerUtil.returnCRUD(smtSortingListService.save(smtSortingList));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtSortingListService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtSortingList.update.class) SmtSortingList smtSortingList) {
        return ControllerUtil.returnCRUD(smtSortingListService.update(smtSortingList));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtSortingList> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtSortingList  smtSortingList = smtSortingListService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtSortingList,StringUtils.isEmpty(smtSortingList)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtSortingList>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtSortingList searchSmtSortingList) {
        Page<Object> page = PageHelper.startPage(searchSmtSortingList.getStartPage(),searchSmtSortingList.getPageSize());
        List<SmtSortingList> list = smtSortingListService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtSortingList));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtSortingList searchSmtSortingList){
    List<SmtSortingList> list = smtSortingListService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtSortingList));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtSortingList信息", SmtSortingList.class, "SmtSortingList.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
