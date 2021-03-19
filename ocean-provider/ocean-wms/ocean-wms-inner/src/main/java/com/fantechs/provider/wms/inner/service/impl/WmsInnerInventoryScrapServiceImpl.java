package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDto;
import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventory;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventoryDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.SmtStorageInventory;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.entity.storage.search.SearchSmtStoragePallet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryScrapDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryScrapDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrap;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrapDet;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtInventoryScrap;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtInventoryScrapMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryScrapDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryScrapMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryScrapService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2021/03/10.
 */
@Service
public class WmsInnerInventoryScrapServiceImpl extends BaseService<WmsInnerInventoryScrap> implements WmsInnerInventoryScrapService {

    @Resource
    private WmsInnerInventoryScrapMapper wmsInnerInventoryScrapMapper;
    @Resource
    private WmsInnerHtInventoryScrapMapper wmsInnerHtInventoryScrapMapper;
    @Resource
    private WmsInnerInventoryScrapDetMapper wmsInnerInventoryScrapDetMapper;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;

    @Override
    public List<WmsInnerInventoryScrapDto> findList(Map<String, Object> map) {
        return wmsInnerInventoryScrapMapper.findList(map);
    }

    @Override
    public List<WmsInnerHtInventoryScrap> findHtList(Map<String, Object> map) {
        return wmsInnerHtInventoryScrapMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int PDASubmit(WmsInnerInventoryScrap wmsInnerInventoryScrap) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isEmpty(wmsInnerInventoryScrap.getWmsInnerInventoryScrapDets())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        for (WmsInnerInventoryScrapDet wmsInnerInventoryScrapDet : wmsInnerInventoryScrap.getWmsInnerInventoryScrapDets()) {

            //判断栈板是否在该储位上
            SearchSmtStoragePallet searchSmtStoragePallet = new SearchSmtStoragePallet();
            searchSmtStoragePallet.setPalletCode(wmsInnerInventoryScrapDet.getBarCode());
            searchSmtStoragePallet.setStorageId(wmsInnerInventoryScrapDet.getStorageId());
            searchSmtStoragePallet.setIsBinding((byte) 1);
            searchSmtStoragePallet.setIsDelete((byte) 1);
            //判断栈板是否绑定区域
            List<SmtStoragePalletDto> smtStoragePallets = storageInventoryFeignApi.findList(searchSmtStoragePallet).getData();
            if (smtStoragePallets.size() <= 0) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100);
            }

            //判断库存数量
            SearchSmtStorageInventory searchSmtStorageInventor = new SearchSmtStorageInventory();
            searchSmtStorageInventor.setStorageId(wmsInnerInventoryScrapDet.getStorageId());
            searchSmtStorageInventor.setMaterialId(wmsInnerInventoryScrapDet.getMaterialId());
            List<SmtStorageInventoryDto> smtStorageInventoryDtos = storageInventoryFeignApi.findList(searchSmtStorageInventor).getData();
            if(StringUtils.isEmpty(smtStorageInventoryDtos)){
                throw new BizErrorException(ErrorCodeEnum.STO30012001);
            }

            SearchSmtStorageInventoryDet searchSmtStorageInventoryDet = new SearchSmtStorageInventoryDet();
            searchSmtStorageInventoryDet.setStorageInventoryId(smtStorageInventoryDtos.get(0).getStorageInventoryId());
            searchSmtStorageInventoryDet.setMaterialBarcodeCode(wmsInnerInventoryScrapDet.getBarCode());
            List<SmtStorageInventoryDetDto> smtStorageInventoryDetDtos = storageInventoryFeignApi.findStorageInventoryDetList(searchSmtStorageInventoryDet).getData();
            if(StringUtils.isEmpty(smtStorageInventoryDetDtos)){
                throw new BizErrorException(ErrorCodeEnum.STO30012001);
            }

            //库存数量小于报废数量
            if(smtStorageInventoryDetDtos.get(0).getMaterialQuantity().compareTo(wmsInnerInventoryScrapDet.getRealityTotalQty()) < 0){
                throw new BizErrorException(ErrorCodeEnum.STO30012000);
            }

