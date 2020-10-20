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
import javax.validation.constraints.NotNull;
import java.util.List;

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
            Long routeId = list.get(0).getRouteId();

            /**
             * 删除上一次配置的工艺路线
             */
            Example example = new Example(SmtRouteProcess.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("routeId",routeId);
            smtRouteProcessMapper.deleteByExample(example);

            for (SmtRouteProcess smtRouteProcess : list) {
                if(smtRouteProcess.getIsPass()==0){
                    Long nextProcessId = smtRouteProcess.getNextProcessId();
                    if(StringUtils.isNotEmpty(nextProcessId)){
                        SmtProcess nextProcess = smtProcessMapper.selectByPrimaryKey(nextProcessId);
                        if(StringUtils.isNotEmpty(nextProcess)&&!nextProcess.getProcessName().equals("维修工序")){
                            throw new BizErrorException("该工序出故障，需要到维修工序去维修");
                        }
                    }

                }
            }

            return smtRouteProcessMapper.insertList(list);
        }

        @Override
        public List<SmtRouteProcess> findConfigureRout(Long routeId) {
            return smtRouteProcessMapper.findList(routeId);
        }
}
