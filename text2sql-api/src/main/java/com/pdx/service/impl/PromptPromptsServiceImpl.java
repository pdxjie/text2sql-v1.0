package com.pdx.service.impl;

import com.pdx.model.entity.PromptPrompts;
import com.pdx.mapper.PromptPromptsMapper;
import com.pdx.service.PromptPromptsService;
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
public class PromptPromptsServiceImpl extends ServiceImpl<PromptPromptsMapper, PromptPrompts> implements PromptPromptsService {

    /**
     * 获取当前激活的 Prompt
     * @param scene 场景
     * @return 激活的 Prompt 对象
     */
    @Override
    public PromptPrompts getActivePrompt(String scene) {
        return this.lambdaQuery().eq(PromptPrompts::getScene, scene)
                .eq(PromptPrompts::getIsActive, true)
                .eq(PromptPrompts::getIsDeleted, false)
                .list().stream().findFirst().orElse(null);
    }
}
