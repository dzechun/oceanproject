package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.SmtWorkShopDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtProLine;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.entity.basic.history.SmtHtFactory;
import com.fantechs.common.base.entity.basic.history.SmtHtWorkShop;
import com.fantechs.common.base.entity.basic.search.SearchSmtFactory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseTeamDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseTeam;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.imes.basic.mapper.SmtHtWorkShopMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProLineMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWorkShopMapper;
import com.fantechs.provider.imes.basic.service.SmtFactoryService;
import com.fantechs.provider.imes.basic.service.SmtWorkShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * Created by lfz on 2020/9/1.
 */
@Service
public class SmtWorkShopServiceImpl extends BaseService<SmtWorkShop> implements SmtWorkShopService {

    @Resource
    private SmtWorkShopMapper smtWorkShopMapper;
    @Resource
    private SmtHtWorkShopMapper smtHtWorkShopMapper;
    @Resource
    private SmtProLineMapper smtProLineMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SmtFactoryService  smtFactoryService;

    @Override
    public List<SmtWorkShopDto> findList(Map<String, Object> map) {
        return smtWorkShopMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtWorkShop smtWorkShop) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtWorkShop.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workShopCode", smtWorkShop.getWorkShopCode());
        SmtWorkShop odlSmtWorkShop = smtWorkShopMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(odlSmtWorkShop)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtWorkShop.setCreateUserId(user.getUserId());
        smtWorkShop.setCreateTime(new Date());
        smtWorkShop.setModifiedUserId(user.getUserId());
        smtWorkShop.setModifiedTime(new Date());
        smtWorkShop.setStatus(StringUtils.isEmpty(smtWorkShop.getStatus())?1:smtWorkShop.getStatus());
        smtWorkShopMapper.insertUseGeneratedKeys(smtWorkShop);

        SmtHtWorkShop smtHtWorkShop  = new SmtHtWorkShop();
        BeanUtils.copyProperties(smtWorkShop,smtHtWorkShop);
        int i = smtHtWorkShopMapper.insert(smtHtWorkShop);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<SmtHtWorkShop> list=new LinkedList<>();
        String[] idsArr =  ids.split(",");
        for(String id : idsArr){
            SmtWorkShop smtWorkShop = smtWorkShopMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(smtWorkShop)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被生产线引用
            Example example = new Example(SmtProLine.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workShopId",smtWorkShop.getWorkShopId());
            List<SmtProLine> smtProLines = smtProLineMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProLines)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //被班组引用
            SearchBaseTeam searchBaseTeam = new SearchBaseTeam();
            searchBaseTeam.setWorkShopId(Long.valueOf(id));
            List<BaseTeamDto> baseTeamDtos = baseFeignApi.findTeamList(searchBaseTeam).getData();
            if (StringUtils.isNotEmpty(baseTeamDtos)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            SmtHtWorkShop smtHtWorkShop = new SmtHtWorkShop();
            BeanUtils.copyProperties(smtWorkShop,smtHtWorkShop);
            smtHtWorkShop.setModifiedUserId(currentUser.getUserId());
            smtHtWorkShop.setModifiedTime(new Date());
            list.add(smtHtWorkShop);
        }
         smtHtWorkShopMapper.insertList(list);
        return smtWorkShopMapper.deleteByIds(ids);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtWorkShop smtWorkShop) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(SmtWorkShop.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workShopCode", smtWorkShop.getWorkShopCode());

        SmtWorkShop odlsmtWorkShop = smtWorkShopMapper.selectOneByExample(example);

        if(StringUtils.isNotEmpty(odlsmtWorkShop)&&!odlsmtWorkShop.getWorkShopId().equals(smtWorkShop.getWorkShopId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        smtWorkShop.setModifiedTime(new Date());
        smtWorkShop.setModifiedUserId(user.getUserId());

        SmtHtWorkShop smtHtWorkShop  = new SmtHtWorkShop();
        BeanUtils.copyProperties(smtWorkShop,smtHtWorkShop);

        smtHtWorkShopMapper.insert(smtHtWorkShop);

        return smtWorkShopMapper.updateByPrimaryKeySelective(smtWorkShop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtWorkShopDto> smtWorkShopDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtWorkShop> list = new LinkedList<>();
        LinkedList<SmtHtWorkShop> htList = new LinkedList<>();
        for (int i = 0; i < smtWorkShopDtos.size(); i++) {
            SmtWorkShopDto smtWorkShopDto = smtWorkShopDtos.get(i);
            String factoryCode = smtWorkShopDto.getFactoryCode();
            String workShopCode = smtWorkShopDto.getWorkShopCode();
            String workShopName = smtWorkShopDto.getWorkShopName();
            if (StringUtils.isEmpty(
                    factoryCode,workShopCode,workShopName
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtFactory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workShopCode",smtWorkShopDto.getWorkShopCode());
            if (StringUtils.isNotEmpty(smtWorkShopMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            //判断工厂是否存在
            SearchSmtFactory searchSmtFactory = new SearchSmtFactory();
            searchSmtFactory.setFactoryCode(smtWorkShopDto.getFactoryCode());
            searchSmtFactory.setCodeQueryMark((byte) 1);
            SmtFactoryDto factoryDto = smtFactoryService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtFactory)).get(0);
            if (StringUtils.isEmpty(factoryDto)){
                fail.add(i+3);
                continue;
            }

            SmtWorkShop smtWorkShop = new SmtWorkShop();
            BeanUtils.copyProperties(smtWorkShopDto,smtWorkShop);
            smtWorkShop.setCreateTime(new Date());
            smtWorkShop.setCreateUserId(currentUser.getUserId());
            smtWorkShop.setModifiedTime(new Date());
            smtWorkShop.setModifiedUserId(currentUser.getUserId());
            smtWorkShop.setFactoryId(factoryDto.getFactoryId());
            smtWorkShop.setStatus(1);
            list.add(smtWorkShop);
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtWorkShopMapper.insertList(list);
        }

        for (SmtWorkShop smtWorkShop : list) {
            SmtHtWorkShop smtHtWorkShop = new SmtHtWorkShop();
            BeanUtils.copyProperties(smtWorkShop,smtHtWorkShop);
            htList.add(smtHtWorkShop);
        }

        if (StringUtils.isNotEmpty(htList)){
            smtHtWorkShopMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
