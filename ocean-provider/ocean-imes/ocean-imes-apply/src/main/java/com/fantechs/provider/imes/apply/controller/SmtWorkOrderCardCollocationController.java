package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.dto.apply.SmtWorkOrderCardCollocationDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderCardCollocationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/20.
 */
@RestController
@Api(tags = "smtWorkOrderCardCollocation控制器")
@RequestMapping("/smtWorkOrderCardCollocation")
@Validated
public class SmtWorkOrderCardCollocationController {

    @Autowired
    private SmtWorkOrderCardCollocationService smtWorkOrderCardCollocationService;

    /**
     * 产生工单流转卡
     * @param smtWorkOrderCardCollocation
     * @return
     */
    @ApiOperation(value = "产生工单流转卡",notes = "产生工单流转卡")
    @PostMapping("/generateWorkOrderCardCollocation")
    public ResponseEntity generateWorkOrderCardCollocation(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtWorkOrderCardCollocation smtWorkOrderCardCollocation) {
        return ControllerUtil.returnCRUD(smtWorkOrderCardCollocationService.save(smtWorkOrderCardCollocation));
    }


    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkOrderCardCollocation> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtWorkOrderCardCollocation  smtWorkOrderCardCollocation = smtWorkOrderCardCollocationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWorkOrderCardCollocation,StringUtils.isEmpty(smtWorkOrderCardCollocation)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderCardCollocationDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderCardCollocation.getStartPage(),searchSmtWorkOrderCardCollocation.getPageSize());
        List<SmtWorkOrderCardCollocationDto> list = smtWorkOrderCardCollocationService.findList(searchSmtWorkOrderCardCollocation);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}