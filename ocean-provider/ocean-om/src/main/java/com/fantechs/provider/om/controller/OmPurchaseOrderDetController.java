package com.fantechs.provider.om.controller;

import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.OmPurchaseOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
/**
 *
 * Created by leifengzhi on 2021/06/17.
 */
@RestController
@Api(tags = "采购订单详情")
@RequestMapping("/omPurchaseOrderDet")
@Validated
public class OmPurchaseOrderDetController {

    @Resource
    private OmPurchaseOrderDetService omPurchaseOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated OmPurchaseOrderDet omPurchaseOrderDet) {
        return ControllerUtil.returnCRUD(omPurchaseOrderDetService.save(omPurchaseOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(omPurchaseOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=OmPurchaseOrderDet.update.class) OmPurchaseOrderDet omPurchaseOrderDet) {
        return ControllerUtil.returnCRUD(omPurchaseOrderDetService.update(omPurchaseOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<OmPurchaseOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        OmPurchaseOrderDet  omPurchaseOrderDet = omPurchaseOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(omPurchaseOrderDet,StringUtils.isEmpty(omPurchaseOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<OmPurchaseOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchOmPurchaseOrderDet searchOmPurchaseOrderDet) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseOrderDet.getStartPage(),searchOmPurchaseOrderDet.getPageSize());
        List<OmPurchaseOrderDetDto> list = omPurchaseOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity saveByApi(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<OmPurchaseOrderDet> omPurchaseOrderDets) {
        int i = omPurchaseOrderDetService.batchAdd(omPurchaseOrderDets);
        return ControllerUtil.returnCRUD(i);
    }
}
