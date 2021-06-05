package com.fantechs.provider.wms.inner.util;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDetDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcProductPalletDet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderReMspp;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderReMsppMapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 入库条码校验
 * @author mr.lei
 */
@Component
public class InBarcodeUtil {
    //Feign
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource

    private static InBarcodeUtil inBarcodeUtil;

    //初始化
    @PostConstruct
    public void init(){
        inBarcodeUtil = this;
        inBarcodeUtil.sfcFeignApi = sfcFeignApi;
    }

    /**
     * 校验成品条码获取栈板数量
     * @param workOrderId
     * @param barCode
     * @return
     */
    public static BigDecimal checkBarCode(Long workOrderId, String barCode){
        MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = inBarcodeUtil.sfcFeignApi.findBarcode(barCode).getData();
        if(StringUtils.isEmpty(mesSfcWorkOrderBarcode)){
           throw new BizErrorException("不存在该条码");
        }
        //条码是否为工单条码且是否为对应工单
        if(mesSfcWorkOrderBarcode.getBarcodeType()==(byte)1 && mesSfcWorkOrderBarcode.getWorkOrderId()!=workOrderId){
            throw new BizErrorException("该条码不属于该工单");
        }
        //查询工单条码关联展板id
        SearchMesSfcProductPalletDet searchMesSfcProductPalletDet = new SearchMesSfcProductPalletDet();
        searchMesSfcProductPalletDet.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
        ResponseEntity<List<MesSfcProductPalletDetDto>> responseEntity = inBarcodeUtil.sfcFeignApi.findList(searchMesSfcProductPalletDet);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException("检验条码失败");
        }
        List<MesSfcProductPalletDetDto> mesSfcProductPalletDetDtos = responseEntity.getData();
        BigDecimal qty = BigDecimal.ZERO;
        //统计栈板数量
        for (MesSfcProductPalletDetDto mesSfcProductPalletDetDto : mesSfcProductPalletDetDtos) {
            MesSfcProductPallet mesSfcProductPallet = inBarcodeUtil.sfcFeignApi.detail(mesSfcProductPalletDetDto.getProductPalletId()).getData();
            if(!StringUtils.isEmpty(mesSfcProductPallet) && !StringUtils.isEmpty(mesSfcProductPallet.getNowPackageSpecQty())){
                qty.add(mesSfcProductPallet.getNowPackageSpecQty());
            }
        }
        //展板查询包装数量
        if(qty.compareTo(BigDecimal.ZERO)==0){
            throw new BizErrorException("暂无数量");
        }
        return qty;
    }
}
