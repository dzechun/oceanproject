package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutProductionMaterialDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterialdDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtProductionMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.wms.out.mapper.WmsOutHtProductionMaterialMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutProductionMaterialMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutProductionMaterialdDetMapper;
import com.fantechs.provider.wms.out.service.WmsOutProductionMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by leifengzhi on 2021/01/18.
 */
@Service
public class WmsOutProductionMaterialServiceImpl extends BaseService<WmsOutProductionMaterial> implements WmsOutProductionMaterialService {

    @Resource
    private WmsOutProductionMaterialMapper wmsOutProductionMaterialMapper;
    @Resource
    private WmsOutProductionMaterialdDetMapper wmsOutProductionMaterialdDetMapper;
    @Resource
    private WmsOutHtProductionMaterialMapper wmsOutHtProductionMaterialMapper;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;

    @Override
    public List<WmsOutProductionMaterialDto> findList(Map<String, Object> map) {
        return wmsOutProductionMaterialMapper.findList(map);
    }

    @Override
    public List<WmsOutProductionMaterial> findHtList(Map<String, Object> map) {
        return wmsOutHtProductionMaterialMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByWorkOrderId(WmsOutProductionMaterial wmsOutProductionMaterial) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isEmpty(wmsOutProductionMaterial.getRealityQty())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        if (StringUtils.isEmpty(wmsOutProductionMaterial.getMaterialId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        if (StringUtils.isEmpty(wmsOutProductionMaterial.getWorkOrderId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        //更新发料明细表开工扫描数量
        Example ex = new Example(WmsOutProductionMaterialdDet.class);
        ex.createCriteria().andEqualTo("workOrderId", wmsOutProductionMaterial.getWorkOrderId()).andEqualTo("materialId", wmsOutProductionMaterial.getMaterialId());
        List<WmsOutProductionMaterialdDet> wmsOutProductionMaterialdDets = wmsOutProductionMaterialdDetMapper.selectByExample(ex);
        if (wmsOutProductionMaterialdDets.isEmpty()) {
            return 1;
        }
        WmsOutProductionMaterialdDet wmsOutProductionMaterialdDet = wmsOutProductionMaterialdDets.get(0);
        wmsOutProductionMaterialdDet.setScanQty(wmsOutProductionMaterialdDet.getScanQty().add(wmsOutProductionMaterial.getRealityQty()));
        if (wmsOutProductionMaterialdDet.getScanQty().compareTo(wmsOutProductionMaterialdDet.getRealityQty()) > 0) {
            throw new BizErrorException("扫描数量不能大于发料数量");
        }
        wmsOutProductionMaterialdDetMapper.updateByPrimaryKeySelective(wmsOutProductionMaterialdDet);

        //计算最小齐套数 再更新实发数量
        //找出所有部件，取最小扫描数量
        ex.clear();
        ex.createCriteria().andEqualTo("productionMaterialId", wmsOutProductionMaterialdDet.getProductionMaterialId());
        wmsOutProductionMaterialdDets = wmsOutProductionMaterialdDetMapper.selectByExample(ex);
        //按最小数量排序
        List<WmsOutProductionMaterialdDet> temp = wmsOutProductionMaterialdDets.stream().sorted(Comparator.comparing(WmsOutProductionMaterialdDet::getScanQty)).collect(Collectors.toList());

        BigDecimal lastQty = temp.get(0).getScanQty().subtract(temp.get(0).getUseQty());
        //最小齐套数大于0
        if (lastQty.compareTo(new BigDecimal(0)) > 0) {

            //更新最小配套数
            for (WmsOutProductionMaterialdDet outProductionMaterialdDet : wmsOutProductionMaterialdDets) {
                outProductionMaterialdDet.setUseQty(outProductionMaterialdDet.getUseQty().add(lastQty));
                wmsOutProductionMaterialdDetMapper.updateByPrimaryKeySelective(outProductionMaterialdDet);
            }

            //其中一张发料单
            wmsOutProductionMaterial = wmsOutProductionMaterialMapper.selectByPrimaryKey(temp.get(0).getProductionMaterialId());

            //库存数据
            SmtStorageInventory smtStorageInventory = new SmtStorageInventory();
            smtStorageInventory.setQuantity(wmsOutProductionMaterial.getRealityQty());
            smtStorageInventory.setStorageId(wmsOutProductionMaterial.getStorageId());
            smtStorageInventory.setMaterialId(wmsOutProductionMaterial.getMaterialId());

            SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
            smtStorageInventoryDet.setMaterialQuantity(wmsOutProductionMaterial.getRealityQty());
            smtStorageInventoryDet.setGodownEntry(wmsOutProductionMaterial.getFinishedProductCode());
            List<SmtStorageInventoryDet> smtStorageInventoryDets = new ArrayList<>();
            smtStorageInventoryDets.add(smtStorageInventoryDet);
            smtStorageInventory.setSmtStorageInventoryDets(smtStorageInventoryDets);
            //扣库存
            storageInventoryFeignApi.out(smtStorageInventory);

            //计算发料数量
            Example example = new Example(WmsOutProductionMaterial.class);
            example.createCriteria().andEqualTo("workOrderId", wmsOutProductionMaterial.getWorkOrderId()).andNotEqualTo("outStatus", (byte) 2);
            example.setOrderByClause("create_time");

            List<WmsOutProductionMaterial> dataResources = wmsOutProductionMaterialMapper.selectByExample(example);

            for (WmsOutProductionMaterial dataResource : dataResources) {
                if (dataResource.getRealityQty().add(lastQty).compareTo(dataResource.getPlanQty()) >= 0) {
                    lastQty = lastQty.subtract(dataResource.getPlanQty().subtract(dataResource.getRealityQty()));
                    dataResource.setRealityQty(dataResource.getPlanQty());
                    dataResource.setOutStatus((byte) 2);
                    //履历
                    WmsOutHtProductionMaterial wmsOutHtProductionMaterial = new WmsOutHtProductionMaterial();
                    BeanUtils.copyProperties(dataResource, wmsOutHtProductionMaterial);
                    wmsOutHtProductionMaterialMapper.insertSelective(wmsOutHtProductionMaterial);
                    wmsOutProductionMaterialMapper.updateByPrimaryKeySelective(dataResource);
                } else {
                    dataResource.setRealityQty(dataResource.getRealityQty().add(lastQty));
                    dataResource.setOutStatus((byte) 1);
                    wmsOutProductionMaterialMapper.updateByPrimaryKeySelective(dataResource);
                    //履历
                    WmsOutHtProductionMaterial wmsOutHtProductionMaterial = new WmsOutHtProductionMaterial();
                    BeanUtils.copyProperties(dataResource, wmsOutHtProductionMaterial);
                    wmsOutHtProductionMaterialMapper.insertSelective(wmsOutHtProductionMaterial);
                    break;
                }
            }

            wmsOutProductionMaterial.setModifiedUserId(user.getCreateUserId());
            wmsOutProductionMaterial.setModifiedTime(new Date());
        }

        return 1;
    }

    @Override
    public int save(WmsOutProductionMaterial wmsOutProductionMaterial) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutProductionMaterial.setProductionMaterialCode(CodeUtils.getId("FL"));
        wmsOutProductionMaterial.setOrganizationId(user.getOrganizationId());
        wmsOutProductionMaterial.setCreateUserId(user.getCreateUserId());
        wmsOutProductionMaterial.setCreateTime(new Date());
        int i = wmsOutProductionMaterialMapper.insertSelective(wmsOutProductionMaterial);

        WmsOutHtProductionMaterial wmsOutHtProductionMaterial = new WmsOutHtProductionMaterial();
        BeanUtils.copyProperties(wmsOutProductionMaterial, wmsOutHtProductionMaterial);
        wmsOutHtProductionMaterialMapper.insertSelective(wmsOutHtProductionMaterial);

        return i;
    }

    @Override
    public int update(WmsOutProductionMaterial wmsOutProductionMaterial) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        wmsOutProductionMaterial.setModifiedUserId(user.getCreateUserId());
        wmsOutProductionMaterial.setModifiedTime(new Date());

        //履历
        WmsOutHtProductionMaterial wmsOutHtProductionMaterial = new WmsOutHtProductionMaterial();
        BeanUtils.copyProperties(wmsOutProductionMaterial, wmsOutHtProductionMaterial);
        wmsOutHtProductionMaterialMapper.insertSelective(wmsOutHtProductionMaterial);

        return wmsOutProductionMaterialMapper.updateByPrimaryKeySelective(wmsOutProductionMaterial);
    }

    @Override
    public int batchDelete(String ids) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            WmsOutProductionMaterial wmsOutProductionMaterial = wmsOutProductionMaterialMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsOutProductionMaterial)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsOutHtProductionMaterial wmsOutHtProductionMaterial = new WmsOutHtProductionMaterial();
            BeanUtils.copyProperties(wmsOutProductionMaterial, wmsOutHtProductionMaterial);
            wmsOutHtProductionMaterialMapper.insertSelective(wmsOutHtProductionMaterial);
        }
        return wmsOutProductionMaterialMapper.deleteByIds(ids);
    }
}
