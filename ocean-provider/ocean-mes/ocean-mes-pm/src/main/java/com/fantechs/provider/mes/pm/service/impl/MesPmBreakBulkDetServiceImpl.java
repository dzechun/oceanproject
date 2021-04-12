package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDetDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulk;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulkDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmBreakBulkDetMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmBreakBulkMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmBreakBulkDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/01/18.
 */
@Service
public class MesPmBreakBulkDetServiceImpl extends BaseService<MesPmBreakBulkDet> implements MesPmBreakBulkDetService {

    @Resource
    private MesPmBreakBulkDetMapper mesPmBreakBulkDetMapper;
    @Resource
    private MesPmBreakBulkMapper mesPmBreakBulkMapper;
    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;

    @Override
    public List<MesPmBreakBulkDetDto> findList(Map<String, Object> map) {
        List<MesPmBreakBulkDetDto> list = mesPmBreakBulkDetMapper.findList(map);
        list.forEach(li->{
            Long routeId = li.getRouteId();
            //查询工艺路线配置
            List<BaseRouteProcess> routeProcesses = mesPmWorkOrderMapper.selectRouteProcessByRouteId(routeId);
            if (StringUtils.isNotEmpty(routeProcesses)) {
                StringBuffer sb =new StringBuffer();
                for (BaseRouteProcess routeProcess : routeProcesses) {
                    sb.append(routeProcess.getProcessName()+"-");
                }
                li.setProcessLink(sb.substring(0,sb.length()-1));
            }
            MesPmBreakBulk mesPmBreakBulk = mesPmBreakBulkMapper.selectByPrimaryKey(li.getBreakBulkId());
            li.setMesPmBreakBulk(mesPmBreakBulk);
        });
        return list;
    }
}
