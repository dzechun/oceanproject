package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeReprintDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcodeReprint;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeReprintMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeReprintService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/28.
 */
@Service
public class MesSfcWorkOrderBarcodeReprintServiceImpl extends BaseService<MesSfcWorkOrderBarcodeReprint> implements MesSfcWorkOrderBarcodeReprintService {

    @Resource
    private MesSfcWorkOrderBarcodeReprintMapper mesSfcWorkOrderBarcodeReprintMapper;

    @Override
    public List<MesSfcWorkOrderBarcodeReprintDto> findList(Map<String, Object> map) {
        return mesSfcWorkOrderBarcodeReprintMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<MesSfcWorkOrderBarcodeReprint> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
