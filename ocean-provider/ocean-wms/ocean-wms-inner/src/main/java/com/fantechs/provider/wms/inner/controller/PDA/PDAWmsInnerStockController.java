package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
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
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/6/4
 */
@RestController
@Api(tags = "PDA盘点")
@RequestMapping("/PDAWmsInnerStock")
@Validated
public class PDAWmsInnerStockController {

    @Resource
    private WmsInnerInventoryDetService wmsInnerInventoryDetService;

    @ApiOperation("PDA盘点列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerInventoryDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventoryDet searchWmsInnerInventoryDet) {
        searchWmsInnerInventoryDet.setPda(true);
        Page<Object> page = PageHelper.startPage(searchWmsInnerInventoryDet.getStartPage(),searchWmsInnerInventoryDet.getPageSize());
        List<WmsInnerInventoryDetDto> list = wmsInnerInventoryDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
