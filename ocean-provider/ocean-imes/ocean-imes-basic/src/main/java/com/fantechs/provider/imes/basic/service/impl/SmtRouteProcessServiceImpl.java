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

            for (SmtRouteProcess smtRouteProcess : list) {
                Long processId = smtRouteProcess.getProcessId();
                Long nextProcessId = smtRouteProcess.getNextProcessId();
                Integer orderNum = smtRouteProcess.getOrderNum();
                SmtProcess smtProcess = smtProcessMapper.selectByPrimaryKey(processId);

                if(StringUtils.isNotEmpty(smtProcess)){
                    //查询当前工序的工序类别
                    SmtProcessCategory smtProcessCategory = smtProcessCategoryMapper.selectByPrimaryKey(smtProcess.getProcessCategoryId());
                    if(!smtProcessCategory.getProcessCategoryCode().equalsIgnoreCase("repair")){
                        if(StringUtils.isEmpty(orderNum)){
                            throw new BizErrorException("非维修工序的工序顺序不能为空");
                        }

                        if(smtRouteProcess.getIsPass()==0){
                            throw new BizErrorException("非维修工序的工序是否通过不能为否");
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
