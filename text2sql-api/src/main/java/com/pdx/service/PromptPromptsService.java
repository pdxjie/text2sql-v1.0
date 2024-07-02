package com.pdx.service;

import com.pdx.model.entity.PromptPrompts;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
public interface PromptPromptsService extends IService<PromptPrompts> {

    /**
     * 获取当前激活的 Prompt
     * @param scene 场景
     * @return 激活的 Prompt 对象
     */
    PromptPrompts getActivePrompt(String scene);
}
