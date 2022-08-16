package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.dto.basic.BaseBarCodeDto;
import com.fantechs.common.base.general.dto.basic.BaseBarCodeWorkDto;
import com.fantechs.common.base.general.entity.basic.BaseBarCode;
import com.fantechs.common.base.general.entity.basic.BaseBarCodeDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarCode;
import com.fantechs.common.base.support.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/22.
*/
public interface BaseBarCodeService extends IService<BaseBarCode> {
    List<BaseBarCodeDto> findList(Map<String, Object> map);

    BaseBarCodeWorkDto work(SearchBaseBarCode searchBaseBarCode);

    void download(List<String> savePath, HttpServletResponse response) throws UnsupportedEncodingException;

    int print(Long workOrderId);

    BaseBarCodeDet verifyQrCode(String QrCode, Long workOrderId);

    int saveCode(BaseBarCodeWorkDto baseBarCodeWorkDto);

    int updateByContent(List<BaseBarCodeDet> baseBarCodeDets);

    List<BaseBarCodeDto> reprintList(String workOrderId);

    int reprint(List<String> barCodeId);
}
