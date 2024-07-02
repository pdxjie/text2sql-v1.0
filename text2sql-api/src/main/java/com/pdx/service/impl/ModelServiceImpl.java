package com.pdx.service.impl;

import com.pdx.model.entity.Model;
import com.pdx.mapper.ModelMapper;
import com.pdx.service.ModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

    @Override
    public Model getActiveModel() {
        return this.lambdaQuery()
                .eq(Model::getIsDeleted, 0)
                .eq(Model::getIsActive, 1)
                .one();
    }

    /**
     * 根据模型名称和激活状态获取活跃模型。
     *
     * @param model 模型的名称，用于查询特定的模型。
     * @return 返回查询到的活跃模型对象。如果没有匹配的活跃模型，则返回null。
     */
    @Override
    public Model getActiveModelBySence(String model) {
        // 使用Lambda查询语言查询满足条件的模型：模型名称匹配、未被删除、处于激活状态
        return this.lambdaQuery()
                .eq(Model::getModel, model)
                .eq(Model::getIsDeleted, 0)
                .eq(Model::getIsActive, 1)
                .one();
    }
}
