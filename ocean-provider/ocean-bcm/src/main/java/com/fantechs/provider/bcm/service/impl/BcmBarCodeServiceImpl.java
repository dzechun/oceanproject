package com.fantechs.provider.bcm.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.BcmBarCodeDto;
import com.fantechs.common.base.general.dto.bcm.BcmBarCodeWorkDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSpec;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.bcm.BcmBarCode;
import com.fantechs.common.base.general.entity.bcm.BcmBarCodeDet;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmBarCode;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;

import com.fantechs.provider.bcm.mapper.BcmBarCodeDetMapper;
import com.fantechs.provider.bcm.mapper.BcmBarCodeMapper;
import com.fantechs.provider.bcm.service.BcmBarCodeService;
import com.fantechs.provider.bcm.util.FTPUtil;
import com.fantechs.provider.bcm.util.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Mr.Lei
* @create 2020/12/22.
*/
@Service
public class BcmBarCodeServiceImpl  extends BaseService<BcmBarCode> implements BcmBarCodeService {

    @Resource
    private BcmBarCodeMapper bcmBarCodeMapper;
    @Autowired
    private FTPUtil ftpUtil;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private BcmBarCodeDetMapper bcmBarCodeDetMapper;
    @Resource
    private PMFeignApi pmFeignApi;

    @Value("${print.ip}")
    private String ip;
    @Value("${print.port}")
    private Integer port;

    @Override
    public List<BcmBarCodeDto> findList(SearchBcmBarCode searchBcmBarCode) {
        return bcmBarCodeMapper.findList(searchBcmBarCode);
    }

