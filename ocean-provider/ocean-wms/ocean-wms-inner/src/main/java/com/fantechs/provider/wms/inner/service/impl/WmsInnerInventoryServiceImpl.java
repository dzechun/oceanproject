package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsHtInnerInventory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsHtInnerInventoryMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/07.
 */
@Service
public class WmsInnerInventoryServiceImpl extends BaseService<WmsInnerInventory> implements WmsInnerInventoryService {

    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    @Resource
    private WmsHtInnerInventoryMapper wmsHtInnerInventoryMapper;

    @Override
    public List<WmsInnerInventoryDto> findList(Map<String, Object> map) {
        return wmsInnerInventoryMapper.findList(map);
    }

    @Override
    public int lock(Long id, BigDecimal quantity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(WmsInnerInventory.class);
        example.createCriteria().andEqualTo("inventoryId",id);
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if (wmsInnerInventory.getLockStatus() == 0){
            if (StringUtils.isNotEmpty(wmsInnerInventory) && wmsInnerInventory.getPackingQty().compareTo(quantity) != -1){
                WmsInnerInventory innerInventory = new WmsInnerInventory();
                BeanUtils.copyProperties(innerInventory, wmsInnerInventory);
                innerInventory.setPackingQty(quantity);
                innerInventory.setInventoryTotalQty(quantity.multiply(wmsInnerInventory.getPackageSpecificationQuantity()));
                innerInventory.setParentInventoryId(id);
                innerInventory.setInventoryId(null);
                innerInventory.setLockStatus((byte) 1);
                this.save(innerInventory);

                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(quantity));
                wmsInnerInventory.setInventoryTotalQty(wmsInnerInventory.getInventoryTotalQty().subtract(innerInventory.getInventoryTotalQty()));
                this.update(wmsInnerInventory);
            }else {
                throw new BizErrorException("库存数量不足");
            }
        }else {
            throw new BizErrorException("当前库存已被锁定");
        }
        return 1;
    }

    @Override
    public int unlock(Long id, BigDecimal quantity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(WmsInnerInventory.class);
        example.createCriteria().andEqualTo("inventoryId",id);
        WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(wmsInnerInventory) && wmsInnerInventory.getLockStatus() == 1) {
            if (quantity.compareTo(wmsInnerInventory.getPackingQty()) == 1){
                throw new BizErrorException("数量超出锁定的数量");
            } else if (quantity.compareTo(wmsInnerInventory.getPackingQty()) == 0){
                wmsInnerInventory.setLockStatus((byte) 0);
            } else {
                WmsInnerInventory innerInventory = new WmsInnerInventory();
                BeanUtils.copyProperties(innerInventory, wmsInnerInventory);
                innerInventory.setPackingQty(quantity);
                innerInventory.setInventoryTotalQty(quantity.multiply(wmsInnerInventory.getPackageSpecificationQuantity()));
                innerInventory.setParentInventoryId(id);
                innerInventory.setInventoryId(null);
                innerInventory.setLockStatus((byte) 0);
                this.save(innerInventory);

                wmsInnerInventory.setPackingQty(wmsInnerInventory.getPackingQty().subtract(quantity));
                wmsInnerInventory.setInventoryTotalQty(wmsInnerInventory.getInventoryTotalQty().subtract(innerInventory.getInventoryTotalQty()));
            }
            this.update(wmsInnerInventory);
        } else {
            throw new BizErrorException("当前库存未进行锁定");
        }
        return 1;
    }

    @Override
    public int save(WmsInnerInventory record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = wmsInnerInventoryMapper.insertUseGeneratedKeys(record);

        WmsHtInnerInventory wmsHtInnerInventory = new WmsHtInnerInventory();
        BeanUtils.copyProperties(record,wmsHtInnerInventory);
        wmsHtInnerInventoryMapper.insert(wmsHtInnerInventory);

        return i;
    }

    @Override
    public int update(WmsInnerInventory entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        WmsHtInnerInventory wmsHtInnerInventory = new WmsHtInnerInventory();
        BeanUtils.copyProperties(entity,wmsHtInnerInventory);
        wmsHtInnerInventoryMapper.insert(wmsHtInnerInventory);

        return wmsInnerInventoryMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<WmsHtInnerInventory> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            WmsInnerInventory wmsInnerInventory = wmsInnerInventoryMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInnerInventory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsHtInnerInventory wmsHtInnerInventory = new WmsHtInnerInventory();
            BeanUtils.copyProperties(wmsInnerInventory,wmsHtInnerInventory);
            list.add(wmsHtInnerInventory);
        }

        wmsHtInnerInventoryMapper.insertList(list);
        return wmsInnerInventoryMapper.deleteByIds(ids);
    }
}
