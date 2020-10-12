package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtRoute;
import com.fantechs.common.base.entity.basic.history.SmtHtRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtRoute;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtRouteMapper;
import com.fantechs.provider.imes.basic.mapper.SmtRouteMapper;
import com.fantechs.provider.imes.basic.service.SmtRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class SmtRouteServiceImpl extends BaseService<SmtRoute> implements SmtRouteService {

      @Resource
      private SmtRouteMapper smtRouteMapper;
      @Resource
      private SmtHtRouteMapper smtHtRouteMapper;

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int save(SmtRoute smtRoute) {
          SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
          if(StringUtils.isEmpty(currentUser)){
              throw new BizErrorException(ErrorCodeEnum.UAC10011039);
          }

          Example example = new Example(SmtRoute.class);
          Example.Criteria criteria = example.createCriteria();
          criteria.andEqualTo("routeName",smtRoute.getRouteName());
          List<SmtRoute> smtRoutes = smtRouteMapper.selectByExample(example);
          if(StringUtils.isNotEmpty(smtRoutes)){
              throw new BizErrorException("工艺路线名称已存在");
          }

          smtRoute.setCreateUserId(currentUser.getUserId());
          smtRoute.setCreateTime(new Date());
          smtRouteMapper.insertUseGeneratedKeys(smtRoute);

          //新增工艺路线历史信息
          SmtHtRoute smtHtRoute=new SmtHtRoute();
          BeanUtils.copyProperties(smtRoute,smtHtRoute);
          int i = smtHtRouteMapper.insertSelective(smtHtRoute);
          return i;
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(SmtRoute smtRoute) {
          SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
          if(StringUtils.isEmpty(currentUser)){
              throw new BizErrorException(ErrorCodeEnum.UAC10011039);
          }

          Example example = new Example(SmtRoute.class);
          Example.Criteria criteria = example.createCriteria();
          criteria.andEqualTo("routeName",smtRoute.getRouteName());

          SmtRoute route = smtRouteMapper.selectOneByExample(example);

          if(StringUtils.isNotEmpty(route)&&!route.getRouteId().equals(smtRoute.getRouteId())){
              throw new BizErrorException("工艺路线名称已存在");
          }

          smtRoute.setModifiedUserId(currentUser.getUserId());
          smtRoute.setModifiedTime(new Date());
          int i= smtRouteMapper.updateByPrimaryKeySelective(smtRoute);

          //新增工艺路线历史信息
          SmtHtRoute smtHtRoute=new SmtHtRoute();
          BeanUtils.copyProperties(smtRoute,smtHtRoute);
          smtHtRoute.setModifiedUserId(currentUser.getUserId());
          smtHtRoute.setModifiedTime(new Date());
          smtHtRouteMapper.insertSelective(smtHtRoute);
          return i;
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
     public int batchDelete(String ids) {
         int i=0;
         List<SmtHtRoute> list=new ArrayList<>();

         SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
         if(StringUtils.isEmpty(currentUser)){
             throw new BizErrorException(ErrorCodeEnum.UAC10011039);
         }

         String[] routeIds = ids.split(",");
         for (String routeId : routeIds) {
             SmtRoute smtRoute = smtRouteMapper.selectByPrimaryKey(Long.parseLong(routeId));
             if(StringUtils.isEmpty(smtRoute)){
                 throw new BizErrorException(ErrorCodeEnum.OPT20012003);
             }
             //新增工艺路线历史信息
             SmtHtRoute smtHtRoute=new SmtHtRoute();
             BeanUtils.copyProperties(smtRoute,smtHtRoute);
             smtHtRoute.setModifiedUserId(currentUser.getUserId());
             smtHtRoute.setModifiedTime(new Date());
             list.add(smtHtRoute);
         }
         smtHtRouteMapper.insertList(list);

         return smtRouteMapper.deleteByIds(ids);
     }

    @Override
    public List<SmtRoute> findList(SearchSmtRoute searchSmtRoute) {
        return smtRouteMapper.findList(searchSmtRoute);
    }
}
