package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.MesOrderMaterialDTO;
import com.fantechs.common.base.dto.apply.SaveOrderMaterialDTO;
import com.fantechs.common.base.dto.apply.SmtOrderDto;
import com.fantechs.common.base.entity.apply.MesOrderMaterial;
import com.fantechs.common.base.entity.apply.SmtOrder;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtOrderMapper;
import com.fantechs.provider.imes.apply.service.SmtOrderService;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/10/13.
 */
@Service
public class SmtOrderServiceImpl extends BaseService<SmtOrder> implements SmtOrderService {

    @Resource
    private SmtOrderMapper smtOrderMapper;

    @Override
    public int save(SmtOrder smtOrder) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        /*Example example = new Example(SmtOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderCode",smtOrder.getOrderCode());
        SmtOrder order = smtOrderMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(order)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }*/
        smtOrder.setContractCode(CodeUtils.getId("ORDER"));
        smtOrder.setCreateTime(new Date());
        smtOrder.setCreateUserId(currentUserInfo.getUserId());
        smtOrder.setModifiedTime(new Date());
        smtOrder.setModifiedUserId(currentUserInfo.getUserId());
        return smtOrderMapper.insertSelective(smtOrder);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SmtOrder smtOrder = smtOrderMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return smtOrderMapper.deleteByIds(ids);
    }

    @Override
    public int update(SmtOrder smtOrder) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        /*Example example = new Example(SmtOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderCode",smtOrder.getOrderCode());
        SmtOrder order = smtOrderMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(order) && !order.getOrderId().equals(smtOrder.getOrderId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }*/

        smtOrder.setModifiedUserId(currentUserInfo.getUserId());
        smtOrder.setModifiedTime(new Date());
        return smtOrderMapper.updateByPrimaryKey(smtOrder);
    }

    @Override
    public List<SmtOrderDto> findList(Map<String,Object> parm) {
        return smtOrderMapper.findList(parm);
    }

    @Override
    public int saveOrderMaterial(SaveOrderMaterialDTO saveOrderMaterialDTO) {
        SmtOrder smtOrder = saveOrderMaterialDTO.getSmtOrder();
        if(StringUtils.isEmpty(smtOrder.getOrderId())){
            if(this.save(smtOrder)<=0){
                return 0;
            }
        }else{
            if(this.update(smtOrder)<=0){
                return 0;
            }
        }

        //删除原有的关联产品信息
        if(smtOrderMapper.deleteMaterialByOrderId(smtOrder.getOrderId())<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }

        List<MesOrderMaterial> mesOrderMaterialList = saveOrderMaterialDTO.getMesOrderMaterialList();
        if(StringUtils.isNotEmpty(mesOrderMaterialList)){
            for (MesOrderMaterial mesOrderMaterial : mesOrderMaterialList) {
                mesOrderMaterial.setOrderId(smtOrder.getOrderId());
            }
            if(smtOrderMapper.batchAddOrderMaterial(mesOrderMaterialList)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }

        return 1;
    }

    @Override
    public List<MesOrderMaterialDTO> findOrderMaterial(Long orderId) {
        return smtOrderMapper.findOrderMaterial(orderId);
    }
}