    /**
     * 获取条码解析码
     * @param searchBcmBarCode
     * @return
     */
    @Override
    public BcmBarCodeWorkDto work(SearchBcmBarCode searchBcmBarCode) {
        BcmBarCodeWorkDto bcmBarCodeWorkDto = bcmBarCodeMapper.sel_work_order(searchBcmBarCode);
        if(StringUtils.isEmpty(bcmBarCodeWorkDto)){
            throw new BizErrorException("获取工单信息绑定条码规则信息失败");
        }
        //生成规则
        SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec = new SearchSmtBarcodeRuleSpec();
        searchSmtBarcodeRuleSpec.setBarcodeRuleId(bcmBarCodeWorkDto.getBarcodeRuleId());
        List<SmtBarcodeRuleSpec> list = pmFeignApi.findSpec(searchSmtBarcodeRuleSpec).getData();
        Example example1 = new Example(BcmBarCode.class);
        example1.createCriteria().andEqualTo("workOrderId",bcmBarCodeWorkDto.getWorkOrderId());
        List<BcmBarCode> bcmBarCodes = bcmBarCodeMapper.selectByExample(example1);
        String maxCode = bcmBarCodeMapper.selMaxCode(bcmBarCodeWorkDto.getWorkOrderId());
        if(!StringUtils.isEmpty(maxCode)){
            maxCode = pmFeignApi.generateMaxCode(list, maxCode).getData();
        }
        String code = pmFeignApi.generateCode(list,maxCode,bcmBarCodeWorkDto.getMaterialCode()).getData();
        bcmBarCodeWorkDto.setBarcode(code);
        return bcmBarCodeWorkDto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void download(List<String> savePath, HttpServletResponse response) throws UnsupportedEncodingException {
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("FTP");
            ResponseEntity<List<SysSpecItem>> itemList= securityFeignApi.findSpecItemList(searchSysSpecItem);
            List<SysSpecItem> sysSpecItemList = itemList.getData();
            Map map = (Map) JSON.parse(sysSpecItemList.get(0).getParaValue());
//            List<InputStream> inputStreams = downloadFile(map,savePath);
//            List<String> path = new ArrayList<>();
//            for (String s : savePath) {
//                path.add(s.split("/")[1]);
//            }
//            String[] fileName = path.toArray(new String[path.size()]);
//            this.ftpUtil.downloadZipFiles(response,inputStreams,"打印文件",fileName);
            String[] filName = savePath.get(0).split("/");
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filName[1], "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            InputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = downloadFile(map,savePath).get(0);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("Download  successfully!");

            } catch (Exception e) {
                System.out.println("Download  failed!");

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    /**
     * 打印
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int print(Long workOrderId) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BcmBarCode.class);
        example.createCriteria().andEqualTo("workOrderId",workOrderId).andEqualTo("status",(byte)1);
        List<BcmBarCode> bcmBarCodes = bcmBarCodeMapper.selectByExample(example);
        if(StringUtils.isEmpty(bcmBarCodes)){
            throw new BizErrorException("工单没有生成可打印的条码");
        }
//        if(!SocketClient.isConnect){
//            throw new BizErrorException("连接打印服务失败");
//        }
        try {
            for (BcmBarCode bcmBarCode : bcmBarCodes) {
                Example example1 = new Example(BcmBarCodeDet.class);
                example1.createCriteria().andEqualTo("barCodeId",bcmBarCode.getBarCodeId());
                List<BcmBarCodeDet> bcmBarCodeDets = bcmBarCodeDetMapper.selectByExample(example1);
                for (BcmBarCodeDet bcmBarCodeDet : bcmBarCodeDets) {
                    //打印
                    SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
                    searchMesPmWorkOrder.setWorkOrderId(workOrderId);
                    List<MesPmWorkOrderDto> smtWorkOrderDto = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
                    if(StringUtils.isEmpty(smtWorkOrderDto)){
                        throw new BizErrorException("获取工单信息失败");
                    }
                    MesPmWorkOrderDto sdto = smtWorkOrderDto.get(0);
                    Map<String, Object> map = JSON.parseObject(JSON.toJSONString(sdto),Map.class);
                    map.put("QrCode",bcmBarCodeDet.getBarCodeContent());

                    //获取抽检员信息
                    new SocketClient(ip,port);
                    String json = JSON.toJSONString(map);
                    SocketClient.out(json);
                    SocketClient.closeSocket();
                }
                //更新已打印状态
                bcmBarCode.setStatus((byte)2);
                bcmBarCode.setModifiedTime(new Date());
                bcmBarCode.setModifiedUserId(currentUserInfo.getUserId());
                bcmBarCodeMapper.updateByPrimaryKeySelective(bcmBarCode);
            }
        }catch (Exception e){
            throw new BizErrorException("打印失败");
        }
        return 1;
    }

    /**
     * 入库条码校验
     * @param QrCode
     * @param workOrderId
     * @return
     */
    @Override
    public BcmBarCodeDet verifyQrCode(String QrCode, Long workOrderId) {
        Example example = new Example(BcmBarCode.class);
        example.createCriteria().andEqualTo("workOrderId",workOrderId).andEqualTo("status",(byte)2);
        List<BcmBarCode> list = bcmBarCodeMapper.selectByExample(example);
        if(StringUtils.isEmpty(list)){
            return null;
        }
        List<Long> ids = list.stream().map(BcmBarCode::getBarCodeId).collect(Collectors.toList());
        Example example1 =new Example(BcmBarCodeDet.class);
        example1.createCriteria().andIn("barCodeId",ids).andEqualTo("barCodeContent",QrCode).andEqualTo("status",(byte)1);
        List<BcmBarCodeDet> bcmBarCodeDets = bcmBarCodeDetMapper.selectByExample(example1);
        if(bcmBarCodeDets.size()>0){
            return bcmBarCodeDets.get(0);
        }
        return null;
    }

    /**
     * 生成条码
     * @param record
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int saveCode(BcmBarCodeWorkDto record) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //查询产生数量是否大于工单目标数量
        Example example = new Example(BcmBarCode.class);
        example.createCriteria().andEqualTo("workOrderId",record.getWorkOrderId());
        List<BcmBarCode> bcmBarCodes = bcmBarCodeMapper.selectByExample(example);
        Integer num = bcmBarCodes.stream().mapToInt(BcmBarCode::getPrintQuantity).sum();

        if(record.getWorkOrderQuantity().compareTo(BigDecimal.valueOf(num+record.getPrintQuantity()))==-1){
            throw new BizErrorException("产生数量不能大于工单数量");
        }
        record.setStatus((byte)1);
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());
        int nm = bcmBarCodeMapper.insertUseGeneratedKeys(record);

        for (Integer i = 0; i < record.getPrintQuantity(); i++) {
            BcmBarCodeDet bcmBarCodeDet = new BcmBarCodeDet();
            bcmBarCodeDet.setBarCodeId(record.getBarCodeId());
            SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec = new SearchSmtBarcodeRuleSpec();
            searchSmtBarcodeRuleSpec.setBarcodeRuleId(record.getBarcodeRuleId());
            List<SmtBarcodeRuleSpec> list = pmFeignApi.findSpec(searchSmtBarcodeRuleSpec).getData();
            String max = bcmBarCodeMapper.selMaxCode(record.getWorkOrderId());
            String maxCode = pmFeignApi.generateMaxCode(list, max).getData();
            //String code = BarcodeRuleUtils.analysisCode(list,maxCode,record.getMaterialCode());
            //生成流水号
            String code = pmFeignApi.generateCode(list,maxCode,record.getMaterialCode()).getData();
            bcmBarCodeDet.setBarCodeContent(code);
            bcmBarCodeDet.setCreateTime(new Date());
            bcmBarCodeDet.setCreateUserId(currentUserInfo.getUserId());
            bcmBarCodeDet.setModifiedTime(new Date());
            bcmBarCodeDet.setModifiedUserId(currentUserInfo.getUserId());
            bcmBarCodeDetMapper.insertSelective(bcmBarCodeDet);
        }

        return nm;
    }

    /**
     * 入库条码状态更改
     * @param bcmBarCodeDets
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateByContent(List<BcmBarCodeDet> bcmBarCodeDets) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int num = 0;
        for (BcmBarCodeDet bcmBarCodeDet : bcmBarCodeDets) {
            bcmBarCodeDet.setModifiedUserId(currentUserInfo.getUserId());
            bcmBarCodeDet.setModifiedTime(new Date());
            bcmBarCodeDet.setStatus((byte)2);
            num=+bcmBarCodeDetMapper.updateByPrimaryKeySelective(bcmBarCodeDet);
        }
        return num;
    }

    /**
     * 补打列表
     * @param workOrderId
     * @return
     */
    @Override
    public List<BcmBarCodeDto> reprintList(String workOrderId) {
        return bcmBarCodeMapper.reprintList(workOrderId);
    }

    /**
     * 补打打印
     * @param barCodeId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int reprint(List<String> barCodeId) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        try {
            for (String s : barCodeId) {
                BcmBarCode bcmBarCode = bcmBarCodeMapper.selectByPrimaryKey(Long.parseLong(s));
                SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
                searchMesPmWorkOrder.setWorkOrderId(bcmBarCode.getWorkOrderId());
                List<MesPmWorkOrderDto> smtWorkOrderDto = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData();
                if(StringUtils.isEmpty(smtWorkOrderDto)){
                    throw new BizErrorException("获取工单信息失败");
                }
                Example example1 = new Example(BcmBarCodeDet.class);
                example1.createCriteria().andEqualTo("barCodeId",bcmBarCode.getBarCodeId());
                List<BcmBarCodeDet> bcmBarCodeDets = bcmBarCodeDetMapper.selectByExample(example1);
                for (BcmBarCodeDet bcmBarCodeDet : bcmBarCodeDets) {
                    //打印
                    MesPmWorkOrderDto sdto = smtWorkOrderDto.get(0);
                    Map<String, Object> map = JSON.parseObject(JSON.toJSONString(sdto),Map.class);
                    map.put("QrCode",bcmBarCodeDet.getBarCodeContent());
                    //获取抽检员信息
                    new SocketClient("192.168.200.56",8098);
                    String json = JSON.toJSONString(map);
                    SocketClient.out(json);
                    SocketClient.closeSocket();
                }
            }
            return 1;
        }catch (Exception e){
            throw new BizErrorException("打印失败");
        }
    }

    /**
     * 下载FTP文件
     * @param map
     * @param savePath
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public List<InputStream> downloadFile(Map map, List<String> savePath) {
        boolean isLogin = false;
        List<InputStream> ins = new ArrayList<>();
        //上传FTP服务器
        for (String s : savePath) {
            try {
                String ip = map.get("ip").toString();
                Integer port = Integer.parseInt(map.get("port").toString());
                String username = map.get("username").toString();
                String password = map.get("password").toString();
                isLogin = this.ftpUtil.connectFTP(ip, port, username, password);
                if (isLogin) {
                    String[] path = s.split("/");
                    InputStream in = this.ftpUtil.downFile(path[0], path[1]);
                    ins.add(in);
                }
            } catch (Exception e) {
                throw new BizErrorException(ErrorCodeEnum.valueOf("下载失败"));
            } finally {
                this.ftpUtil.loginOut();
            }
        }
        return ins;
    }
}
