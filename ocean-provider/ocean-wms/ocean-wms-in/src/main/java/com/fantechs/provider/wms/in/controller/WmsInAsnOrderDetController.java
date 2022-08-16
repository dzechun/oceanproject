package com.fantechs.provider.wms.in.controller;

import com.fantechs.common.base.general.dto.wms.in.WmsInAsnOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInAsnOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.in.service.WmsInAsnOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/04/29.
 */
@RestController
@Api(tags = "完工入库明细")
@RequestMapping("/wmsInAsnOrderDet")
@Validated
public class WmsInAsnOrderDetController {

    @Resource
    private WmsInAsnOrderDetService wmsInAsnOrderDetService;

    @ApiOperation("检查条码")
    @PostMapping("/checkBarcode")
    public ResponseEntity<BigDecimal> checkBarcode(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInAsnOrderDetDto wmsInAsnOrderDetDto) {
        BigDecimal num = wmsInAsnOrderDetService.checkBarcode(wmsInAsnOrderDetDto);
        return  ControllerUtil.returnDataSuccess(num,1);
    }

    @ApiOperation("提交")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInAsnOrderDetDto wmsInAsnOrderDetDto) {
        return ControllerUtil.returnCRUD(wmsInAsnOrderDetService.update(wmsInAsnOrderDetDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInAsnOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInAsnOrderDet  wmsInAsnOrderDet = wmsInAsnOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInAsnOrderDet,StringUtils.isEmpty(wmsInAsnOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInAsnOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInAsnOrderDet searchWmsInAsnOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInAsnOrderDet.getStartPage(),searchWmsInAsnOrderDet.getPageSize());
        List<WmsInAsnOrderDetDto> list = wmsInAsnOrderDetService.findList(searchWmsInAsnOrderDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
