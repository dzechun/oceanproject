package com.fantechs.provider.restapi.mulinsen.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.general.dto.mulinsen.HrUserInfoDto;
import com.fantechs.common.base.general.entity.mulinsen.HrUserInfo;
import com.fantechs.common.base.general.entity.mulinsen.search.SearchHrUserInfo;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.restapi.mulinsen.mapper.HrUserInfoMapper;
import com.fantechs.provider.restapi.mulinsen.service.HrUserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class HrUserInfoServiceImpl implements HrUserInfoService {

    @Resource
    private HrUserInfoMapper hrUserInfoMapper;

    @Resource
    private SecurityFeignApi securityFeignApi;

    @Value("${deptCodePrefix}")
    private String deptCodePrefix;

    @Override
    public List<HrUserInfoDto> findList(SearchHrUserInfo searchHrUserInfo) {
        return hrUserInfoMapper.findList(searchHrUserInfo);
    }

    @Override
    @Transactional
    @LcnTransaction
    public int synchronizeHrUserInfo() throws Exception {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchHrUserInfo searchHrUserInfo = new SearchHrUserInfo();
        searchHrUserInfo.setDeptCodePrefix(deptCodePrefix);
        searchHrUserInfo.setNotDataStatus(1);
        List<HrUserInfoDto> hrUserInfoDtoList = findList(searchHrUserInfo);

        SearchSysUser searchSysUser = new SearchSysUser();
        searchSysUser.setStartPage(1);
        searchSysUser.setPageSize(999999);
        List<SysUser> sysUserList = securityFeignApi.selectUsers(searchSysUser).getData();

        List<HrUserInfo> hrUserInfoList = new LinkedList<>();
        for (HrUserInfoDto hrUserInfoDto : hrUserInfoDtoList) {
            long count = sysUserList.stream().filter(item -> item.getUserCode().equals(hrUserInfoDto.getJobNum())).count();
            if (count <= 0) {
                if (hrUserInfoDto.getIsdel() == 0) {
                    SysUser sysUser = new SysUser();
                    sysUser.setUserCode(hrUserInfoDto.getJobNum());
                    sysUser.setUserName(hrUserInfoDto.getJobNum());
                    sysUser.setNickName(hrUserInfoDto.getRealName());
                    sysUser.setMobile(hrUserInfoDto.getMobilePhone());
                    sysUser.setEmail(hrUserInfoDto.getEmail());
                    sysUser.setOrganizationId(user.getOrganizationId());
                    securityFeignApi.add(sysUser);
                }
            } else {
                SysUser sysUser = sysUserList.stream().filter(item -> item.getUserCode().equals(hrUserInfoDto.getJobNum())).findFirst().get();
                sysUser.setUserCode(hrUserInfoDto.getJobNum());
                sysUser.setUserName(hrUserInfoDto.getJobNum());
                sysUser.setNickName(hrUserInfoDto.getRealName());
                sysUser.setMobile(hrUserInfoDto.getMobilePhone());
                sysUser.setEmail(hrUserInfoDto.getEmail());
                if (StringUtils.isNotEmpty(hrUserInfoDto.getLeavedate())) {
                    sysUser.setStatus((byte) 0);
                }
                securityFeignApi.update(sysUser);
            }

            HrUserInfo hrUserInfo = new HrUserInfo();
            BeanUtils.autoFillEqFields(hrUserInfoDto, hrUserInfo);
            hrUserInfo.setDataStatus(1);
            hrUserInfo.setSyncTime(new Date());
        }

        if (!hrUserInfoList.isEmpty()) {
            hrUserInfoMapper.batchUpdate(hrUserInfoList);
        }

        return hrUserInfoDtoList.size() == 0 ? 1 : hrUserInfoDtoList.size();
    }

}
