package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessCardDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessListCardDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessListProcessReDTO;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmProcessCard;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmProcessListCard;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessCard;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmProcessCardService;
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
 * Created by mr.lei on 2021/01/19.
 */
@RestController
@Api(tags = "工单流转记录报表")
@RequestMapping("/mesPmProcessCard")
@Validated
public class MesPmProcessCardController {

    @Resource
    private MesPmProcessCardService mesPmProcessCardService;

    @ApiOperation("列表")
    @PostMapping("/detial")
    public ResponseEntity<MesPmProcessCardDto> detial(@ApiParam(value = "查询对象")@RequestBody SearchMesPmProcessCard searchMesPmProcessCard) {
        MesPmProcessCardDto list = mesPmProcessCardService.detial(searchMesPmProcessCard);
        return ControllerUtil.returnDataSuccess(list,StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation("工序过站记录")
    @PostMapping("/processList")
    public ResponseEntity<List<MesPmProcessListCardDto>> processList(@RequestBody(required = true)SearchMesPmProcessListCard searchMesPmProcessListCard){
        Page<Object> page = PageHelper.startPage(searchMesPmProcessListCard.getStartPage(), searchMesPmProcessListCard.getPageSize());
        List<MesPmProcessListCardDto> mesPmProcessListCardDto = mesPmProcessCardService.processList(searchMesPmProcessListCard);
        return ControllerUtil.returnDataSuccess(mesPmProcessListCardDto,(int)page.getTotal());
    }
}
