package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.srm.SrmPoExpediteRecordDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.entity.srm.SrmPoExpedite;
import com.fantechs.common.base.general.entity.srm.SrmPoExpediteRecord;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPoExpedite;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtMaterialBarcodeReOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtMaterialBarcodeReOrderMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeReOrderMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeReOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/14.
 */
@Service
public class WmsInnerMaterialBarcodeReOrderServiceImpl extends BaseService<WmsInnerMaterialBarcodeReOrder> implements WmsInnerMaterialBarcodeReOrderService {

    @Resource
    private WmsInnerMaterialBarcodeReOrderMapper wmsInnerMaterialBarcodeReOrderMapper;
    @Resource
    private WmsInnerHtMaterialBarcodeReOrderMapper wmsInnerHtMaterialBarcodeReOrderMapper;

    @Override
    public List<WmsInnerMaterialBarcodeReOrderDto> findList(Map<String, Object> map) {
        return wmsInnerMaterialBarcodeReOrderMapper.findList(map);
    }

    @Override
    public int batchAdd(List<WmsInnerMaterialBarcodeReOrder> list) {
        for (WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder : list) {
            this.save(wmsInnerMaterialBarcodeReOrder);
        }
        return 1;
    }

    @Override
    public int save(WmsInnerMaterialBarcodeReOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setCreateTime(new Date());
        entity.setCreateUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        entity.setOrgId(user.getOrganizationId());

        int i = wmsInnerMaterialBarcodeReOrderMapper.insertUseGeneratedKeys(entity);

        record(entity);

        return i;
    }

    @Override
    public int update(WmsInnerMaterialBarcodeReOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        entity.setOrgId(user.getOrganizationId());

        int i = wmsInnerMaterialBarcodeReOrderMapper.updateByPrimaryKeySelective(entity);

        record(entity);

        return i;
    }

    @Override
    public int batchDelete(String ids) {
        String[] idArry = ids.split(",");

        for (String id : idArry) {
            WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder = wmsInnerMaterialBarcodeReOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(wmsInnerMaterialBarcodeReOrder)){
                continue;
            }
            record(wmsInnerMaterialBarcodeReOrder);
        }

        return wmsInnerMaterialBarcodeReOrderMapper.deleteByIds(ids);
    }

    private void record(WmsInnerMaterialBarcodeReOrder record) {
        WmsInnerHtMaterialBarcodeReOrder wmsInnerHtMaterialBarcodeReOrder = new WmsInnerHtMaterialBarcodeReOrder();
        BeanUtil.copyProperties(record,wmsInnerHtMaterialBarcodeReOrder);
        wmsInnerHtMaterialBarcodeReOrderMapper.insertSelective(wmsInnerHtMaterialBarcodeReOrder);
    }

}
