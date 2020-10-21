package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtProcessMapper;
import com.fantechs.provider.imes.basic.mapper.SmtRouteProcessMapper;
import com.fantechs.provider.imes.basic.service.SmtRouteProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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

            for (SmtRouteProcess smtRouteProcess : list) {
                Long processId = smtRouteProcess.getProcessId();
                Long nextProcessId = smtRouteProcess.getNextProcessId();
                Integer orderNum = smtRouteProcess.getOrderNum();
                SmtProcess smtProcess = smtProcessMapper.selectByPrimaryKey(processId);
                if(StringUtils.isNotEmpty(smtProcess)&&!smtProcess.getProcessName().equals("维修工序")){
                    if(StringUtils.isEmpty(orderNum)){
                        throw new BizErrorException("非维修工序工序顺序不能为空");
                    }
                }
                if(smtRouteProcess.getIsPass()==0){
                    if(StringUtils.isNotEmpty(nextProcessId)){
                        SmtProcess nextProcess = smtProcessMapper.selectByPrimaryKey(nextProcessId);
                        if(StringUtils.isNotEmpty(nextProcess)&&!nextProcess.getProcessName().equals("维修工序")){
                            throw new BizErrorException("该工序出故障，需要到维修工序去维修");
                        }
                    }

                    //该出现故障工序对应的维修工序
                    Example example1 = new Example(SmtRouteProcess.class);
                    Example.Criteria criteria1 = example1.createCriteria();
                    criteria1.andEqualTo("routeId",routeId);
                    criteria1.andEqualTo("previousProcessId",processId);
                    criteria1.andEqualTo("processId",nextProcessId);
                    SmtRouteProcess routeProcess = smtRouteProcessMapper.selectOneByExample(example1);
                    if(StringUtils.isNotEmpty(routeProcess)){
                        //该出现故障工序对应的维修工序的下一道工序
                        Example example2 = new Example(SmtRouteProcess.class);
                        Example.Criteria criteria2 = example2.createCriteria();
                        criteria2.andEqualTo("routeId",routeId);
                        criteria2.andEqualTo("previousProcessId",routeProcess.getProcessId());
                        criteria2.andEqualTo("processId",routeProcess.getNextProcessId());
                        SmtRouteProcess process = smtRouteProcessMapper.selectOneByExample(example2);
                        if(StringUtils.isNotEmpty(process)){
                            if(process.getOrderNum()>orderNum){
                                throw new BizErrorException("该工序维修后，不能返回该工序的后续工序");
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
