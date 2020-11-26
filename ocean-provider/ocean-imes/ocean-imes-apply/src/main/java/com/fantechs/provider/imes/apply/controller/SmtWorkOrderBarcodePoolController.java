package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.dto.apply.SmtWorkOrderBarcodePoolDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBarcodePool;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderBarcodePoolService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/25.
 */
@RestController
@Api(tags = "工单任务池表")
@RequestMapping("/smtWorkOrderBarcodePool")
@Validated
public class SmtWorkOrderBarcodePoolController {

    @Autowired
    private SmtWorkOrderBarcodePoolService smtWorkOrderBarcodePoolService;


    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtWorkOrderBarcodePool> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtWorkOrderBarcodePool  smtWorkOrderBarcodePool = smtWorkOrderBarcodePoolService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtWorkOrderBarcodePool,StringUtils.isEmpty(smtWorkOrderBarcodePool)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderBarcodePoolDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderBarcodePool searchSmtWorkOrderBarcodePool) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderBarcodePool.getStartPage(),searchSmtWorkOrderBarcodePool.getPageSize());
        List<SmtWorkOrderBarcodePoolDto> list = smtWorkOrderBarcodePoolService.findList(searchSmtWorkOrderBarcodePool);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
