package com.fantechs.provider.esop.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDetDto;
import com.fantechs.common.base.general.dto.esop.EsopWiReleaseDto;
import com.fantechs.common.base.general.entity.esop.EsopWiRelease;
import com.fantechs.common.base.general.entity.esop.EsopWiReleaseDet;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWiRelease;
import com.fantechs.common.base.general.entity.esop.history.EsopHtWiReleaseDet;
import com.fantechs.common.base.general.entity.esop.search.SearchEsopWiRelease;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.esop.mapper.EsopHtWiReleaseDetMapper;
import com.fantechs.provider.esop.mapper.EsopHtWiReleaseMapper;
import com.fantechs.provider.esop.mapper.EsopWiReleaseDetMapper;
import com.fantechs.provider.esop.mapper.EsopWiReleaseMapper;
import com.fantechs.provider.esop.service.EsopIssueService;
import com.fantechs.provider.esop.service.EsopWiReleaseService;
import com.fantechs.provider.esop.service.socket.SocketService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/07/08.
 */
@Service
public class EsopWiReleaseServiceImpl extends BaseService<EsopWiRelease> implements EsopWiReleaseService {

    @Resource
    private EsopWiReleaseMapper esopWiReleaseMapper;
    @Resource
    private EsopHtWiReleaseMapper esopHtWiReleaseMapper;
    @Resource
    private EsopWiReleaseDetMapper esopWiReleaseDetMapper;
    @Resource
    private EsopHtWiReleaseDetMapper esopHtWiReleaseDetMapper;
    @Resource
    private SocketService socketService;
    @Resource
    private AuthFeignApi securityFeignApi;
    @Resource
    private EsopIssueService esopIssueService;

    @Override
    public List<EsopWiReleaseDto> findList(SearchEsopWiRelease searchEsopWiRelease) {
        if(StringUtils.isEmpty(searchEsopWiRelease.getOrgId())){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            searchEsopWiRelease.setOrgId(user.getOrganizationId());
        }
        return esopWiReleaseMapper.findList(searchEsopWiRelease);
    }


