package com.fantechs.provider.qms.service;

import com.fantechs.common.base.general.entity.qms.QmsSamplingPlan;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */
public interface QmsSamplingPlanService extends IService<QmsSamplingPlan> {

    List<QmsSamplingPlan> findList(Map<String, Object> map);
}
