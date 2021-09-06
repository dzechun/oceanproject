package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderDto;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.general.entity.eng.search.SearchEngContractQtyOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.eng.service.EngContractQtyOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@RestController
@Api(tags = "合同量单信息")
@RequestMapping("/engContractQtyOrder")
@Validated
public class EngContractQtyOrderController {

    @Resource
    private EngContractQtyOrderService engContractQtyOrderService;

//    @ApiOperation(value = "新增",notes = "新增")
//    @PostMapping("/add")
//    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EngContractQtyOrder engContractQtyOrder) {
//        return ControllerUtil.returnCRUD(engContractQtyOrderService.save(engContractQtyOrder));
//    }

//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(engContractQtyOrderService.batchDelete(ids));
//    }

//    @ApiOperation("修改")
//    @PostMapping("/update")
//    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= EngContractQtyOrder.update.class) EngContractQtyOrder engContractQtyOrder) {
//        return ControllerUtil.returnCRUD(engContractQtyOrderService.update(engContractQtyOrder));
//    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EngContractQtyOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EngContractQtyOrder engContractQtyOrder = engContractQtyOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(engContractQtyOrder, StringUtils.isEmpty(engContractQtyOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EngContractQtyOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEngContractQtyOrder searchEngContractQtyOrder) {
        Page<Object> page = PageHelper.startPage(searchEngContractQtyOrder.getStartPage(),searchEngContractQtyOrder.getPageSize());
        List<EngContractQtyOrderDto> list = engContractQtyOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngContractQtyOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEngContractQtyOrder searchEngContractQtyOrder){
    List<EngContractQtyOrderDto> list = engContractQtyOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEngContractQtyOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EngContractQtyOrder信息", EngContractQtyOrderDto.class, "EngContractQtyOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity saveByApi(@ApiParam(value = "必传：contractCode、dominantTermCode",required = true)@RequestBody @Validated EngContractQtyOrder engContractQtyOrder) {
        return ControllerUtil.returnCRUD(engContractQtyOrderService.saveByApi(engContractQtyOrder));
    }
}