            //修改库存
            SmtStorageInventory smtStorageInventory = smtStorageInventoryDtos.get(0);
            smtStorageInventory.setQuantity(wmsInnerInventoryScrapDet.getRealityTotalQty());

            List<SmtStorageInventoryDet> smtStorageInventoryDets = new ArrayList<>();
            SmtStorageInventoryDet smtStorageInventoryDet = new SmtStorageInventoryDet();
            smtStorageInventoryDet.setMaterialQuantity(wmsInnerInventoryScrapDet.getRealityTotalQty());
            smtStorageInventoryDet.setStorageInventoryId(smtStorageInventory.getStorageInventoryId());
            smtStorageInventoryDet.setMaterialBarcodeCode(wmsInnerInventoryScrapDet.getBarCode());
            smtStorageInventoryDets.add(smtStorageInventoryDet);

            smtStorageInventory.setSmtStorageInventoryDets(smtStorageInventoryDets);
            storageInventoryFeignApi.out(smtStorageInventory);

            wmsInnerInventoryScrapDet.setBarCodeStatus((byte) 1);
            wmsInnerInventoryScrapDet.setInventoryScrapStatus((byte) 2);
            wmsInnerInventoryScrapDet.setModifiedUserId(user.getUserId());
            wmsInnerInventoryScrapDet.setModifiedTime(new Date());
            wmsInnerInventoryScrapDetMapper.updateByPrimaryKeySelective(wmsInnerInventoryScrapDet);
        }

        //改变主表状态
        Map<String, Object> map = new HashMap();
        map.put("inventoryScrapId", wmsInnerInventoryScrap.getInventoryScrapId());
        List<WmsInnerInventoryScrapDetDto> wmsInnerInventoryScrapDetDtos = wmsInnerInventoryScrapDetMapper.findList(map);
        if (StringUtils.isNotEmpty(wmsInnerInventoryScrapDetDtos) && wmsInnerInventoryScrapDetDtos.size() > 0) {
            Boolean flag = true;
            for (WmsInnerInventoryScrapDetDto wmsInnerInventoryScrapDetDto : wmsInnerInventoryScrapDetDtos) {
                if (wmsInnerInventoryScrapDetDto.getInventoryScrapStatus() != 2) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                //子表数据都报废完成
                wmsInnerInventoryScrap.setInventoryScrapStatus((byte) 2);
            } else {
                wmsInnerInventoryScrap.setInventoryScrapStatus((byte) 1);
            }
        }

        wmsInnerInventoryScrap.setModifiedUserId(user.getUserId());
        wmsInnerInventoryScrap.setProcessorUserId(user.getUserId());
        wmsInnerInventoryScrap.setModifiedTime(new Date());

        //履历
        WmsInnerHtInventoryScrap wmsInnerHtInventoryScrap = new WmsInnerHtInventoryScrap();
        BeanUtils.copyProperties(wmsInnerInventoryScrap, wmsInnerHtInventoryScrap);
        wmsInnerHtInventoryScrapMapper.insertSelective(wmsInnerHtInventoryScrap);

        return wmsInnerInventoryScrapMapper.updateByPrimaryKeySelective(wmsInnerInventoryScrap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsInnerInventoryScrap wmsInnerInventoryScrap) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isEmpty(wmsInnerInventoryScrap.getWmsInnerInventoryScrapDets())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        int i = 0;

        wmsInnerInventoryScrap.setInventoryScrapCode(CodeUtils.getId("IS"));
        wmsInnerInventoryScrap.setCreateTime(new Date());
        wmsInnerInventoryScrap.setCreateUserId(user.getUserId());
        wmsInnerInventoryScrap.setModifiedTime(new Date());
        wmsInnerInventoryScrap.setModifiedUserId(user.getUserId());
        wmsInnerInventoryScrap.setIsDelete((byte) 1);
        wmsInnerInventoryScrap.setInventoryScrapStatus(StringUtils.isEmpty(wmsInnerInventoryScrap.getInventoryScrapStatus()) ? 0 : wmsInnerInventoryScrap.getInventoryScrapStatus());
        wmsInnerInventoryScrap.setOrganizationId(user.getOrganizationId());
        i = wmsInnerInventoryScrapMapper.insertUseGeneratedKeys(wmsInnerInventoryScrap);

        //履历
        WmsInnerHtInventoryScrap wmsInnerHtInventoryScrap = new WmsInnerHtInventoryScrap();
        BeanUtils.copyProperties(wmsInnerInventoryScrap, wmsInnerHtInventoryScrap);
        wmsInnerHtInventoryScrapMapper.insertSelective(wmsInnerHtInventoryScrap);

        for (WmsInnerInventoryScrapDet wmsInnerInventoryScrapDet : wmsInnerInventoryScrap.getWmsInnerInventoryScrapDets()) {

            wmsInnerInventoryScrapDet.setInventoryScrapId(wmsInnerInventoryScrap.getInventoryScrapId());
            wmsInnerInventoryScrapDet.setCreateTime(new Date());
            wmsInnerInventoryScrapDet.setCreateUserId(user.getUserId());
            wmsInnerInventoryScrapDet.setModifiedTime(new Date());
            wmsInnerInventoryScrapDet.setModifiedUserId(user.getUserId());
            wmsInnerInventoryScrapDet.setOrganizationId(user.getOrganizationId());
            i = wmsInnerInventoryScrapDetMapper.insertSelective(wmsInnerInventoryScrapDet);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsInnerInventoryScrap wmsInnerInventoryScrap) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int i = 0;

        wmsInnerInventoryScrap.setOrganizationId(user.getOrganizationId());
        wmsInnerInventoryScrap.setModifiedUserId(user.getUserId());
        wmsInnerInventoryScrap.setModifiedTime(new Date());

        i = wmsInnerInventoryScrapMapper.updateByPrimaryKeySelective(wmsInnerInventoryScrap);

        //履历
        WmsInnerHtInventoryScrap wmsInnerHtInventoryScrap = new WmsInnerHtInventoryScrap();
        BeanUtils.copyProperties(wmsInnerInventoryScrap, wmsInnerHtInventoryScrap);
        wmsInnerHtInventoryScrapMapper.insertSelective(wmsInnerHtInventoryScrap);

        //删除明细
        Example example = new Example(WmsInnerInventoryScrapDet.class);
        example.createCriteria().andEqualTo("inventoryScrapId", wmsInnerInventoryScrap.getInventoryScrapId());
        wmsInnerInventoryScrapDetMapper.deleteByExample(example);

        for (WmsInnerInventoryScrapDet wmsInnerInventoryScrapDet : wmsInnerInventoryScrap.getWmsInnerInventoryScrapDets()) {

            wmsInnerInventoryScrapDet.setInventoryScrapId(wmsInnerInventoryScrap.getInventoryScrapId());
            wmsInnerInventoryScrapDet.setCreateTime(new Date());
            wmsInnerInventoryScrapDet.setCreateUserId(user.getUserId());
            wmsInnerInventoryScrapDet.setModifiedTime(new Date());
            wmsInnerInventoryScrapDet.setModifiedUserId(user.getUserId());
            wmsInnerInventoryScrapDet.setOrganizationId(user.getOrganizationId());
            i = wmsInnerInventoryScrapDetMapper.insertSelective(wmsInnerInventoryScrapDet);
        }

        return i;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            WmsInnerInventoryScrap wmsInnerInventoryScrap = wmsInnerInventoryScrapMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInnerInventoryScrap)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsInnerHtInventoryScrap wmsInnerHtInventoryScrap = new WmsInnerHtInventoryScrap();
            BeanUtils.copyProperties(wmsInnerInventoryScrap, wmsInnerHtInventoryScrap);
            wmsInnerHtInventoryScrapMapper.insertSelective(wmsInnerHtInventoryScrap);

            //删除明细
            Example example = new Example(WmsInnerInventoryScrapDet.class);
            example.createCriteria().andEqualTo("inventoryScrapId", wmsInnerInventoryScrap.getInventoryScrapId());
            wmsInnerInventoryScrapDetMapper.deleteByExample(example);
        }

        //删除盘存转报废单
        return wmsInnerInventoryScrapMapper.deleteByIds(ids);
    }
}
