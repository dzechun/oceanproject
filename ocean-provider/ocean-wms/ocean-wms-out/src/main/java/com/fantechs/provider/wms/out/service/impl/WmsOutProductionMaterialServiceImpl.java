package com.fantechs.provider.wms.out.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventoryDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutProductionMaterialDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterialdDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtProductionMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
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
    private BaseFeignApi baseFeignApi;

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
        ex.createCriteria().andEqualTo("workOrderId", wmsOutProductionMaterial.getWorkOrderId()).andEqualTo("materialId", wmsOutProductionMaterial.getMaterialId()).andEqualTo("processId", wmsOutProductionMaterial.getProcessId());
        List<WmsOutProductionMaterialdDet> wmsOutProductionMaterialdDets = wmsOutProductionMaterialdDetMapper.selectByExample(ex);
        if (wmsOutProductionMaterialdDets.isEmpty()) {
            return 1;
        }
        WmsOutProductionMaterialdDet wmsOutProductionMaterialdDet = wmsOutProductionMaterialdDets.get(0);
/*        //部件数量除于用量
        BigDecimal bigDecimal = wmsOutProductionMaterial.getRealityQty().divide(wmsOutProductionMaterialdDet.getQuantity());
        if (new BigDecimal(bigDecimal.intValue()).compareTo(bigDecimal) != 0) {
            throw new BizErrorException("请输入正确的扫描数量");
        }
        if (wmsOutProductionMaterialdDet.getScanQty().compareTo(wmsOutProductionMaterialdDet.getRealityQty()) > 0) {
            throw new BizErrorException("扫描数量不能大于发料数量");
        }*/
        //累加扫描数量(配件数量)
        wmsOutProductionMaterialdDet.setScanQty(wmsOutProductionMaterialdDet.getScanQty().add(wmsOutProductionMaterial.getRealityQty()));
        wmsOutProductionMaterialdDetMapper.updateByPrimaryKeySelective(wmsOutProductionMaterialdDet);

        //计算最小齐套数 再更新实发数量
        //找出所有部件，取最小扫描数量
        ex.clear();
        ex.createCriteria().andEqualTo("productionMaterialId", wmsOutProductionMaterialdDet.getProductionMaterialId());
        wmsOutProductionMaterialdDets = wmsOutProductionMaterialdDetMapper.selectByExample(ex);

        List<BigDecimal> list = new ArrayList<>();
        for (WmsOutProductionMaterialdDet outProductionMaterialdDet : wmsOutProductionMaterialdDets) {
            //如果部件扫描数量是0，则需要判断当前部件是否有当前工段工艺。有则跳过，没有则赋值当前开工数


            list.add(outProductionMaterialdDet.getScanQty().divide(outProductionMaterialdDet.getQuantity()).setScale(0, BigDecimal.ROUND_DOWN));//扫描数量除于用量 向下取整
        }

//        //按最小数量排序
//        List<WmsOutProductionMaterialdDet> temp = wmsOutProductionMaterialdDets.stream().sorted(Comparator.comparing(WmsOutProductionMaterialdDet::getScanQty)).collect(Collectors.toList());
        Collections.sort(list);
        //这是最小齐套数
        BigDecimal lastQty = list.get(0).subtract(wmsOutProductionMaterialdDets.get(0).getUseQty());
        //最小齐套数大于0
        if (lastQty.compareTo(new BigDecimal(0)) > 0) {

            //更新最小配套数
            for (WmsOutProductionMaterialdDet outProductionMaterialdDet : wmsOutProductionMaterialdDets) {
                outProductionMaterialdDet.setUseQty(outProductionMaterialdDet.getUseQty().add(lastQty));
                wmsOutProductionMaterialdDetMapper.updateByPrimaryKeySelective(outProductionMaterialdDet);
            }

            //其中一张发料单
            wmsOutProductionMaterial = wmsOutProductionMaterialMapper.selectByPrimaryKey(wmsOutProductionMaterialdDets.get(0).getProductionMaterialId());

            //库存数据
            WmsInnerStorageInventory wmsInnerStorageInventory = new WmsInnerStorageInventory();
            wmsInnerStorageInventory.setQuantity(wmsOutProductionMaterial.getRealityQty());
            wmsInnerStorageInventory.setStorageId(wmsOutProductionMaterial.getStorageId());
            wmsInnerStorageInventory.setMaterialId(wmsOutProductionMaterial.getMaterialId());

            WmsInnerStorageInventoryDet smtStorageInventoryDet = new WmsInnerStorageInventoryDet();
            smtStorageInventoryDet.setMaterialQuantity(wmsOutProductionMaterial.getRealityQty());
            smtStorageInventoryDet.setGodownEntry(wmsOutProductionMaterial.getFinishedProductCode());
            List<WmsInnerStorageInventoryDet> smtStorageInventoryDets = new ArrayList<>();
            smtStorageInventoryDets.add(smtStorageInventoryDet);
            wmsInnerStorageInventory.setSmtStorageInventoryDets(smtStorageInventoryDets);
            //扣库存
            baseFeignApi.out(wmsInnerStorageInventory);

            //计算发料数量
            Example example = new Example(WmsOutProductionMaterial.class);
            example.createCriteria().andEqualTo("workOrderId", wmsOutProductionMaterial.getWorkOrderId()).andEqualTo("processId", wmsOutProductionMaterial.getProcessId()).andNotEqualTo("outStatus", (byte) 2);
            example.setOrderByClause("create_time");

            List<WmsOutProductionMaterial> dataResources = wmsOutProductionMaterialMapper.selectByExample(example);

            for (WmsOutProductionMaterial dataResource : dataResources) {
                if (dataResource.getRealityQty().add(lastQty).compareTo(dataResource.getPlanQty()) > 0) {
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
                    if (dataResource.getRealityQty().compareTo(dataResource.getPlanQty()) == 0) {
                        dataResource.setOutStatus((byte) 2);
                    } else {
                        dataResource.setOutStatus((byte) 1);
                    }
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
