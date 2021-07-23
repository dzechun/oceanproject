package com.fantechs.provider.smt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.smt.SmtSolderPasteDto;
import com.fantechs.common.base.general.dto.smt.SmtSolderPasterConfig;
import com.fantechs.common.base.general.entity.smt.SmtSolderPaste;
import com.fantechs.common.base.general.entity.smt.history.SmtHtSolderPaste;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.smt.mapper.SmtHtSolderPasteMapper;
import com.fantechs.provider.smt.mapper.SmtSolderPasteMapper;
import com.fantechs.provider.smt.service.SmtSolderPasteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Mr.Lei on 2021/07/22.
 */
@Service
public class SmtSolderPasteServiceImpl extends BaseService<SmtSolderPaste> implements SmtSolderPasteService {

    @Resource
    private SmtSolderPasteMapper smtSolderPasteMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SmtHtSolderPasteMapper smtHtSolderPasteMapper;

    //存入redis前缀
    private static String DEFAULT_NAME="SOLDER:";

    @Override
    public List<SmtSolderPasteDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        List<SmtSolderPasteDto> list = smtSolderPasteMapper.findList(map);
        for (SmtSolderPasteDto smtSolderPasteDto : list) {
            if (redisUtil.hasKey(DEFAULT_NAME+smtSolderPasteDto.getSolderPasteId())){
                SmtSolderPasterConfig smtSolderPasterConfig = new SmtSolderPasterConfig();
                BeanUtils.mapToObject((Map<String, Object>) redisUtil.get(DEFAULT_NAME+smtSolderPasteDto.getSolderPasteId()),smtSolderPasterConfig);
                smtSolderPasteDto.setSmtSolderPasterConfig(smtSolderPasterConfig);
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public SmtSolderPasteDto scanSolder(String barCode,Byte solderPasteStatus,Integer PASS) {
        //查询锡膏条码是否存在库存明细
        String productionBatchCode = smtSolderPasteMapper.findInvDet(barCode);
        if(StringUtils.isEmpty(productionBatchCode)){
            throw new BizErrorException("条码错误");
        }
        //用批号匹配是否存在
        Example example = new Example(SmtSolderPaste.class);
        example.createCriteria().andEqualTo("batchCode",productionBatchCode);
        List<SmtSolderPaste> smtSolderPastes = smtSolderPasteMapper.selectByExample(example);
        if(smtSolderPastes.size()<1){
            throw new BizErrorException("暂无该条码信息");
        }else if(smtSolderPastes.size()>1){
            throw new BizErrorException("批次错误！请检查批次");
        }
        SmtSolderPaste smtSolderPaste = smtSolderPastes.get(0);
        if (redisUtil.hasKey(DEFAULT_NAME+smtSolderPaste.getSolderPasteId())){
            SmtSolderPasterConfig smtSolderPasterConfig = new SmtSolderPasterConfig();
            BeanUtils.mapToObject((Map<String, Object>) redisUtil.get(DEFAULT_NAME+smtSolderPaste.getSolderPasteId()),smtSolderPasterConfig);
            smtSolderPaste.setSmtSolderPasterConfig(smtSolderPasterConfig);
        }else {
            throw new BizErrorException("锡膏配置获取失败");
        }
//        smtSolderPaste.setSmtSolderPasterConfig((SmtSolderPasterConfig) redisUtil.get(DEFAULT_NAME+smtSolderPaste.getSolderPasteId().toString()));
        if(smtSolderPaste.getSolderPasteStatus()==7){
            throw new BizErrorException("锡膏已报废,无法执行");
        }
        smtSolderPaste = this.checkStatus(smtSolderPaste,solderPasteStatus,PASS);
        if(StringUtils.isEmpty(smtSolderPaste.getMessage())){
            smtSolderPasteMapper.updateByPrimaryKeySelective(smtSolderPaste);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("solderPasteId",smtSolderPaste.getSolderPasteId());
        SmtSolderPasteDto smtSolderPasteDto = smtSolderPasteMapper.findList(map).get(0);
        smtSolderPasteDto.setMessage(smtSolderPasteDto.getMessage());
        smtSolderPasteDto.setExecuteStatus(smtSolderPaste.getExecuteStatus());
        smtSolderPasteDto.setIsDate(smtSolderPaste.getIsDate());

        SmtHtSolderPaste smtHtSolderPaste = new SmtHtSolderPaste();
        BeanUtil.copyProperties(smtSolderPaste,smtHtSolderPaste);
        smtHtSolderPasteMapper.insertSelective(smtHtSolderPaste);
        return smtSolderPasteDto;
    }

    @Override
    public List<SmtSolderPasteDto> findHtList(Map<String, Object> map) {
        SysUser sysUser  = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return smtHtSolderPasteMapper.findHtList(map);
    }

    private SmtSolderPaste checkStatus(SmtSolderPaste smtSolderPaste,Byte solderPasteStatus,Integer PASS){
        //ps: pass==1强制执行 PASS==2过期执行
        if(PASS==0 || PASS==2){
            Map<String,Object> map = this.getDatePoor(smtSolderPaste.getSpStatusUpdateTime(),new Date());
            if(PASS==0){
                //判断锡膏是否过期
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                if(!sf.format(smtSolderPaste.getProductionDate()).equals(sf.format(new Date()))){
                    if(smtSolderPaste.getSmtSolderPasterConfig().getProductionDateStatus()==1){
                        smtSolderPaste.setMessage("锡膏已过期，已强制停止执行");
                        smtSolderPaste.setExecuteStatus(1);
                        smtSolderPaste.setIsDate(1);
                    }else{
                        smtSolderPaste.setMessage("锡膏已过期，是否继续执行");
                        smtSolderPaste.setExecuteStatus(2);
                        smtSolderPaste.setIsDate(1);
                    }
                }
            }
            switch (solderPasteStatus){
                case 1:
                    //入冰窟
                    if(smtSolderPaste.getSolderPasteStatus()>solderPasteStatus){
                        throw new BizErrorException("锡膏已经"+this.StatusToName(solderPasteStatus));
                    }
                    break;
                case 2:
                    //回温
                    //ps：回冰后可以再次回温
                    if(smtSolderPaste.getSolderPasteStatus()!=6 && smtSolderPaste.getSolderPasteStatus()>solderPasteStatus){
                        throw new BizErrorException("锡膏已经"+this.StatusToName(solderPasteStatus));
                    }
                    break;
                case 3:
                    //搅拌
                    //判断回温是否达到要求时间
                    if(smtSolderPaste.getSolderPasteStatus()!=2){
                        throw new BizErrorException("锡膏未回温，无法执行");
                    }
                    if(smtSolderPaste.getTBackTime().compareTo((BigDecimal) map.get("min"))==1){
                        if(smtSolderPaste.getSmtSolderPasterConfig().getTBackTimeStatus()==1){
                            //强制执行
                            smtSolderPaste.setMessage("该锡膏超出设定时间，已强制停止执行");
                            smtSolderPaste.setExecuteStatus(1);
                            smtSolderPaste.setIsDate(0);
                        }else{
                            smtSolderPaste.setMessage("该锡膏超出设定时间，是否继续执行");
                            smtSolderPaste.setExecuteStatus(2);
                            smtSolderPaste.setIsDate(0);
                        }

                    }else  if(smtSolderPaste.getTBackTime().compareTo((BigDecimal) map.get("min"))==-1){
                        if(smtSolderPaste.getSmtSolderPasterConfig().getTBackTimeStatus()==1){
                            smtSolderPaste.setMessage("该锡膏未到达设定时间,已强制停止执行");
                            smtSolderPaste.setExecuteStatus(1);
                            smtSolderPaste.setIsDate(0);
                        }else{
                            smtSolderPaste.setMessage("该锡膏未到达设定时间，是否继续执行");
                            smtSolderPaste.setExecuteStatus(2);
                            smtSolderPaste.setIsDate(0);
                        }
                    }
                    break;
                case 4:
                    //开封
                    //ps:判断搅拌时间是否达到设定时间
                    if(smtSolderPaste.getStirTime().compareTo((BigDecimal) map.get("min"))==1){
                        //判断是否超过未开封日期
                        if(smtSolderPaste.getNotOpenTimeLimit().compareTo((BigDecimal) map.get("hour"))==1){
                            if(smtSolderPaste.getSmtSolderPasterConfig().getStirTimeStatus()==0 ||smtSolderPaste.getSmtSolderPasterConfig().getNotOpenTimeLimitStatus()==0){
                                smtSolderPaste.setMessage("该锡膏超过搅拌时间及未开封时间，是否继续执行");
                                smtSolderPaste.setExecuteStatus(2);
                                smtSolderPaste.setIsDate(0);
                            }else{
                                smtSolderPaste.setMessage("该锡膏超出搅拌时间及未开封时间，已强制停止执行");
                                smtSolderPaste.setExecuteStatus(1);
                                smtSolderPaste.setIsDate(0);
                            }
                        }
                    }else if(smtSolderPaste.getStirTime().compareTo((BigDecimal) map.get("min"))==-1){
                        if(smtSolderPaste.getSmtSolderPasterConfig().getStirTimeStatus()==1){
                            smtSolderPaste.setMessage("该锡膏未到达设定的搅拌时间，已强制停止执行");
                            smtSolderPaste.setExecuteStatus(1);
                            smtSolderPaste.setIsDate(0);
                        }else{
                            smtSolderPaste.setMessage("该锡膏未到达设定的搅拌时间，是否继续执行");
                            smtSolderPaste.setExecuteStatus(2);
                            smtSolderPaste.setIsDate(0);
                        }
                    }
                    break;
                case 5:
                    //上料
                    //判断是否超过开封日期
                    if(smtSolderPaste.getOpenTimeLimit().compareTo((BigDecimal) map.get("min"))==1){
                        if(smtSolderPaste.getSmtSolderPasterConfig().getOpenTimeLimitStatus()==1){
                            smtSolderPaste.setMessage("该锡膏超过未开封时间，已强制停止执行");
                            smtSolderPaste.setExecuteStatus(1);
                            smtSolderPaste.setIsDate(0);
                        }else{
                            smtSolderPaste.setMessage("该锡膏超过未开封时间，是否继续执行");
                            smtSolderPaste.setExecuteStatus(2);
                            smtSolderPaste.setIsDate(0);
                        }
                    }
                    break;
                case 6:
                    //回冰
                    //ps：判断回冰次数
                    if(smtSolderPaste.getReturnIceTime()==smtSolderPaste.getCurrentReturnIceTime()){
                        if(smtSolderPaste.getSmtSolderPasterConfig().getReturnIceTimeStatus()==1){
                            smtSolderPaste.setMessage("该锡膏超过回冰次数，已强制停止执行");
                            smtSolderPaste.setExecuteStatus(1);
                            smtSolderPaste.setIsDate(0);
                        }else {
                            smtSolderPaste.setMessage("该锡膏超过回冰次数，是否继续执行");
                            smtSolderPaste.setExecuteStatus(2);
                            smtSolderPaste.setIsDate(0);
                        }
                    }
                    smtSolderPaste.setReturnIceTime(smtSolderPaste.getReturnIceTime()+1);
                    break;
                case 7:
                    //报废
                    redisUtil.del(smtSolderPaste.getSolderPasteId().toString());
                    break;
            }
        }
        smtSolderPaste.setSolderPasteStatus(solderPasteStatus);
        smtSolderPaste.setSpStatusUpdateTime(new Date());
        return smtSolderPaste;
    }

    /**
     * 状态名称
     * @param solderPasteStatus 状态编码
     * @return
     */
    private String StatusToName(Byte solderPasteStatus){
        switch (solderPasteStatus){
            case 1:
                //入冰窟
                return "入冰窟";
            case 2:
                //回温
                return "回温";
            case 3:
                //搅拌
                return "搅拌";
            case 4:
                //开封
                return "开封";
            case 5:
                //上料
                return "上料";
            case 6:
                //回冰
                return "回冰";
            case 7:
                //报废
                return "报废";
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(SmtSolderPaste record) {
        SysUser sysUser = currentUser();
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        int num = smtSolderPasteMapper.insertSelective(record);

        //SmtSolderPasterConfig smtSolderPasterConfig = record.getSmtSolderPasterConfig();
//        smtSolderPasterConfig.setSolderPasteId(record.getSolderPasteId());
//        //将锡膏执行状态存入redis
//        redisUtil.set(record.getSolderPasteId().toString(),smtSolderPasterConfig);
        return num;
    }

    @Override
    public int update(SmtSolderPaste entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());

        int num = smtSolderPasteMapper.updateByPrimaryKeySelective(entity);
        if(StringUtils.isEmpty(entity.getSmtSolderPasterConfig())){
            throw new BizErrorException("配置不能为空");
        }
        SmtSolderPasterConfig smtSolderPasterConfig = entity.getSmtSolderPasterConfig();
        smtSolderPasterConfig.setSolderPasteId(entity.getSolderPasteId());
        //将锡膏执行状态存入redis
        redisUtil.set(DEFAULT_NAME+entity.getSolderPasteId().toString(),smtSolderPasterConfig);
        SmtHtSolderPaste smtHtSolderPaste = new SmtHtSolderPaste();
        BeanUtil.copyProperties(entity,smtHtSolderPaste);
        smtHtSolderPasteMapper.insertSelective(smtHtSolderPaste);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            SmtSolderPaste smtSolderPaste = smtSolderPasteMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(smtSolderPaste)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }
            //删除锡膏执行状态
            redisUtil.del(DEFAULT_NAME+id);

            SmtHtSolderPaste smtHtSolderPaste = new SmtHtSolderPaste();
            BeanUtil.copyProperties(smtSolderPaste,smtHtSolderPaste);
            smtHtSolderPasteMapper.insertSelective(smtHtSolderPaste);
        }
        return super.batchDelete(ids);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

    public Map<String,Object> getDatePoor(Date endDate, Date nowDate) {

        Map<String,Object> map = new HashMap<>();
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        map.put("day",day);
        map.put("hour",hour);
        map.put("min",min);
        return map;
    }
}
