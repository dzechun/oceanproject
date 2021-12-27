package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.service.PDAWmsInnerCartonSplitAndCombineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/5/10
 */
@Service
public class PDAWmsInnerCartonSplitAndCombineServiceImpl implements PDAWmsInnerCartonSplitAndCombineService {

    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int checkCartonCode(String cartonCode,Integer type) {
        Map<String,Object> map = new HashMap<>();
        map.put("cartonCode",cartonCode);
        List<WmsInnerInventoryDetDto> inventoryDetDtoList = wmsInnerInventoryDetMapper.findList(map);
        if(StringUtils.isEmpty(inventoryDetDtoList)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"找不到该箱码信息");
        }

        return 1;
    }

}
