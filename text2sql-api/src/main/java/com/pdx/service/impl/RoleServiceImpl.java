package com.pdx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdx.model.entity.Role;
import com.pdx.mapper.RoleMapper;
import com.pdx.model.vo.QueryRoleVo;
import com.pdx.model.vo.UpdateRoleVo;
import com.pdx.response.Result;
import com.pdx.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 派同学
 * @since 2023-07-24
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    /**
     * 条件查询
     * @param vo
     * @return
     */
    @Override
    public Result<?> pages(QueryRoleVo vo) {

        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        Page<Role> page = new Page<>(vo.getCurrent(), vo.getPageSize());
        // 拼接查询条件
        if (StringUtils.isNotEmpty(vo.getStartTime())) {
            wrapper.ge("create_time", vo.getStartTime());
        }
        if (StringUtils.isNotEmpty(vo.getEndTime())) {
            wrapper.le("create_time", vo.getEndTime());
        }
        if (null != vo.getStatus() && StringUtils.isNotEmpty(String.valueOf(vo.getStatus()))) {
            wrapper.eq("status", vo.getStatus());
        }
        Page<Role> rolePage = page(page, wrapper);
        long total = rolePage.getTotal();
        List<Role> records = rolePage.getRecords();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", total);
        resultMap.put("roles", records);
        return Result.success(resultMap);
    }

    /**
     * 添加Role
     * @param role
     * @return
     */
    @Override
    public Result<?> insertRole(Role role) {
        role.setId(UUID.randomUUID().toString());
        role.setRoleCode(role.getRoleCode().toUpperCase());
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        boolean result = save(role);
        return result ? Result.success() : Result.fail();
    }

    /**
     * 修改Role Info
     * @param vo
     * @return
     */
    @Override
    public Result<?> updateRole(UpdateRoleVo vo) {

        UpdateWrapper<Role> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", vo.getId());

        if (StringUtils.isNotEmpty(vo.getRemark())) {
            wrapper.set("remark", vo.getRemark());
        }

        if (StringUtils.isNotEmpty(vo.getRoleName())) {
            wrapper.set("role_name", vo.getRoleName());
        }

        boolean result = update(wrapper);
        return result ? Result.success() : Result.fail();
    }

    /**
     * 获取角色信息
     * @param id
     * @return
     */
    @Override
    public Result<?> getRoleInfo(String id) {
        Role role = getById(id);
        return Result.success(role);
    }

    @Override
    public Result<?> changeStatus(String roleId) {
        Role role = getById(roleId);
        role.setStatus(!role.isStatus());
        boolean result = updateById(role);
        return result ? Result.success() : Result.fail();
    }
}
