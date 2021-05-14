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
    public List<MesSfcBarcodeProcess> findBarcode(SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess) {
        Example example = new Example(MesSfcBarcodeProcess.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(searchMesSfcBarcodeProcess.getBarCode())){
            criteria.andEqualTo("barcode",searchMesSfcBarcodeProcess.getBarCode());
        }else if(!StringUtils.isEmpty(searchMesSfcBarcodeProcess.getCartonCode())){
            criteria.andEqualTo("cartonCode",searchMesSfcBarcodeProcess.getCartonCode());
        }else if(!StringUtils.isEmpty(searchMesSfcBarcodeProcess.getPalletCode())){
            criteria.andEqualTo("palletCode",searchMesSfcBarcodeProcess.getPalletCode());
        }else if(!StringUtils.isEmpty(searchMesSfcBarcodeProcess.getWorkOrderId())){
            criteria.andEqualTo("workOrderId",searchMesSfcBarcodeProcess.getWorkOrderId());
        }
       List<MesSfcBarcodeProcess> list = mesSfcBarcodeProcessMapper.selectByExample(example);
        return list;
    }
}
