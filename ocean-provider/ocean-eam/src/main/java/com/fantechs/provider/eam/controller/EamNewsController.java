package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamNewsDto;
import com.fantechs.common.base.general.entity.eam.EamNews;
import com.fantechs.common.base.general.entity.eam.history.EamHtNews;
import com.fantechs.common.base.general.entity.eam.search.SearchEamNews;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtNewsService;
import com.fantechs.provider.eam.service.EamNewsService;
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
import java.net.UnknownHostException;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/07.
 */
@RestController
@Api(tags = "新闻发布")
@RequestMapping("/eamNews")
@Validated
public class EamNewsController {

    @Resource
    private EamNewsService eamNewsService;
    @Resource
    private EamHtNewsService eamHtNewsService;

    @ApiOperation(value = "审核并发布",notes = "审核并发布")
    @PostMapping("/audit")
    public ResponseEntity audit(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) throws UnknownHostException {
        return ControllerUtil.returnCRUD(eamNewsService.audit(ids));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamNews eamNews) {
        return ControllerUtil.returnCRUD(eamNewsService.save(eamNews));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamNewsService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamNews.update.class) EamNews eamNews) {
        return ControllerUtil.returnCRUD(eamNewsService.update(eamNews));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamNews> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamNews  eamNews = eamNewsService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamNews,StringUtils.isEmpty(eamNews)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamNewsDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamNews searchEamNews) {
        Page<Object> page = PageHelper.startPage(searchEamNews.getStartPage(),searchEamNews.getPageSize());
        List<EamNewsDto> list = eamNewsService.findList(ControllerUtil.dynamicConditionByEntity(searchEamNews));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtNews>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamNews searchEamNews) {
        Page<Object> page = PageHelper.startPage(searchEamNews.getStartPage(),searchEamNews.getPageSize());
        List<EamHtNews> list = eamHtNewsService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamNews));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamNews searchEamNews){
    List<EamNewsDto> list = eamNewsService.findList(ControllerUtil.dynamicConditionByEntity(searchEamNews));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "新闻发布", EamNewsDto.class, "新闻发布.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
