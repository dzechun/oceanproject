package com.fantechs.provider.om.controller;

import com.fantechs.common.base.entity.basic.history.SmtHtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.general.dto.mes.pm.history.SearchSmtHtOrderListDTO;
import com.fantechs.common.base.general.dto.mes.pm.history.SmtHtOrderDTO;
import com.fantechs.common.base.general.dto.om.MesOrderMaterialDTO;
import com.fantechs.common.base.general.dto.om.SaveOrderMaterialDTO;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesOrderMaterialListDTO;
import com.fantechs.common.base.general.dto.om.SmtOrderAndMaterialDTO;
import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtOrder;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.SmtOrderService;
import com.fantechs.provider.om.service.ht.SmtHtOrderService;
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
import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/10/13.
 */
@RestController
@Api(tags = "订单管理")
@RequestMapping("/smtOrder")
@Validated
public class SmtOrderController {

    @Autowired
    private SmtOrderService smtOrderService;
    @Autowired
    private SmtHtOrderService smtHtOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "订单对象信息",required = true)@RequestBody SmtOrder smtOrder) {
        return ControllerUtil.returnCRUD(smtOrderService.save(smtOrder));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/save")
    public ResponseEntity save(@ApiParam(value = "销售订单对象信息",required = true)@RequestBody SaveOrderMaterialDTO saveOrderMaterialDTO) {
        return ControllerUtil.returnCRUD(smtOrderService.saveOrderMaterial(saveOrderMaterialDTO));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody SmtOrder smtOrder) {
        return ControllerUtil.returnCRUD(smtOrderService.update(smtOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtOrder  smtOrder = smtOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtOrder,StringUtils.isEmpty(smtOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtOrder searchSmtOrder) {
        Page<Object> page = PageHelper.startPage(searchSmtOrder.getStartPage(),searchSmtOrder.getPageSize());
        List<SmtOrderDto> list = smtOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation(value = "获取订单履历列表",notes = "获取订单履历列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtOrderDTO>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtHtOrderListDTO searchSmtHtOrderListDTO){
        Page<Object> page = PageHelper.startPage(searchSmtHtOrderListDTO.getStartPage(),searchSmtHtOrderListDTO.getPageSize());
        List<SmtHtOrderDTO> smtHtOrderDTOList = smtHtOrderService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchSmtHtOrderListDTO));
        return ControllerUtil.returnDataSuccess(smtHtOrderDTOList,(int)page.getTotal());
    }

    @ApiOperation("列表及子列表")
    @PostMapping("/findListAndChildren")
    public ResponseEntity<List<SmtOrderAndMaterialDTO>> findListAndChildren(@ApiParam(value = "查询对象")@RequestBody SearchSmtOrder searchSmtOrder) {
        List<SmtOrderAndMaterialDTO> smtOrderAndMaterialDTOList=new LinkedList<>();
        Page<Object> page = PageHelper.startPage(searchSmtOrder.getStartPage(),searchSmtOrder.getPageSize());
        List<SmtOrderDto> list = smtOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtOrder));
        if(StringUtils.isNotEmpty(list)){
            for (SmtOrderDto smtOrderDto : list) {
                SmtOrderAndMaterialDTO smtOrderAndMaterialDTO = new SmtOrderAndMaterialDTO();
                smtOrderAndMaterialDTO.setSmtOrderDto(smtOrderDto);
                SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO = new SearchMesOrderMaterialListDTO();
                searchMesOrderMaterialListDTO.setOrderId(smtOrderDto.getOrderId());
                searchMesOrderMaterialListDTO.setScheduleStatus((byte)0);
                List<MesOrderMaterialDTO> orderMaterial = smtOrderService.findOrderMaterial(searchMesOrderMaterialListDTO);
                smtOrderAndMaterialDTO.setMesOrderMaterialDTOList(orderMaterial);
                smtOrderAndMaterialDTOList.add(smtOrderAndMaterialDTO);
            }
        }
        return ControllerUtil.returnDataSuccess(smtOrderAndMaterialDTOList,(int)page.getTotal());
    }

    @ApiOperation("获取订单产品详情")
    @PostMapping("/orderMaterialDetail")
    public ResponseEntity<List<MesOrderMaterialDTO>> orderMaterialDetail(@ApiParam(value = "订单ID",required = true)@RequestParam Long orderId) {
        SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO = new SearchMesOrderMaterialListDTO();
        searchMesOrderMaterialListDTO.setOrderId(orderId);
        List<MesOrderMaterialDTO> orderMaterial = smtOrderService.findOrderMaterial(searchMesOrderMaterialListDTO);
        return  ControllerUtil.returnDataSuccess(orderMaterial,StringUtils.isEmpty(orderMaterial)?0:orderMaterial.size());
    }

    @PostMapping(value = "/export",produces = "application/octet-stream")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtOrder searchSmtOrder){
    List<SmtOrderDto> list = smtOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtOrder信息", SmtOrderDto.class, "SmtOrderDto.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
