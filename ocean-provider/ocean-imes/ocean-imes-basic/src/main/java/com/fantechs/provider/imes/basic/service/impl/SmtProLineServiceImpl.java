package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.SmtWorkShopDto;
import com.fantechs.common.base.entity.basic.SmtDept;
import com.fantechs.common.base.entity.basic.SmtProLine;
import com.fantechs.common.base.entity.basic.SmtProductProcessRoute;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.entity.basic.history.SmtHtDept;
import com.fantechs.common.base.entity.basic.history.SmtHtProLine;
import com.fantechs.common.base.entity.basic.search.SearchSmtProLine;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtProLineMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProLineMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProductProcessRouteMapper;
import com.fantechs.provider.imes.basic.mapper.SmtWorkShopMapper;
import com.fantechs.provider.imes.basic.service.SmtFactoryService;
import com.fantechs.provider.imes.basic.service.SmtProLineService;
import com.fantechs.provider.imes.basic.service.SmtWorkShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Service
public class SmtProLineServiceImpl  extends BaseService<SmtProLine> implements SmtProLineService {

    @Resource
    private SmtProLineMapper smtProLineMapper;

    @Resource
    private SmtHtProLineMapper smtHtProLineMapper;

    @Resource
    private SmtProductProcessRouteMapper smtProductProcessRouteMapper;

    @Resource
    private SmtFactoryService smtFactoryService;

    @Resource
    private SmtWorkShopMapper smtWorkShopMapper;

    @Override
    public List<SmtProLine> findList(SearchSmtProLine searchSmtProLine) {
        return smtProLineMapper.findList(searchSmtProLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProLine smtProLine) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int i=0;
        Example example = new Example(SmtProLine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proCode",smtProLine.getProCode());
        List<SmtProLine> smtProLines = smtProLineMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(smtProLines)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProLine.setCreateUserId(currentUser.getUserId());
        smtProLine.setCreateTime(new Date());
        smtProLine.setModifiedUserId(currentUser.getUserId());
        smtProLine.setModifiedTime(new Date());
        smtProLineMapper.insertUseGeneratedKeys(smtProLine);

        //新增生产线历史信息
        SmtHtProLine smtHtProLine=new SmtHtProLine();
        BeanUtils.copyProperties(smtProLine,smtHtProLine);
        i = smtHtProLineMapper.insertSelective(smtHtProLine);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProLine smtProLine) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProLine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proCode",smtProLine.getProCode());
        SmtProLine proLine = smtProLineMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(proLine)&&!proLine.getProLineId().equals(smtProLine.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProLine.setModifiedUserId(currentUser.getUserId());
        smtProLine.setModifiedTime(new Date());
        int i= smtProLineMapper.updateByPrimaryKeySelective(smtProLine);

        //新增生产线历史信息
        SmtHtProLine smtHtProLine=new SmtHtProLine();
        BeanUtils.copyProperties(smtProLine,smtHtProLine);
        smtHtProLineMapper.insertSelective(smtHtProLine);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        List<SmtHtProLine> list=new ArrayList<>();
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] idsArr = ids.split(",");
        for (String proLineId : idsArr) {
            SmtProLine smtProLine = smtProLineMapper.selectByPrimaryKey(proLineId);
            if(StringUtils.isEmpty(smtProLine)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }


            //被产品工艺路线引用
            Example example = new Example(SmtProductProcessRoute.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("proLineId",proLineId);
            List<SmtProductProcessRoute> smtProductProcessRoutes = smtProductProcessRouteMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtProductProcessRoutes)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增生产线历史信息
            SmtHtProLine smtHtProLine=new SmtHtProLine();
            BeanUtils.copyProperties(smtProLine,smtHtProLine);
            smtHtProLine.setModifiedUserId(currentUser.getUserId());
            smtHtProLine.setModifiedTime(new Date());
            list.add(smtHtProLine);

        }
         smtHtProLineMapper.insertList(list);
         i=smtProLineMapper.deleteByIds(ids);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SmtProLine> smtProLines) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SmtProLine> list = new LinkedList<>();
        LinkedList<SmtHtProLine> htList = new LinkedList<>();

        for (int i = 0; i < smtProLines.size(); i++) {
            SmtProLine smtProLine = smtProLines.get(i);
            String proCode = smtProLine.getProCode();
            String proName = smtProLine.getProName();
            String factoryCode = smtProLine.getFactoryCode();
            String workShopCode = smtProLine.getWorkShopCode();
            if (StringUtils.isEmpty(
                    proCode,proName,factoryCode,workShopCode
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(SmtProLine.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("proCode",smtProLine.getProCode());
            if (StringUtils.isNotEmpty(smtProLineMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            //判断工厂是否存在
            HashMap<String, Object> map = new HashMap<>();
            map.put("factoryCode",smtProLine.getFactoryCode());
            map.put("codeQueryMark",1);
            List<SmtFactoryDto> list1 = smtFactoryService.findList(map);
            if (StringUtils.isEmpty(list1)){
                fail.add(i+3);
                continue;
            }

            //判断车间是否存在
            Example example1 = new Example(SmtWorkShop.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("workShopCode",smtProLine.getWorkShopCode());
            SmtWorkShop smtWorkShop = smtWorkShopMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(smtWorkShop)){
                fail.add(i+3);
                continue;
            }

            smtProLine.setCreateTime(new Date());
            smtProLine.setCreateUserId(currentUser.getUserId());
            smtProLine.setModifiedTime(new Date());
            smtProLine.setModifiedUserId(currentUser.getUserId());
            smtProLine.setStatus(1);
            smtProLine.setFactoryId(list1.get(0).getFactoryId());
            smtProLine.setWorkShopId(smtWorkShop.getWorkShopId());
            list.add(smtProLine);
        }

        if (StringUtils.isNotEmpty(list)){
            success = smtProLineMapper.insertList(list);
        }

        for (SmtProLine smtProLine : list) {
            SmtHtProLine smtHtProLine = new SmtHtProLine();
            BeanUtils.copyProperties(smtProLine,smtHtProLine);
            htList.add(smtHtProLine);
        }

        if (StringUtils.isNotEmpty(htList)){
            smtHtProLineMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
