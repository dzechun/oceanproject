package com.fantechs.provider.mes.sfc.service.impl;


import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeProcessMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/09.
 */
@Service
public class MesSfcBarcodeProcessServiceImpl extends BaseService<MesSfcBarcodeProcess> implements MesSfcBarcodeProcessService {

    @Resource
    private MesSfcBarcodeProcessMapper mesSfcBarcodeProcessMapper;

    @Override
    public List<MesSfcBarcodeProcessDto> findList(Map<String, Object> map) {
        return mesSfcBarcodeProcessMapper.findList(map);
    }

    @Override
    public List<MesSfcBarcodeProcess> findByPOGroup(Map<String, Object> map) {
        return mesSfcBarcodeProcessMapper.findByPOGroup(map);
    }

    @Override
    public List<MesSfcBarcodeProcess> findByPalletPOGroup(Map<String, Object> map) {
        return mesSfcBarcodeProcessMapper.findByPalletPOGroup(map);
    }

    @Override
    public List<MesSfcBarcodeProcess> findBarcode(SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess) {
        Example example = new Example(MesSfcBarcodeProcess.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(searchMesSfcBarcodeProcess.getBarcode())){
            criteria.andEqualTo("barcode",searchMesSfcBarcodeProcess.getBarcode());
        }else if(!StringUtils.isEmpty(searchMesSfcBarcodeProcess.getCartonCode())){
            criteria.andEqualTo("cartonCode",searchMesSfcBarcodeProcess.getCartonCode());
        }else if(!StringUtils.isEmpty(searchMesSfcBarcodeProcess.getPalletCode())){
            criteria.andEqualTo("palletCode",searchMesSfcBarcodeProcess.getPalletCode());
        }else if(!StringUtils.isEmpty(searchMesSfcBarcodeProcess.getWorkOrderId())){
            criteria.andEqualTo("workOrderId",searchMesSfcBarcodeProcess.getWorkOrderId());
        }else if(!StringUtils.isEmpty(searchMesSfcBarcodeProcess.getMaterialId())){
            criteria.andEqualTo("materialId",searchMesSfcBarcodeProcess.getMaterialId());
        }
       List<MesSfcBarcodeProcess> list = mesSfcBarcodeProcessMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<MesSfcBarcodeProcess> findNextProcessIsPallet(Map<String, Object> map) {
        return mesSfcBarcodeProcessMapper.findNextProcessIsPallet(map);
    }

    @Override
    public int batchUpdate(List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList) {
        return mesSfcBarcodeProcessMapper.batchUpdate(mesSfcBarcodeProcessList);
    }

    @Override
    public String countBarcodeListForPOGroup(Map<String, Object> map) {
        return mesSfcBarcodeProcessMapper.countBarcodeListForPOGroup(map);
    }

    @Override
    public String countBarcodeListForSalesOrder(Map<String, Object> map) {
        return mesSfcBarcodeProcessMapper.countBarcodeListForSalesOrder(map);
    }
}
