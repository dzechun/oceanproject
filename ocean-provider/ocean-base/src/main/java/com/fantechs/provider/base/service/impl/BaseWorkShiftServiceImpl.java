package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.SmtWorkShopDto;
import com.fantechs.common.base.entity.basic.SmtFactory;
import com.fantechs.common.base.entity.basic.SmtWorkShop;
import com.fantechs.common.base.entity.basic.history.SmtHtWorkShop;
import com.fantechs.common.base.entity.basic.search.SearchSmtFactory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWorkShiftDto;
import com.fantechs.common.base.general.entity.basic.BaseProductFamily;
import com.fantechs.common.base.general.entity.basic.BaseWorkShift;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkShift;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWorkShiftMapper;
import com.fantechs.provider.base.mapper.BaseWorkShiftMapper;
import com.fantechs.provider.base.service.BaseWorkShiftService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * Created by leifengzhi on 2020/12/21.
 */
@Service
public class BaseWorkShiftServiceImpl extends BaseService<BaseWorkShift> implements BaseWorkShiftService {

    @Resource
    private BaseWorkShiftMapper baseWorkShiftMapper;
    @Resource
    private BaseHtWorkShiftMapper baseHtWorkShiftMapper;

    @Override
    public int save(BaseWorkShift baseWorkShift) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseWorkShift.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("workShiftCode",baseWorkShift.getWorkShiftCode());
        BaseWorkShift baseWorkShift1 = baseWorkShiftMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseWorkShift1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        //新增班次
        baseWorkShift.setCreateTime(new Date());
        baseWorkShift.setCreateUserId(user.getUserId());
        baseWorkShift.setModifiedTime(new Date());
        baseWorkShift.setModifiedUserId(user.getUserId());
        baseWorkShift.setStatus(StringUtils.isEmpty(baseWorkShift.getStatus())?1:baseWorkShift.getStatus());
        int i = baseWorkShiftMapper.insertUseGeneratedKeys(baseWorkShift);

        //新增班次履历
        BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
        BeanUtils.copyProperties(baseWorkShift,baseHtWorkShift);
        baseHtWorkShiftMapper.insert(baseHtWorkShift);

        return i;
    }

    @Override
    public int update(BaseWorkShift baseWorkShift) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseWorkShift.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("workShiftCode",baseWorkShift.getWorkShiftCode())
                .andNotEqualTo("workShiftId",baseWorkShift.getWorkShiftId());
        BaseWorkShift baseWorkShift1 = baseWorkShiftMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseWorkShift1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }


        baseWorkShift.setModifiedUserId(user.getUserId());
        baseWorkShift.setModifiedTime(new Date());

        BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
        BeanUtils.copyProperties(baseWorkShift,baseHtWorkShift);
        baseHtWorkShiftMapper.insert(baseHtWorkShift);

        return baseWorkShiftMapper.updateByPrimaryKeySelective(baseWorkShift);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtWorkShift> baseHtWorkShifts = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseWorkShift baseWorkShift = baseWorkShiftMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseWorkShift)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
            BeanUtils.copyProperties(baseWorkShift,baseHtWorkShift);
            baseHtWorkShifts.add(baseHtWorkShift);
        }

        baseHtWorkShiftMapper.insertList(baseHtWorkShifts);
        return baseWorkShiftMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseWorkShiftDto> findList(Map<String, Object> map) {
        return baseWorkShiftMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseWorkShiftDto> baseWorkShiftDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseWorkShift> list = new LinkedList<>();
        LinkedList<BaseHtWorkShift> htList = new LinkedList<>();
        for (int i = 0; i < baseWorkShiftDtos.size(); i++) {
            BaseWorkShiftDto baseWorkShiftDto = baseWorkShiftDtos.get(i);
            String workShiftCode = baseWorkShiftDto.getWorkShiftCode();
            String workShiftName = baseWorkShiftDto.getWorkShiftName();
            if (StringUtils.isEmpty(
                    workShiftCode,workShiftName
            )){
                fail.add(i+3);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseWorkShift.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workShiftCode",baseWorkShiftDto.getWorkShiftCode());
            if (StringUtils.isNotEmpty(baseWorkShiftMapper.selectOneByExample(example))){
                fail.add(i+3);
                continue;
            }

            BaseWorkShift baseWorkShift = new BaseWorkShift();
            BeanUtils.copyProperties(baseWorkShiftDto,baseWorkShift);
            baseWorkShift.setCreateTime(new Date());
            baseWorkShift.setCreateUserId(currentUser.getUserId());
            baseWorkShift.setModifiedTime(new Date());
            baseWorkShift.setModifiedUserId(currentUser.getUserId());
            baseWorkShift.setStatus((byte) 1);
            list.add(baseWorkShift);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseWorkShiftMapper.insertList(list);
        }

        for (BaseWorkShift baseWorkShift : list) {
            BaseHtWorkShift baseHtWorkShift = new BaseHtWorkShift();
            BeanUtils.copyProperties(baseWorkShift,baseHtWorkShift);
            htList.add(baseHtWorkShift);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtWorkShiftMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
