package com.fantechs.provider.guest.eng.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.restapi.EngReportStockOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.guest.fivering.FiveringFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngContractQtyOrderMapper;
import com.fantechs.provider.guest.eng.mapper.EngDataExportEngPackingOrderMapper;
import com.fantechs.provider.guest.eng.service.EngReportStockOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@Service
public class EngReportStockOrderServiceImpl  implements EngReportStockOrderService {

    @Resource
    private EngDataExportEngPackingOrderMapper engDataExportEngPackingOrderMapper;
    @Resource
    private FiveringFeignApi fiveringFeignApi;
    @Resource
    private EngContractQtyOrderMapper engContractQtyOrderMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;


    @Override
    public String reportStockOrder(List<WmsInnerStockOrderDet> WmsInnerStockOrderDets , WmsInnerStockOrder wmsInnerStockOrder ){
        String jsonVoiceArray="";
        String projectID="3919";
        SearchWmsInnerStockOrder searchWmsInnerStockOrder = new SearchWmsInnerStockOrder();
        searchWmsInnerStockOrder.setStockOrderCode(wmsInnerStockOrder.getStockOrderCode());
        List<WmsInnerStockOrderDto> wmsInnerStockOrderDto = innerFeignApi.findList(searchWmsInnerStockOrder).getData();
        List<EngReportStockOrderDto> engReportStockOrderDtos = new ArrayList<>();
        for(WmsInnerStockOrderDet det : WmsInnerStockOrderDets){
            SearchWmsInnerInventory searchWmsInnerInventory = new SearchWmsInnerInventory();
            searchWmsInnerInventory.setMaterialId(det.getMaterialId());
            searchWmsInnerInventory.setBatchCode(det.getBatchCode());
            searchWmsInnerInventory.setJobStatus((byte)1);
            searchWmsInnerInventory.setWarehouseId(wmsInnerStockOrder.getWarehouseId());
            searchWmsInnerInventory.setStorageId(det.getStorageId());
            List<WmsInnerInventoryDto> wmsInnerInventoryDtos = innerFeignApi.findList(searchWmsInnerInventory).getData();
            if(StringUtils.isEmpty(wmsInnerInventoryDtos)) throw new BizErrorException("未查询到盘点单中对应的库存信息");

            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialId(wmsInnerInventoryDtos.get(0).getMaterialId());
            List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
            if(StringUtils.isEmpty(baseMaterials)) throw new BizErrorException("未查询到盘点单中对应的物料信息");

            Example comtractExample = new Example(EngContractQtyOrder.class);
            comtractExample.createCriteria().andEqualTo("contractCode",wmsInnerInventoryDtos.get(0).getContractCode())
                    .andEqualTo("materialCode",baseMaterials.get(0).getMaterialCode());
            List<EngContractQtyOrder> contractQtyOrders = engContractQtyOrderMapper.selectByExample(comtractExample);
            if(StringUtils.isEmpty(contractQtyOrders)) throw new BizErrorException("未查询到盘点单中对应的合同量单");

            EngReportStockOrderDto dto = new EngReportStockOrderDto();
            dto.setStockOrderId(wmsInnerStockOrder.getStockOrderId());
            dto.setOption1(contractQtyOrders.get(0).getOption1());
            dto.setOption2(contractQtyOrders.get(0).getOption2());
            dto.setContractCode(contractQtyOrders.get(0).getContractCode());
            dto.setPurchaseReqOrderCode(wmsInnerInventoryDtos.get(0).getPurchaseReqOrderCode());
            dto.setMaterialCode(contractQtyOrders.get(0).getMaterialCode());
            dto.setLocationNum(contractQtyOrders.get(0).getLocationNum());
            dto.setDominantTermCode(contractQtyOrders.get(0).getDominantTermCode());
            dto.setDeviceCode(contractQtyOrders.get(0).getDeviceCode());
            dto.setVarianceQty(det.getVarianceQty().toString());
            dto.setInventoryStatusName(wmsInnerInventoryDtos.get(0).getInventoryStatusName());
            dto.setStorageId(det.getStorageId());
            dto.setCreateUserName(wmsInnerStockOrderDto.get(0).getCreateUserName());
            dto.setCreateTime(DateUtil.formatTime(wmsInnerStockOrder.getCreateTime()));

            engReportStockOrderDtos.add(dto);
        }

        jsonVoiceArray= JsonUtils.objectToJson(engReportStockOrderDtos);
        String s0=jsonVoiceArray.replaceAll("stockOrderId","WMSKey");
        String s1=s0.replaceAll("option1","PPGUID");
        String s2=s1.replaceAll("option2","PSGUID");
        String s3=s2.replaceAll("contractCode","合同号");
        String s4=s3.replaceAll("purchaseReqOrderCode","请购单号");
        String s5=s4.replaceAll("materialCode","材料编码");
        String s6=s5.replaceAll("locationNum","位号");
        String s7=s6.replaceAll("dominantTermCode","主项号");
        String s8=s7.replaceAll("deviceCode","装置号");
        String s9=s8.replaceAll("varianceQty","变化量");
        String s10=s9.replaceAll("inventoryStatusName","材料状态");
        String s11=s10.replaceAll("storageId","DHGUID");
        String s12=s11.replaceAll("createTime","登记时间");
        String s13=s12.replaceAll("createUserName","登记人");


        ResponseEntity<String>  responseEntityResult=fiveringFeignApi.writeMakeInventoryDetails(s12,projectID);

        return responseEntityResult.getData();
    }


}
