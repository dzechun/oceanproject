package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.SmtProcessCategory;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtProcessCategoryMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProcessMapper;
import com.fantechs.provider.imes.basic.mapper.SmtRouteProcessMapper;
import com.fantechs.provider.imes.basic.service.SmtRouteProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class SmtRouteProcessServiceImpl extends BaseService<SmtRouteProcess> implements SmtRouteProcessService {

        @Resource
        private SmtRouteProcessMapper smtRouteProcessMapper;
        @Resource
        private SmtProcessMapper smtProcessMapper;
        @Resource
        private SmtProcessCategoryMapper smtProcessCategoryMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int configureRout(List<SmtRouteProcess> list) {
            if(StringUtils.isEmpty(list)){
                throw new BizErrorException("没有配置工艺路线，请去配置");
            }
            Long routeId = list.get(0).getRouteId();

            /**
             * 删除上一次配置的工艺路线
             */
            Example example = new Example(SmtRouteProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("routeId",routeId);
            smtRouteProcessMapper.deleteByExample(example);

            List<Long> processIds=new ArrayList<>();
            for (int i=0;i<list.size();i++) {
                SmtRouteProcess smtRouteProcess = list.get(i);
                Long processId = smtRouteProcess.getProcessId();
                Long nextProcessId = smtRouteProcess.getNextProcessId();
                Long previousProcessId = smtRouteProcess.getPreviousProcessId();
                Integer orderNum = smtRouteProcess.getOrderNum();

                processIds.add(processId);
                //当前工序
                SmtProcess smtProcess = smtProcessMapper.selectByPrimaryKey(processId);
                if(StringUtils.isNotEmpty(smtProcess)){
                    //查询当前工序的工序类别
                    SmtProcessCategory smtProcessCategory = smtProcessCategoryMapper.selectByPrimaryKey(smtProcess.getProcessCategoryId());
                    if(StringUtils.isNotEmpty(smtProcessCategory)&&!smtProcessCategory.getProcessCategoryCode().equalsIgnoreCase("repair")){
                        if(StringUtils.isEmpty(orderNum)){
                            throw new BizErrorException("非维修工序的工序顺序不能为空");
                        }
                    }

                    if(list.size()>1){
                        if(i>0){
                            if(previousProcessId.equals(list.get(i-1).getPreviousProcessId())){
                                //上一道工序相同的两条数据的当前工序为检验的工序或维修工序
                                if(StringUtils.isNotEmpty(nextProcessId)){
                                    if(smtRouteProcess.getIsPass()==0){
                                        //下一道工序
                                        SmtProcess nextProcess = smtProcessMapper.selectByPrimaryKey(processId);
                                        if(StringUtils.isNotEmpty(nextProcess)){
                                            //查询当前工序的下一道工序的工序类别
                                            SmtProcessCategory nextProcessCategory = smtProcessCategoryMapper.selectByPrimaryKey(nextProcess.getProcessCategoryId());
                                            //判断当前数据的下一道工序是否是维修工序
                                            if(StringUtils.isNotEmpty(nextProcessCategory)&&nextProcessCategory.getProcessCategoryCode().equalsIgnoreCase("repair")){
                                                if(list.size()>i+1){
                                                    if(!processIds.contains(list.get(i+1).getNextProcessId())){
                                                        throw new BizErrorException("维修后，不能执行当前工序以后的工序");
                                                    }
                                                }else {
                                                    throw new BizErrorException("工艺路线配置错误");
                                                }
                                            }else {
                                                if(previousProcessId.equals(list.get(i-1).getPreviousProcessId())&StringUtils.isNotEmpty(orderNum)){
                                                    continue;
                                                }else {
                                                    throw new BizErrorException("工艺路线配置错误");
                                                }
                                            }
                                        }
                                    }
                                }
                            }else {
                                //当前数据的工序和上一条数据的下一道工序相同并且当前工序顺序大于上一条数据的工序顺序才能通过
                                if(processId.equals(list.get(i-1).getNextProcessId())){
                                    if(!smtProcessCategory.getProcessCategoryCode().equalsIgnoreCase("repair")){
                                        if(orderNum>list.get(i-1).getOrderNum()){
                                            continue;
                                        }else {
                                            throw new BizErrorException("工艺路线配置错误");
                                        }
                                    }
                                }else {
                                    throw new BizErrorException("工艺路线配置错误");
                                }
                            }
                        }
                    }
                }
            }

            return smtRouteProcessMapper.insertList(list);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int configureProcess(Map<String, Object> map) {
            return smtRouteProcessMapper.configureProcess(map);
        }

        @Override
        public List<SmtRouteProcess> findConfigureRout(Long routeId) {
            return smtRouteProcessMapper.findList(routeId);
        }


}
