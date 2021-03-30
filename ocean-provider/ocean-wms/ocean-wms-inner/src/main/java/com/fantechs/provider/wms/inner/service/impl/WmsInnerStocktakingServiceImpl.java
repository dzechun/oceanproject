package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorageInventoryDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarningDto;
import com.fantechs.common.base.general.dto.basic.BaseWarningPersonnelDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDto;
import com.fantechs.common.base.general.entity.basic.BaseWarning;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarning;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtStocktaking;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtStocktakingDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktaking;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktakingDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStocktakingDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.fileserver.service.BcmFeignApi;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.api.imes.storage.StorageInventoryFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtStocktakingDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerHtStocktakingMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStocktakingDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStocktakingMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStocktakingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/03/22.
 */
@Service
public class WmsInnerStocktakingServiceImpl extends BaseService<WmsInnerStocktaking> implements WmsInnerStocktakingService {

    @Resource
    private WmsInnerStocktakingMapper wmsInnerStocktakingMapper;
    @Resource
    private WmsInnerStocktakingDetMapper wmsInnerStocktakingDetMapper;
    @Resource
    private WmsInnerHtStocktakingMapper wmsInnerHtStocktakingMapper;
    @Resource
    private WmsInnerHtStocktakingDetMapper wmsInnerHtStocktakingDetMapper;
    @Resource
    private BcmFeignApi bcmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private StorageInventoryFeignApi storageInventoryFeignApi;

    @Override
    public List<WmsInnerStocktakingDto> findList(Map<String, Object> map) {
        List<WmsInnerStocktakingDto> list = wmsInnerStocktakingMapper.findList(map);
        for (WmsInnerStocktakingDto wmsInnerStocktakingDto : list) {
            SearchWmsInnerStocktakingDet searchWmsInnerStocktakingDet = new SearchWmsInnerStocktakingDet();
            searchWmsInnerStocktakingDet.setStocktakingId(wmsInnerStocktakingDto.getStocktakingId());
            List<WmsInnerStocktakingDetDto> list1 = wmsInnerStocktakingDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStocktakingDet));
            if (StringUtils.isNotEmpty(list1)){
                wmsInnerStocktakingDto.setWmsInnerStocktakingDetDtos(list1);
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsInnerStocktaking wmsInnerStocktaking) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<WmsInnerStocktakingDetDto> wmsInnerStocktakingDetDtos = wmsInnerStocktaking.getWmsInnerStocktakingDetDtos();
        if (StringUtils.isEmpty(wmsInnerStocktakingDetDtos)){
            throw new BizErrorException("盘点单明细不能为空");
        }

        int stocktakingStatus = 0;
        for (WmsInnerStocktakingDet wmsInnerStocktakingDet : wmsInnerStocktakingDetDtos) {
            //盘点明细的盘点状态
            if (wmsInnerStocktakingDet.getStatus() == 1){
                stocktakingStatus++;
            }
        }

        if (stocktakingStatus == wmsInnerStocktakingDetDtos.size()){
            wmsInnerStocktaking.setStatus((byte) 2);
        }else if (stocktakingStatus == 0){
            wmsInnerStocktaking.setStatus((byte) 0);
        }else {
            wmsInnerStocktaking.setStatus((byte) 1);
        }
        wmsInnerStocktaking.setStocktakingCode(CodeUtils.getId("PD"));
        wmsInnerStocktaking.setCreateTime(new Date());
        wmsInnerStocktaking.setCreateUserId(user.getUserId());
        wmsInnerStocktaking.setModifiedTime(new Date());
        wmsInnerStocktaking.setModifiedUserId(user.getUserId());
        wmsInnerStocktaking.setIsDelete((byte) 1);
        wmsInnerStocktaking.setOrganizationId(user.getOrganizationId());
        //新增盘点单
        int i = wmsInnerStocktakingMapper.insertUseGeneratedKeys(wmsInnerStocktaking);
        //发送邮件
        //获取预警人员
        SearchBaseWarning searchBaseWarning = new SearchBaseWarning();
        searchBaseWarning.setWarningType((long) 0);
        List<BaseWarningDto> baseWarningDtos = baseFeignApi.findBaseWarningList(searchBaseWarning).getData();
        if (StringUtils.isNotEmpty(baseWarningDtos)){
            for (BaseWarningDto baseWarningDto : baseWarningDtos) {
                List<BaseWarningPersonnelDto> baseWarningPersonnelDtoList = baseWarningDto.getBaseWarningPersonnelDtoList();
                if (StringUtils.isNotEmpty(baseWarningPersonnelDtoList)){
                    for (BaseWarningPersonnelDto baseWarningPersonnelDto : baseWarningPersonnelDtoList) {
                        String email = baseWarningPersonnelDto.getEmail();//获取邮箱
                        String stocktakingCode = wmsInnerStocktaking.getStocktakingCode();//获取盘点单号
                        bcmFeignApi.sendSimpleMail(email,"新盘点任务","盘点单号：" + stocktakingCode
                                + "储位名称：" + wmsInnerStocktakingDetDtos.get(0).getStorageName() + "储位编码: " + wmsInnerStocktakingDetDtos.get(0).getStorageCode());
                    }
                }
            }
        }

