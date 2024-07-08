package com.pdx.service.impl;

import com.pdx.config.DatabaseComponent;
import com.pdx.model.entity.ConnConfig;
import com.pdx.mapper.ConnConfigMapper;
import com.pdx.model.vo.ConnVo;
import com.pdx.response.ResponseCode;
import com.pdx.response.Result;
import com.pdx.service.ConnConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdx.service.ProviderService;
import com.pdx.strategy.database.DatasourceCheckContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;

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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        // TODO 关闭连接
        return Result.success();
    }
}
