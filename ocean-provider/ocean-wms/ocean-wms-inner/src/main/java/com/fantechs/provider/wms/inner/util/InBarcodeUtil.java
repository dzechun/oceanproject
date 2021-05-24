package com.fantechs.provider.wms.inner.util;

import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.utils.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
//    @Resource
//    private SFCFeignApi sfcFeignApi;

    private static InBarcodeUtil inBarcodeUtil;

    //初始化
    @PostConstruct
    public void init(){
        inBarcodeUtil = this;
        //inBarcodeUtil.sfcFeignApi = sfcFeignApi;
    }

    public static Map<String,Object> checkBarCode(String barCode){
        Map<String,Object> map = new HashMap<>();
        //MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = inBarcodeUtil.sfcFeignApi.findBarcode(barCode).getData();
        MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = null;
        if(StringUtils.isEmpty(mesSfcWorkOrderBarcode)){
            //查询箱码
        }else{

        }
        map.put("id",mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
        map.put("barcode",mesSfcWorkOrderBarcode.getBarcode());
        return map;
    }
}
