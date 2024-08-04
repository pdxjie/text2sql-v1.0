package com.pdx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.pdx.config.DatabaseComponent;
import com.pdx.model.dto.ConfigInfoDto;
import com.pdx.model.dto.DatabaseTreeDto;
import com.pdx.model.entity.ConnConfig;
import com.pdx.mapper.ConnConfigMapper;
import com.pdx.model.entity.ConnGroup;
import com.pdx.model.entity.GroupConfig;
import com.pdx.model.enums.TirggerType;
import com.pdx.model.enums.TreeNodeType;
import com.pdx.model.vo.ConnVo;
import com.pdx.response.ResponseCode;
import com.pdx.response.Result;
import com.pdx.service.ConnConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdx.service.ConnGroupService;
import com.pdx.service.GroupConfigService;
import com.pdx.service.ProviderService;
import com.pdx.strategy.database.DatasourceCheckContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.pdx.model.constants.BasicConstants.DEFAULT_GROUP_ID;
import static com.pdx.model.constants.BasicConstants.ZERO_VALUE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 派同学
 * @since 2024-07-07
 */
@Service
public class ConnConfigServiceImpl extends ServiceImpl<ConnConfigMapper, ConnConfig> implements ConnConfigService {

    @Resource
    private ProviderService providerService;

    @Resource
    private DatabaseComponent databaseComponent;

    @Resource
    private GroupConfigService groupConfigService;

    @Resource
    private ConnGroupService connGroupService;

    /**
     * 检查连接
     *
     * @param connVo 连接信息
     * @return 结果
     */
    @Override
    public Result<?> checkConn(ConnVo connVo) {
        // 判断连接主体信息是否为空
        if (connVo == null || StringUtils.isEmpty(connVo.getConnHost()) || StringUtils.isEmpty(connVo.getConnPort()) || StringUtils.isEmpty(connVo.getConnUser()) || StringUtils.isEmpty(connVo.getConnPass())) {
            return Result.fail(ResponseCode.CONN_INFO_NOT_SUPPORT_EMPTY);
        }
        // 获取当前用户信息
        String userId = providerService.currentUserId();
        // 判断当前用户是否存在
        if (StringUtils.isEmpty(userId)) {
            return Result.fail(ResponseCode.NEED_LOGIN);
        }
        DatasourceCheckContext context = new DatasourceCheckContext();
        return context.checkDatasource(connVo, userId);
    }

    /**
     * 关闭连接
     */
    @Override
    public Result<?> closeConn() {
        // 关闭连接
        String userId = providerService.currentUserId();
        // 判断当前用户是否存在
        if (StringUtils.isEmpty(userId)) {
            return Result.fail(ResponseCode.NEED_LOGIN);
        }
        // 关闭连接
        DataSource dataSource = databaseComponent.getDataSource(userId);
        if (Objects.nonNull(dataSource)) {
            try {
                dataSource.getConnection().close();
                return Result.success();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Result.fail();
    }

    /**
     * 获取数据源列表
     *
     * @return 结果
     */
    @Override
    public Result<?> getDatabases() {
        // 获取当前用户信息
        String userId = providerService.currentUserId();
        // 判断当前用户是否存在
        if (StringUtils.isEmpty(userId)) {
            return Result.fail(ResponseCode.NEED_LOGIN);
        }
        // 获取数据库列表
        List<ConnConfig> configs = baseMapper.selectList(new QueryWrapper<ConnConfig>().eq("user_id", userId).eq("is_deleted", ZERO_VALUE));
        // 判断数据库列表是否为空
        if (!configs.isEmpty()) {
            List<ConfigInfoDto> configInfoDtos = Lists.newArrayList();
            configs.forEach(config -> {
                ConfigInfoDto configInfoDto = new ConfigInfoDto();
                BeanUtils.copyProperties(config, configInfoDto);
                configInfoDto.setLevel(TreeNodeType.CONNECTION.toString());
                configInfoDto.setKey(config.getId());
                configInfoDto.setTitle(config.getConnNick());
                configInfoDtos.add(configInfoDto);
            });
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("configs", configInfoDtos);
            return Result.success(resultMap);
        }
        // TODO: 如果为空时，返回默认数据库列表
        return Result.success(Lists.newArrayList());
    }

    /**
     * 连接数据源
     *
     * @param connId 数据源 ID
     * @return 结果
     */
    @Override
    public Result<?> connDatasource(String connId) {
        // 获取当前用户信息
        String userId = providerService.currentUserId();
        // 判断当前用户是否存在
        if (StringUtils.isEmpty(userId)) {
            return Result.fail(ResponseCode.NEED_LOGIN);
        }
        // 获取数据源连接信息
        ConnConfig connEntity = baseMapper.selectById(connId);
        // 判断数据源连接信息是否为空
        if (Objects.isNull(connEntity)) {
            return Result.fail(ResponseCode.CONN_NOT_EXIST);
        }
        // 判断当前用户是否有权限访问该数据源
        if (!userId.equalsIgnoreCase(connEntity.getUserId())) {
            return Result.fail(ResponseCode.USER_NOT_AUTHORIZED);
        }
        // 连接数据源
        ConnVo connVo = new ConnVo();
        BeanUtils.copyProperties(connEntity, connVo);
        connVo.setConnSource(connEntity.getConnDriver());
        connVo.setType(TirggerType.CONNECT.toString());
        return this.checkConn(connVo);
    }

    /**
     * 获取自定义分组列表
     *
     * @return 结果
     */
    @Override
    public Result<?> getCustomGroupList() {
        // 获取当前用户信息
        String userId = providerService.currentUserId();
        // 判断当前用户是否存在
        if (StringUtils.isEmpty(userId)) {
            return Result.fail(ResponseCode.NEED_LOGIN);
        }
        ConnGroup connGroup = connGroupService.getById(DEFAULT_GROUP_ID);
        List<ConfigInfoDto> treeDtos = new ArrayList<>();
        // 判断自定义分组是否存在
        if (Objects.nonNull(connGroup)) {
            // 获取自定义分组下的连接列表
            ConfigInfoDto groupDto = new ConfigInfoDto();
            groupDto.setLevel(TreeNodeType.GROUP.toString());
            groupDto.setKey(connGroup.getId());
            groupDto.setTitle(connGroup.getName());
            groupDto.setUserId(userId);
            List<ConfigInfoDto> children = new ArrayList<>();
            // 获取自定义分组下的连接列表
            List<GroupConfig> groupConfigs = groupConfigService.list(new QueryWrapper<GroupConfig>().eq("user_id", userId).eq("group_id", DEFAULT_GROUP_ID).eq("is_deleted", ZERO_VALUE));
            // 获取自定义分组下的连接列表
            List<String> connIds = groupConfigs.stream().map(GroupConfig::getConnId).collect(Collectors.toList());
            // 判断连接列表是否为空
            if (!connIds.isEmpty()) {
                // 获取连接列表
                List<ConnConfig> connConfigs = baseMapper.selectBatchIds(connIds);
                // 获取连接列表
                connConfigs.forEach(connConfig -> {
                    ConfigInfoDto child = new ConfigInfoDto();
                    child.setLevel(TreeNodeType.CONNECTION.toString());
                    child.setKey(connConfig.getId());
                    child.setTitle(connConfig.getConnNick());
                    child.setUserId(userId);
                    children.add(child);
                });
                groupDto.setChildren(children);
            }
            treeDtos.add(groupDto);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("configs", treeDtos);
        return Result.success(resultMap);
    }
}
