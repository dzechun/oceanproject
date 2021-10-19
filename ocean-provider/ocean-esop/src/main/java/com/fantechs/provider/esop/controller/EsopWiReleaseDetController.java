package com.fantechs.provider.esop.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDetDto;
import com.fantechs.common.base.general.entity.esop.EsopWiReleaseDet;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWiReleaseDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.esop.service.EsopWiReleaseDetService;
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
 * Created by leifengzhi on 2021/07/08.
 */
@RestController
@Api(tags = "WI发布详情管理")
@RequestMapping("/esopWiReleaseDet")
@Validated
public class EsopWiReleaseDetController {

    @Resource
    private EsopWiReleaseDetService esopWiReleaseDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EsopWiReleaseDet esopWiReleaseDet) {
        return ControllerUtil.returnCRUD(esopWiReleaseDetService.save(esopWiReleaseDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(esopWiReleaseDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= EsopWiReleaseDet.update.class) EsopWiReleaseDet esopWiReleaseDet) {
        return ControllerUtil.returnCRUD(esopWiReleaseDetService.update(esopWiReleaseDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EsopWiReleaseDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EsopWiReleaseDet  esopWiReleaseDet = esopWiReleaseDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(esopWiReleaseDet,StringUtils.isEmpty(esopWiReleaseDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EsopWiReleaseDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEsopWiReleaseDet searchEsopWiReleaseDet) {
        Page<Object> page = PageHelper.startPage(searchEsopWiReleaseDet.getStartPage(),searchEsopWiReleaseDet.getPageSize());
        List<EsopWiReleaseDetDto> list = esopWiReleaseDetService.findList(searchEsopWiReleaseDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stresop")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEsopWiReleaseDet searchEsopWiReleaseDet){
        List<EsopWiReleaseDetDto> list = esopWiReleaseDetService.findList(searchEsopWiReleaseDet);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "工艺路线", EsopWiReleaseDetDto.class, "工艺路线.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }

    }
}
