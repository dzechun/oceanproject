package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderCardCollocation;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderCardCollocationMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderMapper;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderCardCollocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/20.
 */
@Service
public class SmtWorkOrderCardCollocationServiceImpl extends BaseService<SmtWorkOrderCardCollocation> implements SmtWorkOrderCardCollocationService {

        @Resource
        private SmtWorkOrderCardCollocationMapper smtWorkOrderCardCollocationMapper;
        @Resource
        private SmtWorkOrderMapper smtWorkOrderMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(SmtWorkOrderCardCollocation smtWorkOrderCardCollocation) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            //通过工单中的物料ID查询到物料料号中的转移批量
            Long workOrderId = smtWorkOrderCardCollocation.getWorkOrderId();
            SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderMapper.selectByWorkOrderId(workOrderId);
            Integer workOrderQuantity = smtWorkOrderDto.getWorkOrderQuantity();
            Integer transferQuantity = smtWorkOrderDto.getTransferQuantity();
            //工单的转移批次
            int sumBatchQuantity = (int) Math.ceil((double)workOrderQuantity / transferQuantity);

            //产生数量
            Integer produceQuantity = smtWorkOrderCardCollocation.getProduceQuantity();
            //已产生数量
            Integer generatedQuantity = smtWorkOrderCardCollocation.getGeneratedQuantity()==null?0:smtWorkOrderCardCollocation.getGeneratedQuantity();
            if(produceQuantity+generatedQuantity>sumBatchQuantity){
                   throw new BizErrorException("工单产生条码总数量不能大于工单数量");
            }else if(produceQuantity+generatedQuantity==sumBatchQuantity){
                smtWorkOrderCardCollocation.setStatus((byte) 2);
            }else {
                smtWorkOrderCardCollocation.setStatus((byte) 1);
            }

            smtWorkOrderCardCollocation.setGeneratedQuantity(produceQuantity+generatedQuantity);
            smtWorkOrderCardCollocation.setCreateUserId(currentUser.getUserId());
            smtWorkOrderCardCollocation.setCreateTime(new Date());
            smtWorkOrderCardCollocation.setModifiedUserId(currentUser.getUserId());
            smtWorkOrderCardCollocation.setModifiedTime(new Date());

            return smtWorkOrderCardCollocationMapper.insertSelective(smtWorkOrderCardCollocation);
        }

        @Override
        public List<SmtWorkOrderCardCollocation> findList(SearchSmtWorkOrderCardCollocation searchSmtWorkOrderCardCollocation) {
            return smtWorkOrderCardCollocationMapper.findList(searchSmtWorkOrderCardCollocation);
        }
}
