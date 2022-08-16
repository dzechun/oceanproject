package com.fantechs.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessRecordDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcessRecord;
import com.fantechs.common.base.general.entity.ureport.MesSfcBarcodeProcessReport;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.mapper.MesSfcBarcodeProcessReportMapper;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.service.MesSfcBarcodeProcessReportService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    public List<MesSfcBarcodeProcessReport>  findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());

        String barCode = StringUtils.isNotEmpty(map.get("barcode"))?map.get("barcode").toString():"";

        //判断客户条码
        String customerBarcode = StringUtils.isNotEmpty(map.get("customerBarcode"))?map.get("customerBarcode").toString():"";
        if (StringUtils.isNotEmpty(customerBarcode)){
            map.put("type",1);
            customerBarcode = mesSfcBarcodeProcessReportMapper.findProductBarcodeList(map);
            if (StringUtils.isEmpty(customerBarcode)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "客户条码不存在");
            }
            map.put("barcode",customerBarcode);
        }
        //判断部件条码
        String partBarcode = StringUtils.isNotEmpty(map.get("partBarcode"))?map.get("partBarcode").toString():"";
        if (StringUtils.isNotEmpty(partBarcode)){
            map.put("type",2);
            partBarcode = mesSfcBarcodeProcessReportMapper.findProductBarcodeList(map);
            if (StringUtils.isEmpty(partBarcode)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "部件条码不存在");
            }
            map.put("barcode",partBarcode);
        }
        if (StringUtils.isNotEmpty(partBarcode,customerBarcode) && !customerBarcode.equals(partBarcode)){
            throw new BizErrorException("当前客户条码与部件条码不属于同一个成品条码");
        }
        if (StringUtils.isNotEmpty(barCode) && (((StringUtils.isNotEmpty(partBarcode) && !barCode.equals(partBarcode)) || (StringUtils.isNotEmpty(customerBarcode) && !barCode.equals(customerBarcode))))){
            throw new BizErrorException("客户条码或部件条码与成品条码不一致");
        }
        if (StringUtils.isEmpty(map.get("barcode"))) {
            throw new BizErrorException("查询失败，未找到条码");
        }

        List<MesSfcBarcodeProcessReport> list = mesSfcBarcodeProcessReportMapper.findList(map);
        if (StringUtils.isNotEmpty(list)) {
            MesSfcBarcodeProcessReport mesSfcBarcodeProcessReport = list.get(0);
            Page<Object> page = PageHelper.startPage(Integer.valueOf(map.get("startPage").toString()),Integer.valueOf(map.get("pageSize").toString()));
            MesSfcBarcodeProcessReport recordList = findRecordList(map);
            recordList.setBarcode(mesSfcBarcodeProcessReport.getBarcode());
            recordList.setWorkOrderCode(mesSfcBarcodeProcessReport.getWorkOrderCode());
            recordList.setWorkOrderStatus(mesSfcBarcodeProcessReport.getWorkOrderStatus());
            recordList.setProductModelName(mesSfcBarcodeProcessReport.getProductModelName());
            recordList.setProLineName(mesSfcBarcodeProcessReport.getProLineName());
            recordList.setRouteName(mesSfcBarcodeProcessReport.getRouteName());
            recordList.setMaterialCode(mesSfcBarcodeProcessReport.getMaterialCode());
            recordList.setMaterialName(mesSfcBarcodeProcessReport.getMaterialName());
//            BeanUtil.copyProperties(mesSfcBarcodeProcessReport,recordList);
            recordList.setTotal((int) page.getTotal());
            list.set(0,recordList);
        }

        return list;
    }

    @Override
    public MesSfcBarcodeProcessReport findRecordList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        MesSfcBarcodeProcessReport mesSfcBarcodeProcessReport = new MesSfcBarcodeProcessReport();
        Object tabId = map.get("tabId");
        map.put("orgId",user.getOrganizationId());
        if (StringUtils.isEmpty(map.get("barcode")) && StringUtils.isEmpty(map.get("customerBarcode"))) {
            return null;
        }
        if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 1 ||
                (StringUtils.isNotEmpty(map.get("export")) && Integer.valueOf(map.get("export").toString())==1)) {
            SearchMesSfcBarcodeProcessRecord record = new SearchMesSfcBarcodeProcessRecord();
            if(StringUtils.isNotEmpty(map.get("customerBarcode"))){
                record.setCustomerBarcode(map.get("customerBarcode").toString());
                record.setBarcode("");
            }
            else if(StringUtils.isNotEmpty(map.get("customerBarcode")) && StringUtils.isNotEmpty(map.get("barcode"))){
                record.setCustomerBarcode("");
                record.setBarcode(map.get("barcode").toString());
            }
            else{
                record.setBarcode(map.get("barcode").toString());
            }
            record.setCodeQueryMark(1);
            record.setPageSize(StringUtils.isNotEmpty(map.get("pageSize")) ?Integer.valueOf(map.get("pageSize").toString()):10);
            record.setStartPage(StringUtils.isNotEmpty(map.get("startPage")) ?Integer.valueOf(map.get("startPage").toString()):1);

            //record.setBarcode(map.get("barcode").toString());
            ResponseEntity<List<MesSfcBarcodeProcessRecordDto>> data = sfcFeignApi.findList(record);
            List<MesSfcBarcodeProcessRecordDto> list = data.getData();
            PageHelper.getLocalPage().setTotal(data.getCount());
            if (StringUtils.isNotEmpty(list)) {
                mesSfcBarcodeProcessReport.setBarcodeList(list);
            }
        }
        if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 2 ||
                (StringUtils.isNotEmpty(map.get("export")) && Integer.valueOf(map.get("export").toString())==1)) {
            mesSfcBarcodeProcessReport.setInspectionList(mesSfcBarcodeProcessReportMapper.findInspectionList(map));
        }
        if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 3 ||
                (StringUtils.isNotEmpty(map.get("export")) && Integer.valueOf(map.get("export").toString())==1)) {
            mesSfcBarcodeProcessReport.setBoxList(mesSfcBarcodeProcessReportMapper.findBoxList(map));
        }
        if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 4 ||
                (StringUtils.isNotEmpty(map.get("export")) && Integer.valueOf(map.get("export").toString())==1)) {
            mesSfcBarcodeProcessReport.setPalletList(mesSfcBarcodeProcessReportMapper.findPalletList(map));
        }
        if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 5 ||
                (StringUtils.isNotEmpty(map.get("export")) && Integer.valueOf(map.get("export").toString())==1)) {
            mesSfcBarcodeProcessReport.setReworkList(mesSfcBarcodeProcessReportMapper.findReworkList(map));
        }
        if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 6 ||
                (StringUtils.isNotEmpty(map.get("export")) && Integer.valueOf(map.get("export").toString())==1)) {
            //mesSfcBarcodeProcessReport.setEquipmentParameterList(mesSfcBarcodeProcessReportMapper.findEquipmentParameterList(map));
        }
        if (StringUtils.isNotEmpty(tabId) && Integer.valueOf(tabId.toString()) == 7 ||
                (StringUtils.isNotEmpty(map.get("export")) && Integer.valueOf(map.get("export").toString())==1)) {
            mesSfcBarcodeProcessReport.setAssemblyList(mesSfcBarcodeProcessReportMapper.findAssemblyList(map));
        }
        return mesSfcBarcodeProcessReport;
    }


}
