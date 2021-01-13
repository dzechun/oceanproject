package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.general.dto.wms.out.WmsOutShippingNoteDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNoteDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutShippingNotePallet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.out.mapper.WmsOutShippingNoteDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutShippingNotePalletMapper;
import com.fantechs.provider.wms.out.service.WmsOutShippingNoteDetService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@Service
public class WmsOutShippingNoteDetServiceImpl  extends BaseService<WmsOutShippingNoteDet> implements WmsOutShippingNoteDetService {

    @Resource
    private WmsOutShippingNoteDetMapper wmsOutShippingNoteDetMapper;
    @Resource
    private WmsOutShippingNotePalletMapper wmsOutShippingNotePalletMapper;

    @Override
    public List<WmsOutShippingNoteDetDto> findList(Map<String, Object> map) {

        List<WmsOutShippingNoteDetDto> list = wmsOutShippingNoteDetMapper.findList(map);

        for (WmsOutShippingNoteDetDto wmsOutShippingNoteDetDto : list) {
            Example example = new Example(WmsOutShippingNotePallet.class);
            example.createCriteria().andEqualTo("shippingNoteDetId",wmsOutShippingNoteDetDto.getShippingNoteDetId());
            wmsOutShippingNoteDetDto.setStockPalletList(wmsOutShippingNotePalletMapper.selectByExample(example).stream().map(WmsOutShippingNotePallet::getPalletCode).collect(Collectors.toList()));
        }
        return list;
    }
}
