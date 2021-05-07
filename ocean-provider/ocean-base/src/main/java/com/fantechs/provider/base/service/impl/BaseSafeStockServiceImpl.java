package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSafeStockDto;
import com.fantechs.common.base.general.entity.basic.BaseSafeStock;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSafeStock;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSafeStock;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.base.mapper.BaseHtSafeStockMapper;
import com.fantechs.provider.base.mapper.BaseSafeStockMapper;
import com.fantechs.provider.base.service.BaseSafeStockService;
import com.fantechs.provider.base.service.mail.MailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by mr.lei on 2021/03/04.
 */
@Service
public class BaseSafeStockServiceImpl extends BaseService<BaseSafeStock> implements BaseSafeStockService {

    @Resource
    private BaseSafeStockMapper baseSafeStockMapper;
    @Resource
    private BaseHtSafeStockMapper baseHtSafeStockMapper;
    @Resource
    private MailService mailService;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<BaseSafeStockDto> findList(SearchBaseSafeStock searchBaseSafeStock) {
        return baseSafeStockMapper.findList(searchBaseSafeStock);
    }

    @Override
    public List<BaseSafeStockDto> findHtList(SearchBaseSafeStock searchBaseSafeStock) {
        return baseHtSafeStockMapper.findHtList(searchBaseSafeStock);
    }

    /**
     * 安全库存预警
     * @return
     */
    @Override
    public int inventeryWarning() {
        List<BaseSafeStockDto> list = findList(new SearchBaseSafeStock());
        List<BaseSafeStockDto> oltList = new ArrayList<>();
        for (BaseSafeStockDto oltSafeStockDto : list) {
            BigDecimal qty = baseSafeStockMapper.selectCountByWare(oltSafeStockDto.getWarehouseId(),oltSafeStockDto.getMaterialId());
            if(qty.compareTo(oltSafeStockDto.getMaxQty())==1){
                oltSafeStockDto.setIsMax(true);
                oltList.add(oltSafeStockDto);
            }else if(qty.compareTo(oltSafeStockDto.getMinQty())==-1){
                oltSafeStockDto.setIsMax(false);
                oltList.add(oltSafeStockDto);

            }
        }
        if(oltList.size()>0){
            StringBuffer sb = new StringBuffer();
            sb.append("安全库存预警：\n");
            for (BaseSafeStockDto oltSafeStockDto : oltList) {
                if(oltSafeStockDto.getIsMax()){
                    sb.append("仓库："+oltSafeStockDto.getWarehouseName()+"超出设定安全库存");
                }else{
                    sb.append("仓库："+oltSafeStockDto.getWarehouseName()+"低于设定安全库存");
                }
            }
//            SearchBaseWarning searchBaseWarning = new SearchBaseWarning();
//            searchBaseWarning
//            List<BaseWarningDto> baseWarningDtos = baseFeignApi.findBaseWarningList(searchBaseWarning).getData();
            mailService.sendSimpleMail("lql@fantechs.com.cn","安全库存预警",sb.toString());
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseSafeStock record) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(record.getWarehouseId())){
            throw new BizErrorException("仓库不能为空");
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        int num = baseSafeStockMapper.insertUseGeneratedKeys(record);
        recordHistory(record);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseSafeStock entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        int num = baseSafeStockMapper.updateByPrimaryKeySelective(entity);
        recordHistory(entity);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BaseSafeStock baseSafeStock = baseSafeStockMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseSafeStock)){
                throw new BizErrorException("删除失败");
            }
            recordHistory(baseSafeStock);
        }
        return baseSafeStockMapper.deleteByIds(ids);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

    /**
     * 历史记录
     * @param baseSafeStock
     */
    private void recordHistory(BaseSafeStock baseSafeStock){
        BaseHtSafeStock baseHtSafeStock = new BaseHtSafeStock();
        if (StringUtils.isEmpty(baseHtSafeStock)){
            return;
        }
        BeanUtils.copyProperties(baseSafeStock, baseHtSafeStock);
        baseHtSafeStockMapper.insertSelective(baseHtSafeStock);
    }
}
