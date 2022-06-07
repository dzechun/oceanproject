package com.fantechs.common.base.utils;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.TokenValidationFailedException;
import cz.mallat.uasparser.UserAgentInfo;
import org.apache.tomcat.util.buf.Utf8Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
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
    private static Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    private static RedisUtil redisUtil = (RedisUtil) SpringUtil.getBean("redisUtil");

    /**
     * 调用RedisAPI
     */
    public static int expire = 12 * 60 * 60;// 2h token过期时间
    public static long refresh_expire = 30 * 24 * 60 * 60;//30天
    public static int REPLACEMENT_PROTECTION_TIMEOUT = 30 * 60;//30分钟 token置换保护时间
    public static int REPLACEMENT_DELAY = 2 * 60;//2分钟 旧token延期失效时间
    public static String tokenPrefix = "token:";//统一加入 token前缀标识
    public static String refreshTokenPrefix = "refreshToken:";//统一加入 token前缀标识


    /***
     * @param agent Http头中的user-agent信息
     * @param user 用户信息
     * @return Token格式<br />
     * 	PC：“前缀PC-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
     *  <br/>
     *  MOBILE：“前缀MOBILE-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
     */
    public static String generateToken(String agent, SysUser user) {
        try {

            UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(
                    agent);
            StringBuilder sb = new StringBuilder();
            if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
                if (agent.contains("Apache-HttpClient")) {
                    sb.append("NET-");
                } else if (UserAgentUtil.CheckAgent(agent)) {
                    sb.append("MOBILE-");
                } else {
                    sb.append("PC-");
                }
            } else if (userAgentInfo.getDeviceType()
                    .equals("Personal computer")) {
                sb.append("PC-");
            } else
                sb.append("MOBILE-");
            sb.append(URLEncoder.encode(user.getUserCode(), "UTF-8") + "-");
            // 重写 Token 生成策略
            SaStrategy.me.createToken = (loginId, loginType) -> {
                //客户端 + 随机字符串
                return sb + UUIDUtils.getUUID();
            };
            StpUtil.login(user.getUserId());

            return StpUtil.getTokenValue();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取token
     *
     * @param agent Http头中的user-agent信息
     * @param user  用户信息
     * @return Token格式<br />
     * PC： 前缀
     */
    public static String generateRefreshToken(String agent, SysUser user, String refreshTokenIp) {
        try {
            UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(
                    agent);
            StringBuilder sb = new StringBuilder();
            if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
                if (agent.contains("Apache-HttpClient")) {
                    sb.append("NET-");
                } else if (UserAgentUtil.CheckAgent(agent)) {
                    sb.append("MOBILE-");
                } else {
                    sb.append("PC-");
                }
            } else if (userAgentInfo.getDeviceType()
                    .equals("Personal computer")) {
                sb.append("PC-");
            } else
                sb.append("MOBILE-");
            sb.append(URLEncoder.encode(user.getUserCode(), "UTF-8") + "-");
            sb.append(UUIDUtils.getRawUUID());
            return sb.toString();
        } catch (IOException e) {
            logger.error("");
            e.printStackTrace();
            return null;
        }
    }

    public static void saveToken(String token, SysUser user) {
        redisUtil.set(token, user, expire);
    }

    public static void saveRefreshToken(String refreshToken, SysUser user) {
        redisUtil.set(refreshToken, user, refresh_expire);
    }

    public static void save(String token, SysUser user) {
        if (token.startsWith(refreshTokenPrefix)) {
            // 刷新token
            redisUtil.set(token, user, refresh_expire);
        } else if (token.startsWith(tokenPrefix + "PC-")) {
            // token
            redisUtil.set(token, user, expire);
        } else {
            // 手机认证信息永不失效
            redisUtil.set(token, user);
        }
    }

    public static SysUser load(String token) {
        Object o = redisUtil.get(token);
        return JSONObject.parseObject(JSONObject.toJSONString(o), SysUser.class);
    }

    public static void delete(String token) {
        if (redisUtil.hasKey(token))
            redisUtil.del(token);
    }


    /**
     * 置换token
     *
     * @param agent
     * @param refreshToken
     * @return
     * @throws TokenValidationFailedException
     */
    public static String replaceToken(String agent, String oldToken, String refreshToken)
            throws TokenValidationFailedException {

        boolean validate = validate(agent, refreshToken);
        if (!validate || !refreshToken.startsWith(refreshTokenPrefix)) {
            throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039);
        }
        String[] refreshTokenDetails = refreshToken.split("-");

        //---判断oldToken是否还存在，如果存在判断是否在置换保护期内，如果在不允许置换
        validate = validate(agent, oldToken);
        String[] oldTokenDetails;
        if (validate) {
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
            if (!refreshTokenDetails[2].equals(oldTokenDetails[2])) {
                throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039);
            }
        }
        //---

        // 置换token
        String newToken;
        long ttl = redisUtil.getExpire(refreshToken);// token有效期（剩余秒数 ）
        if (ttl > 0 || ttl == -1) {// 兼容手机与PC端的token在有效期
            SysUser user = load(refreshToken);
            newToken = generateToken(agent, user);
            save(newToken, user);// 缓存新token
            if (validate(agent, oldToken))
                redisUtil.set(oldToken, JSON.toJSONString(user), REPLACEMENT_DELAY);// 2分钟后旧token过期，注意手机端由永久有效变为2分钟（REPLACEMENT_DELAY默认值）后失效
        } else {// 其它未考虑情况，不予置换
            throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039);
        }
        return newToken;
    }

    /**
     * 续期token（刷新token）
     *
     * @param agent
     * @param token
     * @param refreshToken
     * @return
     * @throws TokenValidationFailedException
     */
    public static String renewTimeoutToken(String agent, String token, String refreshToken) throws TokenValidationFailedException {
        // 验证当前refreshToken是否有效,如果有效，则赋予信息的token给前端
        boolean validate = validate(agent, refreshToken);
        if (!validate) {
            throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039, "刷新token无效");
        }

        // 判断token是否还在有效期内
        boolean isLogin = StpUtil.isLogin();
        if (isLogin) {
            // Token 指向的 LoginId 异常时，不进行任何操作
            SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
            if (ObjectUtil.isNull(saTokenInfo)) {
                throw new TokenValidationFailedException(ErrorCodeEnum.UAC10011039, "登录信息无效");
            }

            // token还在有效期内，对token直接续期,把时间续期为12小时
            StpUtil.renewTimeout(expire - saTokenInfo.getTokenTimeout());
            return StpUtil.getTokenValue();
        } else {
            // token已过期,利用刷新token参数来重新登陆
            SysUser user = load(refreshToken);
            // 登录并且返回token
           String newToken = generateToken(agent, user);
            //保存到redis
            TokenUtil.saveToken(newToken, user);
            return newToken;
        }
    }


    /**
     * 对指定 Token 的 timeout 值进行续期
     *
     * @param tokenValue 指定token
     * @param timeout    要修改成为的有效时间 (单位: 秒)
     */
    public void renewTimeout(String tokenValue, long timeout) {


    }


    /**
     * 验证token是否有效
     *
     * @param agent
     * @param token
     * @return
     */
    public static boolean validate(String agent, String token) {
        try {
            // 先找Redis的token
            if (!redisUtil.hasKey(token)) {
                logger.info("当前刷新token不存在，验证失败！");
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("校验token异常:", e);
            return false;
        }
    }

    /**
     * 验证token是否有效
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        // 先找Redis的token
        if (!redisUtil.hasKey(token)) {
            return false;
        }
        try {
            Boolean isLogin = StpUtil.isLogin();
            logger.info("当前登录状态：{}", isLogin ? "已登录" : "未登录");
            return isLogin;
        } catch (Exception e) {
            logger.error("校验token异常:", e);
            return false;
        }
    }

    /**
     * 从请求中清除token
     *
     * @param request
     */
    public static void clearTokenByRequest(HttpServletRequest request) {
        String token = request.getHeader("token");
        String refreshToken = request.getHeader("refreshToken");
        if (StringUtils.isNotEmpty(token)) {
            redisUtil.del(token);
        }
        if (StringUtils.isNotEmpty(refreshToken)) {
            redisUtil.del(refreshToken);
        }
    }

    /**
     * 获取用户真实ip地址
     *
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
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
