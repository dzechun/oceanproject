package com.fantechs.provider.om.controller.ht;

import com.fantechs.common.base.general.entity.om.SmtHtOrder;
import com.fantechs.common.base.general.dto.mes.pm.history.SmtHtOrderDTO;
import com.fantechs.common.base.general.dto.mes.pm.history.SearchSmtHtOrderListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.service.ht.SmtHtOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;
import java.util.List;
/**
 * @Auther: bingo.ren
 * @Date: 2021年1月8日 16:22
 * @Description: 订单历史信息表管理
 * @Version: 1.0
 */
@RestController
@Api(tags = "订单历史信息表管理",basePath = "smtHtOrder")
@RequestMapping("smtHtOrder")
@Slf4j
public class SmtHtOrderController {

    @Resource
    private SmtHtOrderService smtHtOrderService;

    @ApiOperation("查询订单历史信息表列表")
    @PostMapping("list")
    public ResponseEntity<List<SmtHtOrderDTO>> list(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSmtHtOrderListDTO searchSmtHtOrderListDTO
    ){
        Page<Object> page = PageHelper.startPage(searchSmtHtOrderListDTO.getStartPage(), searchSmtHtOrderListDTO.getPageSize());
        List<SmtHtOrderDTO> smtHtOrderDTOList = smtHtOrderService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchSmtHtOrderListDTO));
        return ControllerUtil.returnDataSuccess(smtHtOrderDTOList,(int)page.getTotal());
    }

    @ApiOperation("通过ID查询订单历史信息表")
    @GetMapping("one")
    public ResponseEntity<SmtHtOrder> one(@ApiParam(value = "订单历史信息表对象ID",required = true)@RequestParam Long id){
        SmtHtOrder smtHtOrder = smtHtOrderService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(smtHtOrder, StringUtils.isEmpty(smtHtOrder)?0:1);
    }

    @ApiOperation("增加订单历史信息表数据")
    @PostMapping("add")
    public ResponseEntity add(@ApiParam(value = "订单历史信息表对象",required = true)@RequestBody SmtHtOrder smtHtOrder){
        return ControllerUtil.returnCRUD(smtHtOrderService.save(smtHtOrder));
    }

    @ApiOperation("删除订单历史信息表数据")
    @GetMapping("delete")
    public ResponseEntity delete(@ApiParam(value = "订单历史信息表对象ID",required = true)@RequestParam Long id){
        return ControllerUtil.returnCRUD(smtHtOrderService.deleteByKey(id));
    }

    @ApiOperation("批量删除订单历史信息表数据")
    @GetMapping("batchDelete")
    public ResponseEntity batchDelete(@ApiParam(value = "订单历史信息表对象ID集，多个用英文逗号隔开",required = true)@RequestParam String ids){
        return ControllerUtil.returnCRUD(smtHtOrderService.batchDelete(ids));
    }

    @ApiOperation("修改订单历史信息表数据")
    @PostMapping("update")
    public ResponseEntity update(@ApiParam(value = "订单历史信息表对象，对象ID必传",required = true)@RequestBody SmtHtOrder smtHtOrder){
        return ControllerUtil.returnCRUD(smtHtOrderService.update(smtHtOrder));
    }

    @PostMapping(value = "export",produces = "application/octet-stream")
    @ApiOperation(value = "导出EXCEL")
    public void export(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSmtHtOrderListDTO searchSmtHtOrderListDTO,
            @ApiParam(value = "当前页",required = false,defaultValue = "1")@RequestParam(defaultValue = "1",required = false) int startPage,
            @ApiParam(value = "显示数量",required = false,defaultValue = "10")@RequestParam(defaultValue = "10",required = false) int pageSize,
            HttpServletResponse response){
        Page<Object> page = PageHelper.startPage(startPage, pageSize);
        List<SmtHtOrderDTO> smtHtOrderDTOList = smtHtOrderService.selectFilterAll(ControllerUtil.dynamicConditionByEntity(searchSmtHtOrderListDTO));
        if(StringUtils.isEmpty(smtHtOrderDTOList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012007);
        }
        EasyPoiUtils.exportExcel(smtHtOrderDTOList ,"订单历史信息表信息","订单历史信息表信息", SmtHtOrderDTO.class, "订单历史信息表信息.xls", response);
    }
}
