package com.fantechs.provider.bcm.service;

import com.fantechs.common.base.general.dto.bcm.BcmBarCodeDto;
import com.fantechs.common.base.general.dto.bcm.BcmBarCodeWorkDto;
import com.fantechs.common.base.general.entity.bcm.BcmBarCode;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmBarCode;
import com.fantechs.common.base.support.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/22.
*/
public interface BcmBarCodeService extends IService<BcmBarCode> {
    List<BcmBarCodeDto> findList(SearchBcmBarCode searchBcmBarCode);

    BcmBarCodeWorkDto work(SearchBcmBarCode searchBcmBarCode);

    void download(List<String> savePath, HttpServletResponse response) throws UnsupportedEncodingException;

    int print(Long workOrderId);

    int verifyQrCode(String QrCode,Long workOrderId);
}
