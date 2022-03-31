package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;
import com.fantechs.provider.mes.sfc.service.MesSfcScanBarcodeService;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MesSfcScanBarcodeServiceImpl implements MesSfcScanBarcodeService {

    @Override
    public BaseExecuteResultDto chkLogUserInfo(RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto) throws Exception {

        BaseExecuteResultDto baseExecuteResultDto=new BaseExecuteResultDto();
        try {
            baseExecuteResultDto=BarcodeUtils.ChkLogUserInfo(restapiChkLogUserInfoApiDto);
            if(baseExecuteResultDto.getIsSuccess()==false)
                throw new Exception(baseExecuteResultDto.getFailMsg());
        }
        catch (Exception ex){
            baseExecuteResultDto.setIsSuccess(false);
            baseExecuteResultDto.setFailMsg(ex.getMessage());
        }

        return baseExecuteResultDto;
    }

}
