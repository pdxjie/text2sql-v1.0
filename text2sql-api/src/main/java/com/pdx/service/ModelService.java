package com.pdx.service;

import com.pdx.model.entity.Model;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
public interface ModelService extends IService<Model> {

    /**
     * 获取当前激活的模型
     *
     * @return
     */
    Model getActiveModel();

    Model getActiveModelBySence(String string);
}
