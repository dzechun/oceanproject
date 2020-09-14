package com.fantechs.common.base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.User;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import cz.mallat.uasparser.UserAgentInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: bingo.ren
 * @Date: 2020/6/20 10:05
 * @Description: token操作
 * @Version: 1.0
 */

public class TokenUtil {
    private static RedisUtil redisUtil=(RedisUtil)SpringUtil.getBean("redisUtil");

    /**
     * 调用RedisAPI
     */
    public static int expire = 2*60*60;// 2h token过期时间
    public static long refresh_expire = 30*24*60*60;//30天
    public static int REPLACEMENT_PROTECTION_TIMEOUT = 30*60;//30分钟 token置换保护时间
    public static int REPLACEMENT_DELAY=2*60;//2分钟 旧token延期失效时间
    public static String tokenPrefix = "token:";//统一加入 token前缀标识
    public static String refreshTokenPrefix = "refreshToken:";//统一加入 token前缀标识


    /***
     * @param agent Http头中的user-agent信息
     * @param user 用户信息
     * @return Token格式<br/>
     * 	PC：“前缀PC-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
     *  <br/>
     *  MOBILE：“前缀MOBILE-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
     */
    public static String generateToken(String agent, User user, String refreshTokenIp) {
        // TODO Auto-generated method stub
        try {
            UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(
                    agent);
            StringBuilder sb = new StringBuilder();
            if(StringUtils.isEmpty(refreshTokenIp)){
                sb.append(tokenPrefix);//统一前缀
            }else{
                sb.append(refreshTokenPrefix);//统一前缀
            }
            if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
                if (UserAgentUtil.CheckAgent(agent)) {
                    sb.append("MOBILE-");
                } else {
                    sb.append("PC-");
                }
            } else if (userAgentInfo.getDeviceType()
                    .equals("Personal computer")) {
                sb.append("PC-");
            } else
                sb.append("MOBILE-");
            sb.append(user.getCode() + "-");
            sb.append(MD5.getMd5(user.getId()+"",32) + "-");//加密用户ID
            if(StringUtils.isNotEmpty(refreshTokenIp)){
                sb.append(MD5.getMd5(refreshTokenIp,16) + "-");
            }else{
                sb.append(user.getId() + "-");
            }
            sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + "-");
            sb.append(MD5.getMd5(agent, 6));// 识别客户端的简化实现——6位MD5码

            return sb.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void save(String token, User user) {
        if(token.startsWith(refreshTokenPrefix)){
            redisUtil.set(token, user, refresh_expire);
        }else if (token.startsWith(tokenPrefix+"PC-")) {
            redisUtil.set(token, user, expire);
        }
        else {
            redisUtil.set(token, user);// 手机认证信息永不失效
        }
    }

    public static User load(String token) {
        Object o = redisUtil.get(token);
        return JSONObject.parseObject(JSONObject.toJSONString(o), User.class);
    }

    public static void delete(String token) {
        if (redisUtil.hasKey(token))
            redisUtil.del(token);
    }


    /**
     * 置换token
     * @param agent
     * @param refreshToken
     * @return
     * @throws TokenValidationFailedException
     */
    public static String replaceToken(String agent, String oldToken,String refreshToken)
            throws TokenValidationFailedException {

        boolean validate = validate(agent, refreshToken);
        if(!validate || !refreshToken.startsWith(refreshTokenPrefix)){
            throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039);
        }
        String[] refreshTokenDetails=refreshToken.split("-");

        //---判断oldToken是否还存在，如果存在判断是否在置换保护期内，如果在不允许置换
        validate=validate(agent,oldToken);
        String[] oldTokenDetails;
        if(validate){
            Date TokenGenTime;// oldToken生成时间
            try {
                oldTokenDetails = oldToken.split("-");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                TokenGenTime = formatter.parse(oldTokenDetails[4]);
            } catch (ParseException e) {
                throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039);
            }
            long passed = Calendar.getInstance().getTimeInMillis()
                    - TokenGenTime.getTime();// token已产生时间
            if (passed < REPLACEMENT_PROTECTION_TIMEOUT * 1000) {// 置换保护期内
                throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039);
            }

            //验证原token是否对应refreshToken
            if (!refreshTokenDetails[2].equals(oldTokenDetails[2])){
                throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039);
            }
        }
        //---

        // 置换token
        String newToken;
        long ttl = redisUtil.getExpire(refreshToken);// token有效期（剩余秒数 ）
        if (ttl > 0 || ttl == -1) {// 兼容手机与PC端的token在有效期
            User user = load(refreshToken);
            newToken = generateToken(agent, user,null);
            save(newToken, user);// 缓存新token
            if(validate(agent,oldToken))
                redisUtil.set(oldToken, JSON.toJSONString(user),REPLACEMENT_DELAY);// 2分钟后旧token过期，注意手机端由永久有效变为2分钟（REPLACEMENT_DELAY默认值）后失效
        } else {// 其它未考虑情况，不予置换
            throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039);
        }
        return newToken;
    }

    /**
     * 验证token是否有效
     * @param agent
     * @param token
     * @return
     */
    public static boolean validate(String agent, String token) {
        if (!redisUtil.hasKey(token)) {// token不存在
            return false;
        }
        try {
            Date TokenGenTime;// token生成时间
            String agentMD5;
            String[] tokenDetails = token.split("-");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            TokenGenTime = formatter.parse(tokenDetails[4]);
            long passed = Calendar.getInstance().getTimeInMillis()
                    - TokenGenTime.getTime();
            if(token.startsWith(tokenPrefix)){
                if(passed>expire*1000)
                    return false;
            }else{
                if(passed>refresh_expire*1000)
                    return false;
            }
            agentMD5 = tokenDetails[5];
            if(agent.startsWith("Postman") || MD5.getMd5(agent, 6).equals(agentMD5))
                return true;
        } catch (ParseException e) {
            return false;
        }
        return false;
    }

    /**
     * 从请求中清除token
     * @param request
     */
    public static void clearTokenByRequest(HttpServletRequest request){
        String token = request.getHeader("token");
        String refreshToken = request.getHeader("refreshToken");
        if (StringUtils.isNotEmpty(token)){
            redisUtil.del(token);
        }
        if(StringUtils.isNotEmpty(refreshToken)){
            redisUtil.del(refreshToken);
        }
    }

    /**
     * 获取用户真实ip地址
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
