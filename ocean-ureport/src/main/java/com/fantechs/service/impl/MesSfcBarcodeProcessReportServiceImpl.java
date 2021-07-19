package com.fantechs.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcessRecord;
import com.fantechs.common.base.general.entity.ureport.MesSfcBarcodeProcessReport;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.mapper.MesSfcBarcodeProcessReportMapper;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.service.MesSfcBarcodeProcessReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class MesSfcBarcodeProcessReportServiceImpl extends BaseService<MesSfcBarcodeProcessReport> implements MesSfcBarcodeProcessReportService {

    @Resource
    private MesSfcBarcodeProcessReportMapper mesSfcBarcodeProcessReportMapper;

    @Resource
    SFCFeignApi sfcFeignApi;

    @Override
    public List<MesSfcBarcodeProcessReport> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());

        String barCode = StringUtils.isNotEmpty(map.get("barCode"))?map.get("barCode").toString():"";

        //判断客户条码
        String customerBarcode = StringUtils.isNotEmpty(map.get("customerBarcode"))?map.get("customerBarcode").toString():"";
        if (StringUtils.isNotEmpty(customerBarcode)){
            map.put("type",1);
            customerBarcode = mesSfcBarcodeProcessReportMapper.findProductBarcodeList(map);
            if (StringUtils.isEmpty(customerBarcode)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "客户条码不存在");
            }
        }
        //判断部件条码
        String partBarcode = StringUtils.isNotEmpty(map.get("partBarcode"))?map.get("partBarcode").toString():"";
        if (StringUtils.isNotEmpty(partBarcode)){
            map.put("type",2);
            partBarcode = mesSfcBarcodeProcessReportMapper.findProductBarcodeList(map);
            if (StringUtils.isEmpty(partBarcode)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003, "部件条码不存在");
            }
        }
        if (StringUtils.isNotEmpty(partBarcode,customerBarcode) && !customerBarcode.equals(partBarcode)){
            throw new BizErrorException("当前客户条码与部件条码不属于同一个成品条码");
        }
        if (StringUtils.isNotEmpty(barCode) && (!barCode.equals(partBarcode) || !barCode.equals(customerBarcode))){
            throw new BizErrorException("客户条码或部件条码与成品条码不一致");
        }

        return mesSfcBarcodeProcessReportMapper.findList(map);
    }

    @Override
    public MesSfcBarcodeProcessReport findRecordList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        MesSfcBarcodeProcessReport mesSfcBarcodeProcessReport = new MesSfcBarcodeProcessReport();
        Object tabId = map.get("tabId");
        if(map.get("barcode") == null){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "条码不能为空");
        }
        map.put("orgId",user.getOrganizationId());
        if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 1) {
            SearchMesSfcBarcodeProcessRecord record = new SearchMesSfcBarcodeProcessRecord();
            record.setBarcode(map.get("barcode").toString());
            mesSfcBarcodeProcessReport.setBarcodeList(sfcFeignApi.findList(record).getData());
        } else if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 2) {
            mesSfcBarcodeProcessReport.setInspectionList(mesSfcBarcodeProcessReportMapper.findInspectionList(map));
        } else if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 3) {
            mesSfcBarcodeProcessReport.setBoxList(mesSfcBarcodeProcessReportMapper.findBoxList(map));
        } else if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 4) {
            mesSfcBarcodeProcessReport.setPalletList(mesSfcBarcodeProcessReportMapper.findPalletList(map));
        } else if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 5) {
            mesSfcBarcodeProcessReport.setReworkList(mesSfcBarcodeProcessReportMapper.findReworkList(map));
        } else if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 6) {
            mesSfcBarcodeProcessReport.setEquipmentParameterList(mesSfcBarcodeProcessReportMapper.findEquipmentParameterList(map));
        } else if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 7) {
            mesSfcBarcodeProcessReport.setAssemblyList(mesSfcBarcodeProcessReportMapper.findAssemblyList(map));
        }
        return mesSfcBarcodeProcessReport;
    }


}
