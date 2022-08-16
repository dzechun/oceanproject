package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseAddressDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseAddressImport;
import com.fantechs.common.base.general.entity.basic.BaseAddress;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseAddressMapper;
import com.fantechs.provider.base.service.BaseAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2020/11/13.
 */
@Service
public class BaseAddressServiceImpl extends BaseService<BaseAddress> implements BaseAddressService {

    @Resource
    private BaseAddressMapper baseAddressMapper;

    @Override
    public int save(BaseAddress baseAddress) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(BaseAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("addressDetail", baseAddress.getAddressDetail());
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        List<BaseAddress> baseAddresses = baseAddressMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseAddresses)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        baseAddress.setCreateUserId(currentUser.getUserId());
        baseAddress.setCreateTime(new Date());
        baseAddress.setModifiedTime(new Date());
        baseAddress.setModifiedUserId(currentUser.getUserId());
        baseAddress.setStatus(StringUtils.isEmpty(baseAddress.getStatus())?1: baseAddress.getStatus());
        baseAddress.setOrganizationId(currentUser.getOrganizationId());
        return baseAddressMapper.insertUseGeneratedKeys(baseAddress);
    }

    @Override
    public int update(BaseAddress baseAddress) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("addressDetail", baseAddress.getAddressDetail())
        .andNotEqualTo("addressId", baseAddress.getAddressId());
        List<BaseAddress> baseAddresses = baseAddressMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseAddresses)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseAddress.setModifiedUserId(currentUser.getUserId());
        baseAddress.setModifiedTime(new Date());
        baseAddress.setOrganizationId(currentUser.getOrganizationId());
        return baseAddressMapper.updateByPrimaryKeySelective(baseAddress);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseAddress baseAddress = baseAddressMapper.selectByPrimaryKey(Long.valueOf(id));
            if (StringUtils.isEmpty(baseAddress)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return baseAddressMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseAddressDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return baseAddressMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseAddressImport> baseAddressImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseAddress> list = new LinkedList<>();
        ArrayList<BaseAddressImport> addressImports = new ArrayList<>();

        for (int i = 0; i < baseAddressImports.size(); i++) {
            BaseAddressImport baseAddressImport = baseAddressImports.get(i);
            String addressDetail = baseAddressImport.getAddressDetail();
            if (StringUtils.isEmpty(
                    addressDetail
            )) {
                fail.add(i + 4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseAddress.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("addressDetail", addressDetail);
            if (StringUtils.isNotEmpty(baseAddressMapper.selectOneByExample(example))) {
                fail.add(i + 4);
                continue;
            }

            //判断集合中是否存在重复数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(addressImports)){
                for (BaseAddressImport addressImport : addressImports) {
                    if (addressImport.getAddressDetail().equals(addressDetail)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }
            addressImports.add(baseAddressImport);
        }

        if (StringUtils.isNotEmpty(addressImports)) {
            for (BaseAddressImport baseAddressImport : addressImports) {
                BaseAddress baseAddress = new BaseAddress();
                BeanUtils.copyProperties(baseAddressImport, baseAddress);
                //处理省市区数据
                String completeDetail = "";
                if(StringUtils.isNotEmpty(baseAddressImport.getProvince())) {
                    String[] province = baseAddressImport.getProvince().split("-");
                    baseAddress.setProvinceCode(province[0]);
                    completeDetail = completeDetail+province[1];
                }
                if(StringUtils.isNotEmpty(baseAddressImport.getCity())) {
                    String[] city = baseAddressImport.getCity().split("-");
                    baseAddress.setCityCode(city[0]);
                    completeDetail = completeDetail+city[1];
                }
                if(StringUtils.isNotEmpty(baseAddressImport.getClassify())) {
                    String[] classify = baseAddressImport.getClassify().split("-");
                    baseAddress.setClassifyCode(classify[0]);
                    completeDetail = completeDetail+classify[1];
                }
                baseAddress.setCompleteDetail(completeDetail + baseAddressImport.getAddressDetail());

                baseAddress.setCreateTime(new Date());
                baseAddress.setCreateUserId(currentUser.getUserId());
                baseAddress.setModifiedTime(new Date());
                baseAddress.setModifiedUserId(currentUser.getUserId());
                baseAddress.setStatus((byte)1);
                baseAddress.setOrganizationId(currentUser.getOrganizationId());
                list.add(baseAddress);
            }

            success += baseAddressMapper.insertList(list);

        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }

}
