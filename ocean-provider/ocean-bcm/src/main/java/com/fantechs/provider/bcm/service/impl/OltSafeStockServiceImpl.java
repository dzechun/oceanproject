package com.fantechs.provider.bcm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarningDto;
import com.fantechs.common.base.general.dto.basic.BaseWarningPersonnelDto;
import com.fantechs.common.base.general.dto.bcm.OltSafeStockDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarning;
import com.fantechs.common.base.general.entity.bcm.OltSafeStock;
import com.fantechs.common.base.general.entity.bcm.history.OltHtSafeStock;
import com.fantechs.common.base.general.entity.bcm.search.SearchOltSafeStock;
import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtWorkOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
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
import java.util.*;
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
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<OltSafeStockDto> findList(SearchOltSafeStock searchOltSafeStock) {
        return oltSafeStockMapper.findList(searchOltSafeStock);
    }

    @Override
    public List<OltSafeStockDto> findHtList(SearchOltSafeStock searchOltSafeStock) {
        return oltHtSafeStockMapper.findHtList(searchOltSafeStock);
    }

    /**
     * 安全库存预警
     * @return
     */
    @Override
    public int inventeryWarning() {
        List<OltSafeStockDto> list = findList(new SearchOltSafeStock());
        List<OltSafeStockDto> oltList = new ArrayList<>();
        for (OltSafeStockDto oltSafeStockDto : list) {
            BigDecimal qty = oltSafeStockMapper.selectCountByWare(oltSafeStockDto.getWarehouseId(),oltSafeStockDto.getMaterialId());
            if(qty.compareTo(oltSafeStockDto.getMaxQuantity())==1){
                oltSafeStockDto.setIsMax(true);
                oltList.add(oltSafeStockDto);
            }else if(qty.compareTo(oltSafeStockDto.getMinQuantity())==-1){
                oltSafeStockDto.setIsMax(false);
                oltList.add(oltSafeStockDto);

            }
        }
        if(oltList.size()>0){
            StringBuffer sb = new StringBuffer();
            sb.append("安全库存预警：\n");
            for (OltSafeStockDto oltSafeStockDto : oltList) {
                if(oltSafeStockDto.getIsMax()){
                    sb.append("仓库："+oltSafeStockDto.getWarehouseName()+"超出设定安全库存");
                }else{
                    sb.append("仓库："+oltSafeStockDto.getWarehouseName()+"低于设定安全库存");
                }
            }
            SearchBaseWarning searchBaseWarning = new SearchBaseWarning();
            searchBaseWarning.setWarningType((long)3);
            searchBaseWarning.setNotificationMethod((byte)3);
            List<BaseWarningDto> baseWarningDtos = baseFeignApi.findBaseWarningList(searchBaseWarning).getData();
            for (BaseWarningDto baseWarningDto : baseWarningDtos) {
                for (BaseWarningPersonnelDto baseWarningPersonnelDto : baseWarningDto.getBaseWarningPersonnelDtoList()) {
                    mailService.sendSimpleMail(baseWarningPersonnelDto.getEmail(),"安全库存预警",sb.toString());
                }
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(OltSafeStock record) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(record.getWarehouseId())){
            throw new BizErrorException("仓库不能为空");
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
