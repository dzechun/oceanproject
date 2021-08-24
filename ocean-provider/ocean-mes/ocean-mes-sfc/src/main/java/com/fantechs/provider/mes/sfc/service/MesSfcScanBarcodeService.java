package com.fantechs.provider.mes.sfc.service;

import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;

public interface MesSfcScanBarcodeService {

    /**
     * 用户登录信息验证
     * @param restapiChkLogUserInfoApiDto
     * @return
     */
    BaseExecuteResultDto chkLogUserInfo(RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto) throws Exception;

    /**
     * 过站信息验证
     * @param restapiChkSNRoutingApiDto
     * @return
     */
    BaseExecuteResultDto chkSnRouting(RestapiChkSNRoutingApiDto restapiChkSNRoutingApiDto) throws Exception;

    /**
     * 条码过站
     * @param restapiSNDataTransferApiDto
     * @return
     */
    BaseExecuteResultDto snDataTransfer(RestapiSNDataTransferApiDto restapiSNDataTransferApiDto) throws Exception;

}
