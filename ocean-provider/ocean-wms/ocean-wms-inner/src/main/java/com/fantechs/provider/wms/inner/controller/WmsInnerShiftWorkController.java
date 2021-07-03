package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchPdaFindListDto;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerShiftWorkDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerShiftWorkService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 仓库作业-库内移位作业管理
 */
@RestController
@Api(tags = "仓库作业-库内移位作业管理")
@RequestMapping("/wmsInnerShiftWork")
@Validated
public class WmsInnerShiftWorkController {

    @Resource
    WmsInnerShiftWorkService wmsInnerShiftWorkService;


    @ApiOperation("库内移位单列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerJobOrderDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerShiftWorkDto dto) {
        Page<Object> page = PageHelper.startPage(dto.getStartPage(), dto.getPageSize());
        List<WmsInnerJobOrderDto> list = wmsInnerShiftWorkService.pdaFindList(ControllerUtil.dynamicConditionByEntity(dto));
        return ControllerUtil.returnDataSuccess(list != null && list.size() > 0 ? list : new ArrayList<>(), (int) page.getTotal());
    }
}
