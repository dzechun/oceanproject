package com.fantechs.provider.imes.apply.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SmtWorkOrderBarcodeCollocationDto;
import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderBarcodeCollocationMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderMapper;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderBarcodeCollocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2020/11/21.
 */
@Service
public class SmtWorkOrderBarcodeCollocationServiceImpl  extends BaseService<SmtWorkOrderBarcodeCollocation> implements SmtWorkOrderBarcodeCollocationService {

    @Resource
    private SmtWorkOrderBarcodeCollocationMapper smtWorkOrderBarcodeCollocationMapper;
    @Resource
    private SmtWorkOrderMapper smtWorkOrderMapper;

    @Override
    public List<SmtWorkOrderBarcodeCollocationDto> findList(SearchSmtWorkOrderBarcodeCollocation searchSmtWorkOrderBarcodeCollocation) {
        return smtWorkOrderBarcodeCollocationMapper.findList(searchSmtWorkOrderBarcodeCollocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtWorkOrderBarcodeCollocation record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Long workOrderId = record.getWorkOrderId();
        SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderMapper.selectByWorkOrderId(workOrderId);
        Integer workOrderQuantity = smtWorkOrderDto.getWorkOrderQuantity();
        Integer transferQuantity = smtWorkOrderDto.getTransferQuantity();
        //工单的转移批次
        int sumBatchQuantity = (int) Math.ceil((double)workOrderQuantity / transferQuantity);
        //产生数量
        Integer produceQuantity = record.getProduceQuantity();
        //已产生数量
        Integer generatedQuantity = record.getGeneratedQuantity()==null?0:record.getGeneratedQuantity();
        if(produceQuantity+generatedQuantity>sumBatchQuantity){
            throw new BizErrorException("工单产生条码总数量不能大于工单数量");
        }else if(produceQuantity+generatedQuantity==sumBatchQuantity){
            record.setStatus((byte) 2);
        }else {
            record.setStatus((byte) 1);
        }

        record.setCreateUserId(currentUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(currentUser.getUserId());
        record.setModifiedTime(new Date());

        return smtWorkOrderBarcodeCollocationMapper.insertSelective(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtWorkOrderBarcodeCollocation entity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        entity.setModifiedUserId(currentUser.getUserId());
        entity.setModifiedTime(new Date());
        return smtWorkOrderBarcodeCollocationMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] items = ids.split(",");
        for (String item : items) {
            SmtWorkOrderBarcodeCollocation smtWorkOrderBarcodeCollocation = smtWorkOrderBarcodeCollocationMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(smtWorkOrderBarcodeCollocation)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return smtWorkOrderBarcodeCollocationMapper.deleteByIds(ids);
    }
}
