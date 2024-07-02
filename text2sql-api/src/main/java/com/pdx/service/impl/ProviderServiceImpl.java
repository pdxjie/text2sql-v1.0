package com.pdx.service.impl;

import com.pdx.model.constants.BasicConstants;
import com.pdx.service.ProviderService;
import com.pdx.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;

/*
 * @Author 派同学
 * @Description
 * @Date 2023/7/26
 **/
@Service
public class ProviderServiceImpl implements ProviderService {

    @Resource
    private JwtUtil jwtUtil;

    // 获取当前用户ID
    @Override
    public String currentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String authToken = attributes.getRequest().getHeader(BasicConstants.TOKEN);
        return jwtUtil.getUserIdByJwtToken(authToken);
    }
}
