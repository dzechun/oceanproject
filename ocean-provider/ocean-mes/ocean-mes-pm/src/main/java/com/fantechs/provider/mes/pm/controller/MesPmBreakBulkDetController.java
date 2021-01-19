package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDetDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmBreakBulkDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulkDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmBreakBulkDetService;
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
 * Created by mr.lei on 2021/01/18.
 */
@RestController
@Api(tags = "mesPmBreakBulkDet控制器")
@RequestMapping("/mesPmBreakBulkDet")
@Validated
public class MesPmBreakBulkDetController {

    @Resource
    private MesPmBreakBulkDetService mesPmBreakBulkDetService;

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmBreakBulkDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmBreakBulkDet  mesPmBreakBulkDet = mesPmBreakBulkDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmBreakBulkDet,StringUtils.isEmpty(mesPmBreakBulkDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmBreakBulkDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmBreakBulkDet searchMesPmBreakBulkDet) {
        Page<Object> page = PageHelper.startPage(searchMesPmBreakBulkDet.getStartPage(),searchMesPmBreakBulkDet.getPageSize());
        List<MesPmBreakBulkDetDto> list = mesPmBreakBulkDetService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmBreakBulkDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
