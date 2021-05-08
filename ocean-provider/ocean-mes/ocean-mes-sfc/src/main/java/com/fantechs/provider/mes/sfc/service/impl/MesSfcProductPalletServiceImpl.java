package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductPalletMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/08.
 */
@Service
public class MesSfcProductPalletServiceImpl extends BaseService<MesSfcProductPallet> implements MesSfcProductPalletService {

    @Resource
    private MesSfcProductPalletMapper mesSfcProductPalletMapper;

    @Override
    public List<MesSfcProductPalletDto> findList(Map<String, Object> map) {
        return mesSfcProductPalletMapper.findList(map);
    }
}
