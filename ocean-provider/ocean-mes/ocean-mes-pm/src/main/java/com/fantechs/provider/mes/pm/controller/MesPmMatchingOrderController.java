package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmMatchingDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmMatchingOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.SaveMesPmMatchingOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmMatchingOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMatchingOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.MesPmMatchingOrderService;
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
 * Created by leifengzhi on 2021/02/02.
 */
@RestController
@Api(tags = "配套单信息管理")
@RequestMapping("/mesPmMatchingOrder")
@Validated
public class MesPmMatchingOrderController {

    @Resource
    private MesPmMatchingOrderService mesPmMatchingOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SaveMesPmMatchingOrderDto saveMesPmMatchingOrderDto) {
        return ControllerUtil.returnCRUD(mesPmMatchingOrderService.save(saveMesPmMatchingOrderDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesPmMatchingOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesPmMatchingOrder.update.class) MesPmMatchingOrder mesPmMatchingOrder) {
        return ControllerUtil.returnCRUD(mesPmMatchingOrderService.update(mesPmMatchingOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesPmMatchingOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesPmMatchingOrder  mesPmMatchingOrder = mesPmMatchingOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesPmMatchingOrder,StringUtils.isEmpty(mesPmMatchingOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesPmMatchingOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesPmMatchingOrder searchMesPmMatchingOrder) {
        Page<Object> page = PageHelper.startPage(searchMesPmMatchingOrder.getStartPage(),searchMesPmMatchingOrder.getPageSize());
        List<MesPmMatchingOrderDto> list = mesPmMatchingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmMatchingOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesPmMatchingOrder searchMesPmMatchingOrder){
    List<MesPmMatchingOrderDto> list = mesPmMatchingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmMatchingOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesPmMatchingOrder信息", MesPmMatchingOrderDto.class, "MesPmMatchingOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("获取最小齐套数")
    @PostMapping("/findMinMatchingQuantity")
    public ResponseEntity<MesPmMatchingDto> findMinMatchingQuantity(@ApiParam(value = "查询对象")@RequestParam String workOrderCardId) {

        MesPmMatchingDto mesPmMatchingDto = mesPmMatchingOrderService.findMinMatchingQuantity(workOrderCardId);
        return ControllerUtil.returnSuccess("",mesPmMatchingDto);
    }
}
