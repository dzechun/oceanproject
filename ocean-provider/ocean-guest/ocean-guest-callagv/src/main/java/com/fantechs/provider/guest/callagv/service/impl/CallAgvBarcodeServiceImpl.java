package com.fantechs.provider.guest.callagv.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.CallAgvBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.mapper.CallAgvBarcodeMapper;
import com.fantechs.provider.guest.callagv.service.CallAgvBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CallAgvBarcodeServiceImpl extends BaseService<CallAgvBarcode> implements CallAgvBarcodeService {

    @Resource
    private CallAgvBarcodeMapper callAgvBarcodeMapper;

    @Override
    public List<CallAgvBarcodeDto> findList(Map<String, Object> map) {
        return callAgvBarcodeMapper.findList(map);
    }

    @Override
    @Transactional
    public int save(CallAgvBarcode callAgvBarcode) {

        Example example = new Example(CallAgvBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode", callAgvBarcode.getBarcode());
        if (StringUtils.isNotEmpty(callAgvBarcodeMapper.selectByExample(example))) {
            throw new BizErrorException("条码重复");
        }
        callAgvBarcode.setBarcodeStatus((byte) 1);

        return callAgvBarcodeMapper.insertSelective(callAgvBarcode);
    }
}
