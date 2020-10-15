package com.fantechs.provider.imes.apply.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBom;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBom;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtHtWorkOrderBomMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderBomMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderMapper;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderBomService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/14.
 */
@Service
public class SmtWorkOrderBomServiceImpl extends BaseService<SmtWorkOrderBom> implements SmtWorkOrderBomService {

        @Resource
        private SmtWorkOrderBomMapper smtWorkOrderBomMapper;
        @Resource
        private SmtHtWorkOrderBomMapper smtHtWorkOrderBomMapper;
        @Resource
        private SmtWorkOrderMapper smtWorkOrderMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(SmtWorkOrderBom smtWorkOrderBom) {
            int i=0;
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByPrimaryKey(smtWorkOrderBom.getWorkOrderId());
            //工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
            Integer workOrderStatus = smtWorkOrder.getWorkOrderStatus();
            if(workOrderStatus==0||workOrderStatus==2){
                Example example = new Example(SmtWorkOrderBom.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("partMaterialId",smtWorkOrderBom.getPartMaterialId());
                criteria.andEqualTo("workOrderId",smtWorkOrder.getWorkOrderId());
                List<SmtWorkOrderBom> smtWorkOrderBoms = smtWorkOrderBomMapper.selectByExample(example);
                if(StringUtils.isNotEmpty(smtWorkOrderBoms)){
                    throw new BizErrorException("零件料号已存在");
                }

                if(smtWorkOrder.getMaterialId().equals(smtWorkOrderBom.getPartMaterialId())){
                    throw new BizErrorException("零件料号不能选择产品料号");
                }
                BigDecimal singleQuantity = smtWorkOrderBom.getSingleQuantity();
                smtWorkOrderBom.setQuantity(new BigDecimal(smtWorkOrder.getWorkOrderQuantity().toString()).multiply(singleQuantity));
                smtWorkOrderBom.setCreateUserId(currentUser.getUserId());
                smtWorkOrderBom.setCreateTime(new Date());
                smtWorkOrderBomMapper.insertUseGeneratedKeys(smtWorkOrderBom);

                //新增工单BOM历史信息
                SmtHtWorkOrderBom smtHtWorkOrderBom=new SmtHtWorkOrderBom();
                BeanUtils.copyProperties(smtWorkOrderBom,smtHtWorkOrderBom);
                smtHtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
                smtHtWorkOrderBom.setModifiedTime(new Date());
                i = smtHtWorkOrderBomMapper.insertSelective(smtHtWorkOrderBom);
            }else {
                throw new BizErrorException("只有工单状态为待生产或暂停生产状态，才能新增工单BOM");
            }
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(SmtWorkOrderBom smtWorkOrderBom) {
            int i=0;
            SmtWorkOrderBom orderBom = smtWorkOrderBomMapper.selectByPrimaryKey(smtWorkOrderBom.getWorkOrderBomId());
            BigDecimal singleQuantity = orderBom.getSingleQuantity();
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByPrimaryKey(smtWorkOrderBom.getWorkOrderId());
            //工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
            Integer workOrderStatus = smtWorkOrder.getWorkOrderStatus();
            if(workOrderStatus==0||workOrderStatus==2){
                Example example = new Example(SmtWorkOrderBom.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("partMaterialId",smtWorkOrderBom.getPartMaterialId());
                criteria.andEqualTo("workOrderId",smtWorkOrder.getWorkOrderId());
                SmtWorkOrderBom workOrderBom = smtWorkOrderBomMapper.selectOneByExample(example);

                if(StringUtils.isNotEmpty(workOrderBom)&&!workOrderBom.getWorkOrderBomId().equals(smtWorkOrderBom.getWorkOrderBomId())){
                    throw new BizErrorException("零件料号已存在");
                }

                if(smtWorkOrder.getMaterialId().equals(smtWorkOrderBom.getPartMaterialId())){
                    throw new BizErrorException("零件料号不能选择产品料号");
                }

                if(singleQuantity.compareTo(smtWorkOrderBom.getSingleQuantity())!=0){
                    smtWorkOrderBom.setQuantity(new BigDecimal(smtWorkOrder.getWorkOrderQuantity().toString()).multiply(smtWorkOrderBom.getSingleQuantity()));
                }else {
                    smtWorkOrderBom.setQuantity(orderBom.getQuantity());
                }
                smtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
                smtWorkOrderBom.setModifiedTime(new Date());
                i= smtWorkOrderBomMapper.updateByPrimaryKeySelective(smtWorkOrderBom);

                //新增工单BOM历史信息
                SmtHtWorkOrderBom smtHtWorkOrderBom=new SmtHtWorkOrderBom();
                BeanUtils.copyProperties(smtWorkOrderBom,smtHtWorkOrderBom);
                smtHtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
                smtHtWorkOrderBom.setModifiedTime(new Date());
                smtHtWorkOrderBomMapper.insertSelective(smtHtWorkOrderBom);
            }else {
                throw new BizErrorException("只有工单状态为待生产或暂停生产状态，才能修改工单BOM");
            }
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
            List<SmtHtWorkOrderBom> list=new ArrayList<>();

            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            String[] workOrderBomIds = ids.split(",");
            for (String workOrderBomId : workOrderBomIds) {
                SmtWorkOrderBom smtWorkOrderBom = smtWorkOrderBomMapper.selectByPrimaryKey(workOrderBomId);
                SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByPrimaryKey(smtWorkOrderBom.getWorkOrderId());
                //工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
                Integer workOrderStatus = smtWorkOrder.getWorkOrderStatus();
                if(workOrderStatus==0||workOrderStatus==2){
                    if(StringUtils.isEmpty(smtWorkOrderBom)){
                        throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                    }
                    //新增工单BOM历史信息
                    SmtHtWorkOrderBom smtHtWorkOrderBom=new SmtHtWorkOrderBom();
                    BeanUtils.copyProperties(smtWorkOrderBom,smtHtWorkOrderBom);
                    smtHtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
                    smtHtWorkOrderBom.setModifiedTime(new Date());
                    list.add(smtHtWorkOrderBom);
                }else {
                    throw new BizErrorException("只有工单状态为待生产或暂停生产状态，才能删除工单BOM");
                }
            }
            smtHtWorkOrderBomMapper.insertList(list);

            return smtWorkOrderBomMapper.deleteByIds(ids);
        }

        @Override
        public List<SmtWorkOrderBom> findList(SearchSmtWorkOrderBom searchSmtWorkOrderBom) {
            return smtWorkOrderBomMapper.findList(searchSmtWorkOrderBom);
        }
}
