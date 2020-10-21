package com.fantechs.provider.imes.apply.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBom;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrder;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrderBom;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtHtWorkOrderBomMapper;
import com.fantechs.provider.imes.apply.mapper.SmtHtWorkOrderMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderBomMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderMapper;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by wcz on 2020/10/13.
 */
@Service
public class SmtWorkOrderServiceImpl extends BaseService<SmtWorkOrder> implements SmtWorkOrderService {

        @Resource
        private SmtWorkOrderMapper smtWorkOrderMapper;
        @Resource
        private SmtHtWorkOrderMapper smtHtWorkOrderMapper;
        @Resource
        private SmtWorkOrderBomMapper smtWorkOrderBomMapper;
        @Resource
        private SmtHtWorkOrderBomMapper smtHtWorkOrderBomMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(SmtWorkOrder smtWorkOrder) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            Example example = new Example(SmtWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode",smtWorkOrder.getWorkOrderCode());

            List<SmtWorkOrder> smtWorkOrders = smtWorkOrderMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtWorkOrders)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            smtWorkOrder.setCreateUserId(currentUser.getUserId());
            smtWorkOrder.setCreateTime(new Date());
            smtWorkOrderMapper.insertUseGeneratedKeys(smtWorkOrder);

            //新增工单历史信息
            SmtHtWorkOrder smtHtWorkOrder=new SmtHtWorkOrder();
            BeanUtils.copyProperties(smtWorkOrder,smtHtWorkOrder);
            smtHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            smtHtWorkOrder.setModifiedTime(new Date());
            int i = smtHtWorkOrderMapper.insertSelective(smtHtWorkOrder);

