package com.fantechs.provider.restapi.mulinsen.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.mulinsen.NccBdStordoc;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.restapi.mulinsen.mapper.NccBdStordocMapper;
import com.fantechs.provider.restapi.mulinsen.service.NccBdStordocService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class NccBdStordocServiceImpl extends BaseService<NccBdStordoc> implements NccBdStordocService {

    @Resource
    private NccBdStordocMapper nccBdStordocMapper;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Value("${pkGroup}")
    private String pkGroup;

    @Value("${pkOrg}")
    private String pkOrg;

    @Override
    @Transactional
    @LcnTransaction
    public int synchronizeNccBdStordoc() throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(NccBdStordoc.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("pkGroup,", pkGroup).andEqualTo("pkOrg", pkOrg).andNotEqualTo("dataStatus", 1);
        List<NccBdStordoc> nccBdStordocList = nccBdStordocMapper.selectByExample(example);

        SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
        searchBaseWarehouse.setStartPage(1);
        searchBaseWarehouse.setPageSize(999999);
        List<BaseWarehouse> baseWarehouseList = baseFeignApi.findList(searchBaseWarehouse).getData();

        List<BaseWarehouse> baseWarehouseInsertList = new LinkedList<>();
        List<BaseWarehouse> baseWarehouseUpdateList = new LinkedList<>();
        for (NccBdStordoc nccBdStordoc : nccBdStordocList) {
            long count = baseWarehouseList.stream().filter(item -> item.getWarehouseCode().equals(nccBdStordoc.getId())).count();
            if (count <= 0) {
                if (nccBdStordoc.getDr() == 0) {
                    BaseWarehouse baseWarehouse = new BaseWarehouse();
                    baseWarehouse.setWarehouseCode(nccBdStordoc.getId());
                    baseWarehouse.setWarehouseName(nccBdStordoc.getName());
                    baseWarehouse.setStatus(1);
                    baseWarehouse.setCreateUserId(user.getUserId());
                    baseWarehouse.setCreateTime(new Date());
                    baseWarehouse.setOrgId(user.getOrganizationId());
                    baseWarehouse.setIsDelete((byte) 1);
                    baseWarehouseInsertList.add(baseWarehouse);
                }
            } else {
                BaseWarehouse baseWarehouse = baseWarehouseList.stream().filter(item -> item.getWarehouseCode().equals(nccBdStordoc.getId())).findFirst().get();
                baseWarehouse.setWarehouseCode(nccBdStordoc.getId());
                baseWarehouse.setWarehouseName(nccBdStordoc.getName());
                baseWarehouse.setModifiedUserId(user.getUserId());
                baseWarehouse.setModifiedTime(new Date());
                baseWarehouseUpdateList.add(baseWarehouse);
            }

            nccBdStordoc.setDataStatus(1);
            nccBdStordoc.setSyncTime(new Date());
            nccBdStordocList.add(nccBdStordoc);
        }

        if (!baseWarehouseInsertList.isEmpty()) {
            baseFeignApi.batchSave(baseWarehouseInsertList);
        }

        if (!baseWarehouseUpdateList.isEmpty()) {
            baseFeignApi.batchUpdateWarehouseByCode(baseWarehouseUpdateList);
        }

        if (!nccBdStordocList.isEmpty()) {
            nccBdStordocMapper.batchUpdate(nccBdStordocList);
        }

        return nccBdStordocList.size() == 0 ? 1 : nccBdStordocList.size();
    }

}