        //新增盘点单履历
        WmsInnerHtStocktaking wmsInnerHtStocktaking = new WmsInnerHtStocktaking();
        BeanUtils.copyProperties(wmsInnerStocktaking,wmsInnerHtStocktaking);
        wmsInnerHtStocktaking.setModifiedTime(new Date());
        wmsInnerHtStocktaking.setModifiedUserId(user.getUserId());
        wmsInnerHtStocktakingMapper.insertUseGeneratedKeys(wmsInnerHtStocktaking);

        //新增盘点单明细
        LinkedList<WmsInnerHtStocktakingDet> wmsInnerHtStocktakingDets = new LinkedList<>();
        for (WmsInnerStocktakingDet wmsInnerStocktakingDet : wmsInnerStocktakingDetDtos) {
            wmsInnerStocktakingDet.setStocktakingId(wmsInnerStocktaking.getStocktakingId());
            wmsInnerStocktakingDet.setCreateTime(new Date());
            wmsInnerStocktakingDet.setCreateUserId(user.getUserId());
            wmsInnerStocktakingDet.setModifiedTime(new Date());
            wmsInnerStocktakingDet.setModifiedUserId(user.getUserId());
            //对盘点完成的单据计算其盈亏数量和盈亏率
            if (wmsInnerStocktakingDet.getStatus() == 1 && wmsInnerStocktakingDet.getProfitLossRate() == null){
                wmsInnerStocktakingDet.setProfitLossQuantity(wmsInnerStocktakingDet.getBookInventory().subtract(wmsInnerStocktakingDet.getCountedQuantity()));
                wmsInnerStocktakingDet.setProfitLossRate(wmsInnerStocktakingDet.getProfitLossQuantity().divide(wmsInnerStocktakingDet.getBookInventory()));
            }
            WmsInnerHtStocktakingDet wmsInnerHtStocktakingDet = new WmsInnerHtStocktakingDet();
            BeanUtils.copyProperties(wmsInnerStocktakingDet,wmsInnerHtStocktakingDet);
            wmsInnerHtStocktakingDet.setHtStocktakingId(wmsInnerHtStocktaking.getStocktakingId());
            wmsInnerHtStocktakingDets.add(wmsInnerHtStocktakingDet);
        }

        //新增盘点单明细
        wmsInnerStocktakingDetMapper.insertList(wmsInnerStocktakingDetDtos);
        //新增盘点单明细履历
        wmsInnerHtStocktakingDetMapper.insertList(wmsInnerHtStocktakingDets);