            //根据产品BOM生成工单BOM
            genWorkOrder(smtWorkOrder);
            return i;
        }

        /**
         * 根据产品BOM明细生成工单BOM信息
         * @param smtWorkOrder
         */
        @Transactional(rollbackFor = Exception.class)
        public void genWorkOrder(SmtWorkOrder smtWorkOrder) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            List<SmtWorkOrderBom> list=new ArrayList<>();
            List<SmtHtWorkOrderBom> htList=new ArrayList<>();

            //根据物料ID查询产品BOM信息
            List<SmtProductBomDet> smtProductBomDets=smtWorkOrderMapper.selectProductBomDet(smtWorkOrder.getMaterialId());
            if(StringUtils.isNotEmpty(smtProductBomDets)){
                for (SmtProductBomDet smtProductBomDet : smtProductBomDets) {
                    SmtWorkOrderBom smtWorkOrderBom=new SmtWorkOrderBom();
                    BeanUtils.copyProperties(smtProductBomDet,smtWorkOrderBom,new String[]{"createUserId","createTime","modifiedUserId","modifiedTime"});
                    Integer workOrderQuantity = smtWorkOrder.getWorkOrderQuantity();
                    BigDecimal quantity = smtProductBomDet.getQuantity();
                    smtWorkOrderBom.setWorkOrderId(smtWorkOrder.getWorkOrderId());
                    smtWorkOrderBom.setSingleQuantity(quantity);
                    smtWorkOrderBom.setQuantity(new BigDecimal(workOrderQuantity.toString()).multiply(quantity));
                    smtWorkOrderBom.setCreateUserId(currentUser.getUserId());
                    smtWorkOrderBom.setCreateTime(new Date());
                    list.add(smtWorkOrderBom);

                }
                //批量新增工单BOM信息
                smtWorkOrderBomMapper.insertList(list);

                 if(StringUtils.isNotEmpty(list)){
                     for (SmtWorkOrderBom smtWorkOrderBom : list) {
                         //新增工单BOM历史信息
                         SmtHtWorkOrderBom smtHtWorkOrderBom=new SmtHtWorkOrderBom();
                         BeanUtils.copyProperties(smtWorkOrderBom,smtHtWorkOrderBom);
                         smtHtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
                         smtHtWorkOrderBom.setModifiedTime(new Date());
                         htList.add(smtHtWorkOrderBom);
                     }
                 }
                //批量新增工单BOM历史信息
                smtHtWorkOrderBomMapper.insertList(htList);
            }

        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(SmtWorkOrder smtWorkOrder) {
            int i=0;
            List<SmtWorkOrderBom> list=new ArrayList();
            List<SmtHtWorkOrderBom> htList=new ArrayList<>();

            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            SmtWorkOrder order = smtWorkOrderMapper.selectByPrimaryKey(smtWorkOrder.getWorkOrderId());
            //工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
            Integer workOrderStatus = order.getWorkOrderStatus();
            if(workOrderStatus!=3){
                Example example = new Example(SmtWorkOrder.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("workOrderCode",smtWorkOrder.getWorkOrderCode());

                SmtWorkOrder workOrder = smtWorkOrderMapper.selectOneByExample(example);

                if(StringUtils.isNotEmpty(workOrder)&&!workOrder.getWorkOrderId().equals(smtWorkOrder.getWorkOrderId())){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001);
                }

                if(!order.getMaterialId().equals(smtWorkOrder.getMaterialId())){
                    throw new BizErrorException("工单不能修改产品料号信息");
                }

                if(workOrderStatus==0||workOrderStatus==2){
                    //工单的工单数量改变
                    if(!order.getWorkOrderQuantity().equals(smtWorkOrder.getWorkOrderQuantity())){
                        Example example1 = new Example(SmtWorkOrderBom.class);
                        Example.Criteria criteria1 = example1.createCriteria();
                        criteria1.andEqualTo("workOrderId",smtWorkOrder.getWorkOrderId());
                        List<SmtWorkOrderBom> workOrderBoms = smtWorkOrderBomMapper.selectByExample(example1);
                        if(StringUtils.isNotEmpty(workOrderBoms)){
                            for (SmtWorkOrderBom smtWorkOrderBom : workOrderBoms) {
                                //工单BOM的单个用量
                                BigDecimal singleQuantity = smtWorkOrderBom.getSingleQuantity();
                                smtWorkOrderBom.setQuantity(new BigDecimal(smtWorkOrder.getWorkOrderQuantity().toString()).multiply(singleQuantity));
                                list.add(smtWorkOrderBom);

                                //新增工单BOM历史信息
                                SmtHtWorkOrderBom smtHtWorkOrderBom=new SmtHtWorkOrderBom();
                                BeanUtils.copyProperties(smtWorkOrderBom,smtHtWorkOrderBom);
                                smtHtWorkOrderBom.setModifiedUserId(currentUser.getUserId());
                                smtHtWorkOrderBom.setModifiedTime(new Date());
                                htList.add(smtHtWorkOrderBom);
                            }
                            //批量修改工单BOM的用量
                            smtWorkOrderBomMapper.updateBatch(list);

                            //批量新增工单BOM历史信息
                            smtHtWorkOrderBomMapper.insertList(htList);
                        }
                    }
                }
                smtWorkOrder.setModifiedUserId(currentUser.getUserId());
                smtWorkOrder.setModifiedTime(new Date());
                i= smtWorkOrderMapper.updateByPrimaryKeySelective(smtWorkOrder);


                //新增工单历史信息
                SmtHtWorkOrder smtHtWorkOrder=new SmtHtWorkOrder();
                BeanUtils.copyProperties(smtWorkOrder,smtHtWorkOrder);
                smtHtWorkOrder.setModifiedUserId(currentUser.getUserId());
                smtHtWorkOrder.setModifiedTime(new Date());
                smtHtWorkOrderMapper.insertSelective(smtHtWorkOrder);
            }else {
                throw new BizErrorException("生产完成的工单不允许修改");
            }

            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
            List<SmtHtWorkOrder> list=new ArrayList<>();

            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            String[] workOrderIds = ids.split(",");
            for (String workOrderId : workOrderIds) {
                SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByPrimaryKey(workOrderId);
                if(StringUtils.isEmpty(smtWorkOrder)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //新增工单历史信息
                SmtHtWorkOrder smtHtWorkOrder=new SmtHtWorkOrder();
                BeanUtils.copyProperties(smtWorkOrder,smtHtWorkOrder);
                smtHtWorkOrder.setModifiedUserId(currentUser.getUserId());
                smtHtWorkOrder.setModifiedTime(new Date());
                list.add(smtHtWorkOrder);

                Example example = new Example(SmtWorkOrderBom.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("workOrderId",workOrderId);
                List<SmtWorkOrderBom> smtWorkOrderBoms = smtWorkOrderBomMapper.selectByExample(example);
                if(StringUtils.isNotEmpty(smtWorkOrderBoms)){
                    throw new BizErrorException("工单被引用，不能删除");
                }

            }
            smtHtWorkOrderMapper.insertList(list);

            return smtWorkOrderMapper.deleteByIds(ids);
        }


        @Override
        public List<SmtWorkOrderDto> findList(SearchSmtWorkOrder searchSmtWorkOrder) {
            return smtWorkOrderMapper.findList(searchSmtWorkOrder);
        }
}