    @Override
    public int save(EsopWiReleaseDto EsopWiReleaseDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(EsopWiReleaseDto.getWiReleaseCode())) throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"添加失败，发布编码不能为空");
        if(StringUtils.isEmpty(EsopWiReleaseDto.getProLineId())) throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"添加失败，产线id不能为空");
        Example example1 = new Example(EsopWiRelease.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("orgId", user.getOrganizationId());
        criteria1.andEqualTo("wiReleaseCode",EsopWiReleaseDto.getWiReleaseCode());
        EsopWiRelease wiRelease = esopWiReleaseMapper.selectOneByExample(example1);
        if(StringUtils.isNotEmpty(wiRelease)) throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"添加失败，已存在发布编码");
        example1.clear();


        //验证子表编码不能重复
        HashSet set = new HashSet();
        for(EsopWiReleaseDetDto dto :EsopWiReleaseDto.getEsopWiReleaseDetDtos()){
            set.add(dto.getWiReleaseDetSeqNum());
        }
        if(set.size() != EsopWiReleaseDto.getEsopWiReleaseDetDtos().size()) throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "添加失败，wi序号重复");

        EsopWiRelease EsopWiRelease = new EsopWiRelease();
        BeanUtils.autoFillEqFields(EsopWiReleaseDto, EsopWiRelease);
        EsopWiRelease.setCreateUserId(user.getUserId());
        EsopWiRelease.setCreateTime(new Date());
        EsopWiRelease.setModifiedUserId(user.getUserId());
        EsopWiRelease.setModifiedTime(new Date());
        EsopWiRelease.setStatus((byte)1);
        EsopWiRelease.setReleaseStatus((byte)1);
        EsopWiRelease.setOrgId(user.getOrganizationId());
        int i = esopWiReleaseMapper.insertSelective(EsopWiRelease);
        List<EsopWiReleaseDet> dets = saveDet(EsopWiReleaseDto,user,EsopWiRelease.getWiReleaseId());

        //添加履历表
        EsopHtWiRelease EsopHtWiRelease = new EsopHtWiRelease();
        BeanUtils.autoFillEqFields(EsopWiRelease, EsopHtWiRelease);
        esopHtWiReleaseMapper.insertSelective(EsopHtWiRelease);

        List<EsopHtWiReleaseDet> htDets = new ArrayList<>();
        if(StringUtils.isNotEmpty(dets)) {
            for (EsopWiReleaseDet EsopWiReleaseDet :  dets) {
                EsopHtWiReleaseDet htWiReleaseDet = new EsopHtWiReleaseDet();
                BeanUtils.autoFillEqFields(EsopWiReleaseDet, htWiReleaseDet);
                htDets.add(htWiReleaseDet);
            }
        }
        esopHtWiReleaseDetMapper.insertList(htDets);
        return i;
    }


    @Override
    public int update(EsopWiReleaseDto EsopWiReleaseDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(EsopWiReleaseDto.getWiReleaseId()))
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"id不能为空");
        EsopWiReleaseDto.setReleaseStatus((byte)1);
        esopWiReleaseMapper.updateByPrimaryKey(EsopWiReleaseDto);

        Example example = new Example(EsopWiRelease.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("wiReleaseId", EsopWiReleaseDto.getWiReleaseId());
        EsopWiRelease EsopWiRelease = esopWiReleaseMapper.selectOneByExample(example);
        example.clear();

        Example detExample = new Example(EsopWiReleaseDet.class);
        Example.Criteria detCriteria = detExample.createCriteria();
        detCriteria.andEqualTo("wiReleaseId", EsopWiReleaseDto.getWiReleaseId());
        esopWiReleaseDetMapper.deleteByExample(detExample);
        detExample.clear();


        //保存履历表
        EsopHtWiRelease EsopHtWiRelease = new EsopHtWiRelease();
        BeanUtils.autoFillEqFields(EsopWiRelease,EsopHtWiRelease);
        int i = esopHtWiReleaseMapper.insertSelective(EsopHtWiRelease);

        saveDet(EsopWiReleaseDto,user,EsopWiRelease.getWiReleaseId());
        return i;
    }

    @SneakyThrows
    @Override
    public int censor(EsopWiRelease EsopWiRelease) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(EsopWiRelease.getWiReleaseId()))
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"发布id不能为空");
        if(StringUtils.isEmpty(EsopWiRelease.getProLineId()))
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"产线id不能为空");

        //关闭该产线原有的wi原有的wi
        Example example = new Example(EsopWiRelease.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId())
                .andEqualTo("proLineId", EsopWiRelease.getProLineId());
        List<EsopWiRelease> EsopWiReleases = esopWiReleaseMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(EsopWiReleases)) {
            for(EsopWiRelease wi : EsopWiReleases) {
                wi.setReleaseStatus((byte)1);
                esopWiReleaseMapper.updateByPrimaryKeySelective(wi);
            }
        }
        example.clear();

        EsopWiRelease.setStatus((byte)1);
        EsopWiRelease.setReleaseStatus((byte)2);
        int i = esopWiReleaseMapper.updateByPrimaryKeySelective(EsopWiRelease);
        socketService.BatchInstructions(EsopWiRelease.getProLineId(),"1202","/#/YunZhiESOP?mac=", "1");
        return i;
    }


    public  List<EsopWiReleaseDet> saveDet(EsopWiReleaseDto EsopWiReleaseDto ,SysUser user,Long id){
        List<EsopWiReleaseDet> dets = new ArrayList<>();
        if(StringUtils.isNotEmpty(EsopWiReleaseDto.getEsopWiReleaseDetDtos())) {
            for (EsopWiReleaseDetDto EsopWiReleaseDetDto :  EsopWiReleaseDto.getEsopWiReleaseDetDtos()) {
                EsopWiReleaseDet wiReleaseDet = new EsopWiReleaseDet();
                BeanUtils.autoFillEqFields(EsopWiReleaseDetDto, wiReleaseDet);
                wiReleaseDet.setCreateUserId(user.getUserId());
                wiReleaseDet.setCreateTime(new Date());
                wiReleaseDet.setModifiedUserId(user.getUserId());
                wiReleaseDet.setModifiedTime(new Date());
                wiReleaseDet.setStatus(StringUtils.isEmpty(EsopWiReleaseDto.getStatus())?1: EsopWiReleaseDto.getStatus());
                wiReleaseDet.setOrgId(user.getOrganizationId());
                wiReleaseDet.setWiReleaseId(id);
                dets.add(wiReleaseDet);
            }
        }
        esopWiReleaseDetMapper.insertList(dets);
        return  dets;
    }


    @Override
    public int batchDelete(String ids) {
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            Example example = new Example(EsopWiReleaseDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("wiReleaseId", id);
            esopWiReleaseDetMapper.deleteByExample(example);
            example.clear();
        }
        return esopWiReleaseMapper.deleteByIds(ids);
    }


}
