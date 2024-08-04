package com.pdx.service;

import com.pdx.model.entity.ConnConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pdx.model.vo.ConnVo;
import com.pdx.response.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 派同学
 * @since 2024-07-07
 */
public interface ConnConfigService extends IService<ConnConfig> {

    /**
     * 检查连接
     *
     * @param connVo 连接信息
     * @return 结果
     */
    Result<?> checkConn(ConnVo connVo);

    /**
     * 关闭连接
     */
    Result<?> closeConn();

    Result<?> getDatabases();

    Result<?> connDatasource(String connId);

    /**
     * 获取自定义分组列表
     *
     * @return 结果
     */
    Result<?> getCustomGroupList();

}
