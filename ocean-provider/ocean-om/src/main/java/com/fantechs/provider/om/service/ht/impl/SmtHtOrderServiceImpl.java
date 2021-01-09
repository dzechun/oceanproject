package com.fantechs.provider.om.service.ht.impl;

import com.fantechs.common.base.entity.apply.history.SmtHtOrder;
import com.fantechs.common.base.dto.apply.history.SmtHtOrderDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.om.mapper.SmtHtOrderMapper;
import com.fantechs.provider.om.service.ht.SmtHtOrderService;
import org.springframework.stereotype.Service;
import com.fantechs.common.base.utils.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月8日 16:22
 * @Description: 订单历史信息表接口实现
 * @Version: 1.0
 */
@Service
public class SmtHtOrderServiceImpl extends BaseService<SmtHtOrder>  implements SmtHtOrderService {

     @Resource
     private SmtHtOrderMapper smtHtOrderMapper;

    @Override
    public List<SmtHtOrder> selectAll(Map<String,Object> map) {
        Example example = new Example(SmtHtOrder.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    switch (k){
                        case "Name":
                            criteria.andLike(k,"%"+v+"%");
                            break;
                        default :
                            criteria.andEqualTo(k,v);
                            break;
                    }
                }
            });
        }
        return smtHtOrderMapper.selectByExample(example);
    }

    @Override
    public SmtHtOrder selectByKey(Object id) {
        SmtHtOrder smtHtOrder = smtHtOrderMapper.selectByPrimaryKey(id);
        if(smtHtOrder != null && (smtHtOrder.getIsDelete() != null && smtHtOrder.getIsDelete() == 0)){
        smtHtOrder = null;
        }
        return smtHtOrder;
    }

    @Override
    public int save(SmtHtOrder smtHtOrder) {
        smtHtOrder.setCreateUserId(null);
        smtHtOrder.setIsDelete((byte)1);
        return smtHtOrderMapper.insertSelective(smtHtOrder);
    }

    @Override
    public int deleteByKey(Object id) {
        SmtHtOrder smtHtOrder = new SmtHtOrder();
        smtHtOrder.setHtOrderId((long)id);
        smtHtOrder.setIsDelete((byte)0);
        return update(smtHtOrder);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<SmtHtOrder> smtHtOrders = selectAll(map);
        if (StringUtils.isNotEmpty(smtHtOrders)) {
            for (SmtHtOrder smtHtOrder : smtHtOrders) {
                if(deleteByKey(smtHtOrder.getHtOrderId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(SmtHtOrder smtHtOrder) {
        smtHtOrder.setModifiedUserId(null);
        return smtHtOrderMapper.updateByPrimaryKeySelective(smtHtOrder);
    }

   @Override
   public List<SmtHtOrderDTO> selectFilterAll(Map<String, Object> map) {
       return smtHtOrderMapper.selectFilterAll(map);
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
     * 记录操作历史
     * @param id
     * @param operation
     */
    private void recordHistory(Long id,String operation){
        /*HT ht= new HT();
        ht.setOperation(operation);
        MesSchedule mesSchedule = selectByKey(id);
        if (StringUtils.isEmpty(mesSchedule)){
            return;
        }
        BeanUtils.autoFillEqFields(mesSchedule,ht);
        htService.save(ht);*/
    }
}
