package com.fantechs.provider.esop.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopNewsDto;
import com.fantechs.common.base.general.entity.esop.EsopNews;
import com.fantechs.common.base.general.entity.esop.history.EsopHtNews;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopNews;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.esop.service.EsopHtNewsService;
import com.fantechs.provider.esop.service.EsopNewsService;
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
@RequestMapping("/esopNews")
@Validated
public class EsopNewsController {

    @Resource
    private EsopNewsService esopNewsService;
    @Resource
    private EsopHtNewsService esopHtNewsService;

    @ApiOperation(value = "审核并发布",notes = "审核并发布")
    @PostMapping("/audit")
    public ResponseEntity audit(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) throws UnknownHostException {
        return ControllerUtil.returnCRUD(esopNewsService.audit(ids));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EsopNews esopNews) {
        return ControllerUtil.returnCRUD(esopNewsService.save(esopNews));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(esopNewsService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EsopNews.update.class) EsopNews esopNews) {
        return ControllerUtil.returnCRUD(esopNewsService.update(esopNews));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EsopNews> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EsopNews  esopNews = esopNewsService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(esopNews,StringUtils.isEmpty(esopNews)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EsopNewsDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEsopNews searchEsopNews) {
        Page<Object> page = PageHelper.startPage(searchEsopNews.getStartPage(),searchEsopNews.getPageSize());
        List<EsopNewsDto> list = esopNewsService.findList(ControllerUtil.dynamicConditionByEntity(searchEsopNews));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EsopHtNews>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEsopNews searchEsopNews) {
        Page<Object> page = PageHelper.startPage(searchEsopNews.getStartPage(),searchEsopNews.getPageSize());
        List<EsopHtNews> list = esopHtNewsService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEsopNews));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stresop")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEsopNews searchEsopNews){
    List<EsopNewsDto> list = esopNewsService.findList(ControllerUtil.dynamicConditionByEntity(searchEsopNews));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "新闻发布", EsopNewsDto.class, "新闻发布.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
