package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSelectToUseBarcodeDto;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSelectToUseSubmitDto;
import com.fantechs.common.base.general.entity.qms.QmsBadnessManage;
import com.fantechs.common.base.general.entity.qms.QmsBadnessManageBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.qms.mapper.QmsBadnessManageBarcodeMapper;
import com.fantechs.provider.qms.mapper.QmsBadnessManageMapper;
import com.fantechs.provider.qms.service.QmsBadnessManageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@Service
public class QmsBadnessManageServiceImpl extends BaseService<QmsBadnessManage> implements QmsBadnessManageService {

    @Resource
    private QmsBadnessManageMapper qmsBadnessManageMapper;
    @Resource
    private QmsBadnessManageBarcodeMapper qmsBadnessManageBarcodeMapper;

    @Override
    public List<QmsBadnessManage> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsBadnessManageMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String checkBarcode(String barcode,Long incomingInspectionOrderId) {
        //校验条码

        return barcode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(PdaIncomingSelectToUseSubmitDto pdaIncomingSelectToUseSubmitDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //提交
        int i = 0;
        Byte pickResult = pdaIncomingSelectToUseSubmitDto.getPickResult();
        BigDecimal orderQty = pdaIncomingSelectToUseSubmitDto.getOrderQty();
        List<PdaIncomingSelectToUseBarcodeDto> barcodeDtoList = pdaIncomingSelectToUseSubmitDto.getBarcodeDtoList();
        QmsBadnessManage qmsBadnessManage = new QmsBadnessManage();
        List<QmsBadnessManageBarcode> barcodeList = new LinkedList<>();

        qmsBadnessManage.setIncomingInspectionOrderId(pdaIncomingSelectToUseSubmitDto.getIncomingInspectionOrderId());
        if(pickResult == 1){
            qmsBadnessManage.setSpecialReceiveQty(new BigDecimal(barcodeDtoList.size()));
            qmsBadnessManage.setReturnQty(orderQty.subtract(new BigDecimal(barcodeDtoList.size())));
        }else if(pickResult == 0){
            qmsBadnessManage.setSpecialReceiveQty(orderQty.subtract(new BigDecimal(barcodeDtoList.size())));
            qmsBadnessManage.setReturnQty(new BigDecimal(barcodeDtoList.size()));
        }
        qmsBadnessManage.setOperatorUserId(user.getUserId());
        qmsBadnessManage.setCreateUserId(user.getUserId());
        qmsBadnessManage.setCreateTime(new Date());
        qmsBadnessManage.setModifiedUserId(user.getUserId());
        qmsBadnessManage.setModifiedTime(new Date());
        qmsBadnessManage.setStatus((byte)1);
        qmsBadnessManage.setOrgId(user.getOrganizationId());
        i = qmsBadnessManageMapper.insertUseGeneratedKeys(qmsBadnessManage);

        for (PdaIncomingSelectToUseBarcodeDto barcodeDto : barcodeDtoList){
            QmsBadnessManageBarcode qmsBadnessManageBarcode = new QmsBadnessManageBarcode();
            qmsBadnessManageBarcode.setBadnessManageId(qmsBadnessManage.getBadnessManageId());
            qmsBadnessManageBarcode.setBarcode(barcodeDto.getBarcode());
            qmsBadnessManageBarcode.setPickResult(pickResult);
            qmsBadnessManageBarcode.setCreateUserId(user.getUserId());
            qmsBadnessManageBarcode.setCreateTime(new Date());
            qmsBadnessManageBarcode.setModifiedUserId(user.getUserId());
            qmsBadnessManageBarcode.setModifiedTime(new Date());
            qmsBadnessManageBarcode.setStatus((byte)1);
            qmsBadnessManageBarcode.setOrgId(user.getOrganizationId());
            barcodeList.add(qmsBadnessManageBarcode);
        }
        qmsBadnessManageBarcodeMapper.insertList(barcodeList);

        return i;
    }

}
