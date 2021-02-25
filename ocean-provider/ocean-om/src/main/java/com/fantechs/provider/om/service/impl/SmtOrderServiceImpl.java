package com.fantechs.provider.om.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.om.MesOrderMaterialDTO;
import com.fantechs.common.base.general.dto.om.SaveOrderMaterialDTO;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesOrderMaterialListDTO;
import com.fantechs.common.base.general.dto.om.SmtOrderDto;
import com.fantechs.common.base.general.entity.om.MesOrderMaterial;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.general.entity.mes.pm.history.MesHtOrderMaterial;
import com.fantechs.common.base.general.entity.om.SmtHtOrder;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.om.mapper.SmtOrderMapper;
import com.fantechs.provider.om.service.SmtOrderService;
import com.fantechs.provider.om.service.ht.MesHtOrderMaterialService;
import com.fantechs.provider.om.service.ht.SmtHtOrderService;
import org.springframework.stereotype.Service;

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
    @Resource
    private SmtHtOrderService smtHtOrderService;
    @Resource
    private MesHtOrderMaterialService mesHtOrderMaterialService;

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
        smtOrder.setOrderCode(CodeUtils.getId("ORDER"));
        smtOrder.setCreateTime(new Date());
        smtOrder.setCreateUserId(currentUserInfo.getUserId());
        smtOrder.setModifiedTime(new Date());
        smtOrder.setModifiedUserId(currentUserInfo.getUserId());
        if(smtOrderMapper.insertSelective(smtOrder)<=0){
            return 0;
        }
        recordHistory(smtOrder,"新增");
        return 1;
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
            recordHistory(smtOrder,"删除");
        }
        if(smtOrderMapper.deleteByIds(ids)<=0){
            return 0;
        }
        return 1;
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
        if(smtOrderMapper.updateByPrimaryKey(smtOrder)<=0){
            return 0;
        }
        recordHistory(smtOrder,"更新");
        return 1;
    }

    @Override
    public List<SmtOrderDto> findList(Map<String,Object> parm) {
        return smtOrderMapper.findList(parm);
    }

    @Override
    public int orderMaterialSchedule(Long orderMaterialId) {
        return smtOrderMapper.orderMaterialSchedule(orderMaterialId);
    }

    @Override
    public int saveOrderMaterial(SaveOrderMaterialDTO saveOrderMaterialDTO) {
        SmtOrder smtOrder = saveOrderMaterialDTO.getSmtOrder();
        List<MesOrderMaterial> mesOrderMaterialList = saveOrderMaterialDTO.getMesOrderMaterialList();
        //=====遍历累加物料的总数量
        int total=0;//订单物料总共数量
        if(StringUtils.isNotEmpty(mesOrderMaterialList)){
            for (MesOrderMaterial mesOrderMaterial : mesOrderMaterialList) {
                total+=mesOrderMaterial.getTotal().intValue();
            }
            smtOrder.setOrderQuantity(total);
        }
        //=====
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
        smtOrderMapper.deleteMaterialByOrderId(smtOrder.getOrderId());

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
    public List<MesOrderMaterialDTO> findOrderMaterial(SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO) {
        return smtOrderMapper.findOrderMaterial(searchMesOrderMaterialListDTO);
    }

    /**
     * 记录操作历史
     * @param smtOrder
     * @param operation
     */
    private void recordHistory(SmtOrder smtOrder,String operation){
        SmtHtOrder smtHtOrder = new SmtHtOrder();
        smtHtOrder.setOption1(operation);
        if (StringUtils.isEmpty(smtOrder)){
            return;
        }
        BeanUtils.autoFillEqFields(smtOrder,smtHtOrder);
        smtHtOrderService.save(smtHtOrder);
    }

    /**
     * 记录订单物料操作历史
     * @param id
     * @param operation
     */
    private void recordOrderMaterialHistory(Long id,String operation){
        MesHtOrderMaterial mesHtOrderMaterial = new MesHtOrderMaterial();
        mesHtOrderMaterial.setOperation(operation);
        SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO = new SearchMesOrderMaterialListDTO();
        List<MesOrderMaterialDTO> orderMaterialDTOList = smtOrderMapper.findOrderMaterial(searchMesOrderMaterialListDTO);
        if (StringUtils.isEmpty(orderMaterialDTOList)){
            return;
        }
        BeanUtils.autoFillEqFields(orderMaterialDTOList.get(0),mesHtOrderMaterial);
        mesHtOrderMaterialService.save(mesHtOrderMaterial);
    }
}
