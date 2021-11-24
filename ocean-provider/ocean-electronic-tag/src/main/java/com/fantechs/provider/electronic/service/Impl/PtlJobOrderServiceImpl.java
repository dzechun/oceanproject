package com.fantechs.provider.electronic.service.Impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDetDto;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.electronic.entity.search.SearchPtlJobOrderDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaReWDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorker;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.electronic.mapper.PtlJobOrderDetMapper;
import com.fantechs.provider.electronic.mapper.PtlJobOrderMapper;
import com.fantechs.provider.electronic.service.PtlJobOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/01.
 */
@Service
public class PtlJobOrderServiceImpl extends BaseService<PtlJobOrder> implements PtlJobOrderService {

    @Resource
    private PtlJobOrderMapper ptlJobOrderMapper;

    @Resource
    private PtlJobOrderDetMapper ptlJobOrderDetMapper;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<PtlJobOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(map.get("warehouseAreaId"))) {
            SearchBaseWorker searchBaseWorker = new SearchBaseWorker();
            searchBaseWorker.setUserId(user.getUserId());
            List<BaseWorkerDto> baseWorkerDtos = baseFeignApi.findList(searchBaseWorker).getData();
            List<Long> warehouseAreaIds = new LinkedList<>();
            if (StringUtils.isNotEmpty(baseWorkerDtos)) {
                if (StringUtils.isNotEmpty(baseWorkerDtos.get(0).getBaseWorkingAreaReWDtoList())) {
                    for (BaseWorkingAreaReWDto baseWorkingAreaReWDto : baseWorkerDtos.get(0).getBaseWorkingAreaReWDtoList()) {
                        warehouseAreaIds.add(baseWorkingAreaReWDto.getWarehouseAreaId());
                    }
                }
            }
            map.put("warehouseAreaIds", warehouseAreaIds);
        }
        map.put("orgId", user.getOrganizationId());
        return ptlJobOrderMapper.findList(map);
    }

    @Override
    @Transactional
    @LcnTransaction
    public int updateByRelatedOrderCode(PtlJobOrder ptlJobOrder) throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(PtlJobOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("relatedOrderCode", ptlJobOrder.getRelatedOrderCode())
                .andEqualTo("orgId", user.getOrganizationId())
                .andEqualTo("status", 1);

        return ptlJobOrderMapper.updateByExampleSelective(ptlJobOrder, example);
    }

    @Override
    public Map<String, Object> export(Map<String, Object> map) {
        List<PtlJobOrderDto> ptlJobOrderDtoList = findList(map);
        List<Long> jobOrderIdList = new LinkedList<>();
        for (PtlJobOrderDto ptlJobOrderDto : ptlJobOrderDtoList) {
            jobOrderIdList.add(ptlJobOrderDto.getJobOrderId());
        }
        SearchPtlJobOrderDet searchPtlJobOrderDet = new SearchPtlJobOrderDet();
        searchPtlJobOrderDet.setJobOrderIdList(jobOrderIdList);
        List<PtlJobOrderDetDto> ptlJobOrderDetDtoList = ptlJobOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchPtlJobOrderDet));

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("拣货任务单", ptlJobOrderDtoList);
        resultMap.put("拣货任务单明细", ptlJobOrderDetDtoList);
        return resultMap;
    }
}
