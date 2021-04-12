package com.fantechs.provider.mes.sfc.util;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 条码工具类
 *
 * @author hyc
 * @version 1.0
 * @date 2021/04/09 17:23
 **/
public class BarcodeUtils {

    @Resource
    private static MesSfcWorkOrderBarcodeService mesSfcWorkOrderBarcodeService;

    /**
     * @param barCode   产品条码
     * @param processId 工序ID
     *                  1．传【产品条码、工序ID】参数；
     *                  2．系统检查产品条码是否正确，不正确返回错误信息结束；
     *                  3．系统检查条码流程是否正确，不正确返回错误信息结束；
     *                  4．系统检查条码状态是否正确，如是否挂起、维修、完工等状态返回错误信息结束；
     *                  5．系统检查条码工单状态是否正确，如是否挂起、完工、投产数量>=工单数等状态返回错误信息结束；
     *                  6．系统条码检查OK；
     * @return
     */
    public static Boolean checkSN(String barCode, Integer processId) throws Exception{

        //判断条码是否正确（是否存在）
        List<MesSfcWorkOrderBarcodeDto> mesSfcWorkOrderBarcodeDtos = mesSfcWorkOrderBarcodeService.findList((SearchMesSfcWorkOrderBarcode) ControllerUtil.dynamicCondition(
                "barcode",barCode
        ));
        if(mesSfcWorkOrderBarcodeDtos.isEmpty()){
            throw new BizErrorException("条码不存在");
        }
        if(mesSfcWorkOrderBarcodeDtos.size() > 1){
            throw new BizErrorException("条码重复");
        }
        MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtos.get(0);
        //判断条码流程是否正确

        //判断条码状态是否正确
        if(mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 2 || mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 3){
            throw new BizErrorException("条码状态不正确：" + mesSfcWorkOrderBarcodeDto.getBarcodeStatus());
        }



        return false;
    }

}
