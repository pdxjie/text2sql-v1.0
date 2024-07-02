package com.pdx.utils;

import com.pdx.model.constants.BasicConstants;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/*
 * @Author 派同学
 * @Description 获取个人凭证信息
 * @Date 2023/8/9
 **/
@Component
public class SecurityUtil {

    @Resource
    private Ip2regionSearcher ip2regionSearcher;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 获取个人凭证信息
     * @return
     */
    public String getPrincipal () {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String accessToken = request.getHeader(BasicConstants.TOKEN);
        return accessToken;
    }

    public String getUserId() {
        String assessToken = getPrincipal();
        return jwtUtil.getUserIdByJwtToken(assessToken);
    }

    /**
     * 获取用户所在地
     * @return
     */
    public String getAddress () {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ipAddress = IPUtil.getIpAddress(request);
        String address = ip2regionSearcher.getAddress(ipAddress);
        return address;
    }
}
