package com.fantechs.provider.bcm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.OltSafeStockDto;
import com.fantechs.common.base.general.entity.bcm.OltSafeStock;
import com.fantechs.common.base.general.entity.bcm.history.OltHtSafeStock;
import com.fantechs.common.base.general.entity.bcm.search.SearchOltSafeStock;
import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtWorkOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.bcm.mapper.OltHtSafeStockMapper;
import com.fantechs.provider.bcm.mapper.OltSafeStockMapper;
import com.fantechs.provider.bcm.service.Mail.MailService;
import com.fantechs.provider.bcm.service.Mail.impl.MailServiceImpl;
import com.fantechs.provider.bcm.service.OltSafeStockService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by mr.lei on 2021/03/04.
 */
@Service
public class OltSafeStockServiceImpl extends BaseService<OltSafeStock> implements OltSafeStockService {

    @Resource
    private OltSafeStockMapper oltSafeStockMapper;
    @Resource
    private OltHtSafeStockMapper oltHtSafeStockMapper;
    @Resource
    private MailService mailService;

    @Override
    public List<OltSafeStockDto> findList(SearchOltSafeStock searchOltSafeStock) {
        List<OltSafeStockDto> list = oltSafeStockMapper.findList(searchOltSafeStock);
        for (OltSafeStockDto oltSafeStockDto : list) {
            //按仓库查询总数
            if(oltSafeStockDto.getSafeStockType()==1){
                BigDecimal countQty = oltSafeStockMapper.selectCountByWare(oltSafeStockDto.getWarehouseId());
                oltSafeStockDto.setCountQty(countQty);
            }else if(oltSafeStockDto.getSafeStockType()==2){
                //按物料类别查询总数
                BigDecimal countQty = oltSafeStockMapper.selectCountByCate(oltSafeStockDto.getMaterialCategoryId());
                oltSafeStockDto.setCountQty(countQty);
            }else if(oltSafeStockDto.getSafeStockType()==3){
                //按物料查询总数
                BigDecimal countQty = oltSafeStockMapper.selectCountByCode(oltSafeStockDto.getMaterialId());
                oltSafeStockDto.setCountQty(countQty);
            }
        }
        return list;
    }

    @Override
    public List<OltSafeStockDto> findHtList(SearchOltSafeStock searchOltSafeStock) {
        List<OltSafeStockDto> list = oltHtSafeStockMapper.findHtList(searchOltSafeStock);
        for (OltSafeStockDto oltSafeStockDto : list) {
            //按仓库查询总数
            if(oltSafeStockDto.getSafeStockType()==1){
                BigDecimal countQty = oltSafeStockMapper.selectCountByWare(oltSafeStockDto.getWarehouseId());
                oltSafeStockDto.setCountQty(countQty);
            }else if(oltSafeStockDto.getSafeStockType()==2){
                //按物料类别查询总数
                BigDecimal countQty = oltSafeStockMapper.selectCountByCate(oltSafeStockDto.getMaterialCategoryId());
                oltSafeStockDto.setCountQty(countQty);
            }else if(oltSafeStockDto.getSafeStockType()==3){
                //按物料查询总数
                BigDecimal countQty = oltSafeStockMapper.selectCountByCode(oltSafeStockDto.getMaterialId());
                oltSafeStockDto.setCountQty(countQty);
            }
        }
        return list;
    }

    /**
     * 安全库存预警
     * @return
     */
    @Override
    public int inventeryWarning() {
        List<OltSafeStockDto> list = findList(new SearchOltSafeStock());
        list.stream().filter(li->li.getCountQty().compareTo(li.getSafeStockQuantity())==1).collect(Collectors.toList());
        if(list.size()>0){
            StringBuffer sb = new StringBuffer();
            sb.append("安全库存预警：\n");
            for (OltSafeStockDto oltSafeStockDto : list) {
                if(oltSafeStockDto.getSafeStockType()==1){
                    sb.append("仓库："+oltSafeStockDto.getWarehouseName()+"   现有数量："+oltSafeStockDto.getCountQty());
                }else if(oltSafeStockDto.getSafeStockType()==2){
                    sb.append("物料类型："+oltSafeStockDto.getWarehouseName()+"   现有数量："+oltSafeStockDto.getCountQty());
                }else if(oltSafeStockDto.getSafeStockType()==3){
                    sb.append("物料："+oltSafeStockDto.getMaterialName()+"\n");
                }
            }
            sb.append("总数低于安全库存数量！！");
            mailService.sendSimpleMail("lql@fantechs.com.cn","安全库存预警",sb.toString());
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OltSafeStock record) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(record.getSafeStockType())){
            throw new BizErrorException("类型不能为空");
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        int num = oltSafeStockMapper.insertUseGeneratedKeys(record);
        recordHistory(record);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(OltSafeStock entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        int num = oltSafeStockMapper.updateByPrimaryKeySelective(entity);
        recordHistory(entity);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            OltSafeStock oltSafeStock = oltSafeStockMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(oltSafeStock)){
                throw new BizErrorException("删除失败");
            }
            recordHistory(oltSafeStock);
        }
        return oltSafeStockMapper.deleteByIds(ids);
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
     * @param oltSafeStock
     */
    private void recordHistory(OltSafeStock oltSafeStock){
        OltHtSafeStock oltHtSafeStock = new OltHtSafeStock();
        if (StringUtils.isEmpty(oltHtSafeStock)){
            return;
        }
        BeanUtils.copyProperties(oltSafeStock, oltHtSafeStock);
        oltHtSafeStockMapper.insertSelective(oltHtSafeStock);
    }
}
