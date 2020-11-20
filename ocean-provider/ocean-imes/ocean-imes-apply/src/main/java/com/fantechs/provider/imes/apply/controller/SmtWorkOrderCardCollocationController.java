package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
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

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtWorkOrderCardCollocation smtWorkOrderCardCollocation) {
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
    public ResponseEntity<List<SmtWorkOrderCardCollocation>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderCardCollocation.getStartPage(),searchSmtWorkOrderCardCollocation.getPageSize());
        List<SmtWorkOrderCardCollocation> list = smtWorkOrderCardCollocationService.findList(searchSmtWorkOrderCardCollocation);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