        return i;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsInnerStocktaking wmsInnerStocktaking) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<WmsInnerStocktakingDetDto> wmsInnerStocktakingDetDtos = wmsInnerStocktaking.getWmsInnerStocktakingDetDtos();
        if (StringUtils.isEmpty(wmsInnerStocktakingDetDtos)){
            throw new BizErrorException("盘点单明细不能为空");
        }

        //PDA执行得提交操作则修改单据为盘点完成
        if (wmsInnerStocktaking.getPdaOperation() == 1){
            wmsInnerStocktaking.setStatus((byte) 1);
        }
        wmsInnerStocktaking.setModifiedTime(new Date());
        wmsInnerStocktaking.setModifiedUserId(user.getUserId());
        wmsInnerStocktaking.setIsDelete((byte) 1);
        wmsInnerStocktaking.setOrganizationId(user.getOrganizationId());
        //更新盘点单
        int i = wmsInnerStocktakingMapper.updateByPrimaryKeySelective(wmsInnerStocktaking);

        //新增盘点单履历
        WmsInnerHtStocktaking wmsInnerHtStocktaking = new WmsInnerHtStocktaking();
        BeanUtils.copyProperties(wmsInnerStocktaking,wmsInnerHtStocktaking);
        wmsInnerHtStocktaking.setModifiedTime(new Date());
        wmsInnerHtStocktaking.setModifiedUserId(user.getUserId());
        wmsInnerHtStocktakingMapper.insertUseGeneratedKeys(wmsInnerHtStocktaking);

        //删除原来的明细信息
        Example example1 = new Example(WmsInnerStocktakingDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("stocktakingId",wmsInnerStocktaking.getStocktakingId());
        wmsInnerStocktakingDetMapper.deleteByExample(example1);

        //新增盘点单明细
        LinkedList<WmsInnerHtStocktakingDet> wmsInnerHtStocktakingDets = new LinkedList<>();
        for (WmsInnerStocktakingDet wmsInnerStocktakingDet : wmsInnerStocktakingDetDtos) {
            //对盘点完成的单据计算其盈亏数量和盈亏率
            if (wmsInnerStocktakingDet.getStatus() == 1 && wmsInnerStocktakingDet.getProfitLossRate() == null){
                wmsInnerStocktakingDet.setProfitLossQuantity(wmsInnerStocktakingDet.getBookInventory().subtract(wmsInnerStocktakingDet.getCountedQuantity()));
                wmsInnerStocktakingDet.setProfitLossRate(wmsInnerStocktakingDet.getProfitLossQuantity().divide(wmsInnerStocktakingDet.getBookInventory()));

                //更新储位库存明细数据
                //根据栈板码获取库存信息
                SearchSmtStorageInventoryDet searchSmtStorageInventoryDet = new SearchSmtStorageInventoryDet();
                searchSmtStorageInventoryDet.setMaterialBarcodeCode(wmsInnerStocktakingDet.getPalletCode());
                List<SmtStorageInventoryDetDto> smtStorageInventoryDetDtos = storageInventoryFeignApi.findStorageInventoryDetList(searchSmtStorageInventoryDet).getData();
                if (StringUtils.isEmpty(smtStorageInventoryDetDtos)){
                    throw new BizErrorException("无法获取到栈板码对应的库存信息");
                }
                SmtStorageInventoryDetDto smtStorageInventoryDetDto = smtStorageInventoryDetDtos.get(0);
                if (wmsInnerStocktakingDet.getProfitLossQuantity().compareTo(BigDecimal.valueOf(0)) == 1){
                    smtStorageInventoryDetDto.setMaterialQuantity(smtStorageInventoryDetDto.getMaterialQuantity().add(wmsInnerStocktakingDet.getProfitLossQuantity()));
                }else {
                    smtStorageInventoryDetDto.setMaterialQuantity(smtStorageInventoryDetDto.getMaterialQuantity().subtract(wmsInnerStocktakingDet.getProfitLossQuantity()));
                }
                storageInventoryFeignApi.updateStorageInventoryDet(smtStorageInventoryDetDto);
            }

            //PDA执行得提交操作则修改明细为盘点完成
            if (wmsInnerStocktaking.getPdaOperation() == 1){
                wmsInnerStocktakingDet.setStatus((byte) 1);
            }
            wmsInnerStocktakingDet.setStocktakingId(wmsInnerStocktaking.getStocktakingId());
            wmsInnerStocktakingDet.setCreateUserId(user.getUserId());
            wmsInnerStocktakingDet.setCreateTime(new Date());
            wmsInnerStocktakingDet.setModifiedTime(new Date());
            wmsInnerStocktakingDet.setModifiedUserId(user.getUserId());

            WmsInnerHtStocktakingDet wmsInnerHtStocktakingDet = new WmsInnerHtStocktakingDet();
            BeanUtils.copyProperties(wmsInnerStocktakingDet,wmsInnerHtStocktakingDet);
            wmsInnerHtStocktakingDet.setHtStocktakingId(wmsInnerHtStocktaking.getStocktakingId());
            wmsInnerHtStocktakingDets.add(wmsInnerHtStocktakingDet);
        }

        //新增盘点单明细
        wmsInnerStocktakingDetMapper.insertList(wmsInnerStocktakingDetDtos);
        //新增盘点单明细履历
        wmsInnerHtStocktakingDetMapper.insertList(wmsInnerHtStocktakingDets);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        LinkedList<WmsInnerHtStocktakingDet> wmsInnerHtStocktakingDets = new LinkedList<>();
        for (String id : idsArr) {
            WmsInnerStocktaking wmsInnerStocktaking = wmsInnerStocktakingMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(wmsInnerStocktaking)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            WmsInnerHtStocktaking wmsInnerHtStocktaking = new WmsInnerHtStocktaking();
            BeanUtils.copyProperties(wmsInnerStocktaking,wmsInnerHtStocktaking);
            wmsInnerHtStocktakingMapper.insertUseGeneratedKeys(wmsInnerHtStocktaking);

            //删除盘点单明细
            Example example = new Example(WmsInnerStocktakingDet.class);
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("stocktakingId",wmsInnerStocktaking.getStocktakingId());
            List<WmsInnerStocktakingDet> wmsInnerStocktakingDets = wmsInnerStocktakingDetMapper.selectByExample(example);

            for (WmsInnerStocktakingDet wmsInnerStocktakingDet : wmsInnerStocktakingDets) {
                WmsInnerHtStocktakingDet wmsInnerHtStocktakingDet = new WmsInnerHtStocktakingDet();
                BeanUtils.copyProperties(wmsInnerStocktakingDet,wmsInnerHtStocktakingDet);
                wmsInnerHtStocktakingDet.setHtStocktakingId(wmsInnerHtStocktaking.getHtStocktakingId());
                wmsInnerHtStocktakingDets.add(wmsInnerHtStocktakingDet);
            }

            wmsInnerStocktakingDetMapper.deleteByExample(example);
        }

        if (StringUtils.isNotEmpty(wmsInnerHtStocktakingDets)){
            wmsInnerHtStocktakingDetMapper.insertList(wmsInnerHtStocktakingDets);
        }
        return wmsInnerStocktakingMapper.deleteByIds(ids);

    }
}
