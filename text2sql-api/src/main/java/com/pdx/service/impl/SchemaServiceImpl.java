package com.pdx.service.impl;

import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pdx.exception.BusinessException;
import com.pdx.mapper.DatabaseMapper;
import com.pdx.mapper.FieldMapper;
import com.pdx.model.chat.ChatDto;
import com.pdx.model.chat.Message;
import com.pdx.model.constants.ChatRoleConstant;
import com.pdx.model.dto.AutoImportSchemaDto;
import com.pdx.model.dto.FieldDto;
import com.pdx.model.dto.SchemaDetailResponse;
import com.pdx.model.entity.*;
import com.pdx.mapper.SchemaMapper;
import com.pdx.model.enums.DatabaseType;
import com.pdx.model.enums.FieldType;
import com.pdx.model.enums.PromptScene;
import com.pdx.model.vo.AutoImportSchemaVo;
import com.pdx.model.vo.InsertSchemaVo;
import com.pdx.model.vo.MoveDatabaseVo;
import com.pdx.model.vo.SchemaDetailVo;
import com.pdx.response.Result;
import com.pdx.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdx.utils.PromptUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
@Slf4j
@Service
public class SchemaServiceImpl extends ServiceImpl<SchemaMapper, Schema> implements SchemaService {

    @Resource
    private DatabaseMapper databaseMapper;

    @Resource
    private FieldMapper fieldMapper;

    @Resource
    private FieldService fieldService;

    @Resource
    private ModelService modelService;

    @Resource
    private ProviderService providerService;

    @Resource
    private PromptPromptsService promptPromptsService;

    @Value("${openai.variables.model}")
    private String model;

    @Value("${openai.variables.apiKey}")
    private String apiKey;

    @Value("${openai.remote-url}")
    private String remoteUrl;

    @Value("${openai.variables.maxTokens}")
    private String maxTokens;

    @Value("${openai.variables.temperature}")
    private String temperature;

    /**
     * 获取schema 详情
     * @param schemaDetailVo
     * @return
     */
    @Override
    public Result<?> schemaDetail(SchemaDetailVo schemaDetailVo) {
        // 校验参数
        if (StringUtils.isEmpty(schemaDetailVo.getSchemaId()) || StringUtils.isEmpty(schemaDetailVo.getDataId())) {
            return Result.fail("参数错误");
        }
        // 获取数据库信息
        Database database = databaseMapper.selectById(schemaDetailVo.getDataId());
        if (Objects.isNull(database)) {
            return Result.fail("数据库不存在");
        }
        // 获取 schema 信息
        Schema schema = getById(schemaDetailVo.getSchemaId());
        if (Objects.isNull(schema)) {
            return Result.fail("schema 不存在");
        }
        // 根据 schema 信息获取表信息
        List<Field> fields = fieldMapper.selectList(new QueryWrapper<Field>().eq("schema_id", schemaDetailVo.getSchemaId()));
        SchemaDetailResponse response = new SchemaDetailResponse();
        response.setDatabaseName(database.getDatabaseName());
        response.setSchemaId(schema.getId());
        response.setDatabaseId(database.getId());
        response.setSystem(DatabaseType.SYSTEM.toString().equalsIgnoreCase(database.getType()));
        response.setSchemaName(schema.getSchemaName());
        response.setRemark(schema.getRemark());
        response.setFields(fields);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("schemaDetail", response);
        return Result.success(resultMap);
    }

    @Override
    public SchemaDetailResponse schemaDetail(String databaseId, String schemaId) {
        // 校验参数
        if (StringUtils.isEmpty(databaseId) || StringUtils.isEmpty(schemaId)) {
            throw new BusinessException("参数错误");
        }
        // 获取数据库信息
        Database database = databaseMapper.selectById(databaseId);
        if (Objects.isNull(database)) {
            throw new BusinessException("数据库不存在");
        }
        // 获取 schema 信息
        Schema schema = getById(schemaId);
        if (Objects.isNull(schema)) {
            throw new BusinessException("schema 不存在");
        }
        // 根据 schema 信息获取表信息
        List<Field> fields = fieldMapper.selectList(new QueryWrapper<Field>().eq("schema_id", schemaId));
        SchemaDetailResponse response = new SchemaDetailResponse();
        response.setDatabaseName(database.getDatabaseName());
        response.setSchemaId(schema.getId());
        response.setDatabaseId(database.getId());
        response.setSystem(DatabaseType.SYSTEM.toString().equalsIgnoreCase(database.getType()));
        response.setSchemaName(schema.getSchemaName());
        response.setRemark(schema.getRemark());
        response.setFields(fields);
        return response;
    }

    /**
     * 新增 schema
     * @param insertSchemaVo 新增 schema
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Result<?> insertSchema(InsertSchemaVo insertSchemaVo) {
        // 获取用户 ID
        String userId = providerService.currentUserId();
        // 获取参数
        String databaseId = insertSchemaVo.getDatabaseId();
        if (StringUtils.isEmpty(databaseId)) {
            return Result.fail("参数错误");
        }
        // 查询数据库
        Database database = databaseMapper.selectById(databaseId);
        // 比较数据库名称是否发生变化
        boolean change = false;
        if (!insertSchemaVo.getDatabaseName().equalsIgnoreCase(database.getDatabaseName())) {
            change = true;
        }
        // 如果发生变化，则需要更新
        if (change) {
            database.setDatabaseName(insertSchemaVo.getDatabaseName());
            database.setUpdateTime(new Date());
            databaseMapper.updateById(database);
        }
        // schemaId
        String schemaId = UUID.randomUUID().toString();
        // 判断 schema ID 是否存在, 如果 schema Id 存在，则说明是更新操作，否则是新增操作
        if (StringUtils.isEmpty(insertSchemaVo.getSchemaId())) {
            Schema schema = new Schema();
            schema.setId(schemaId);
            schema.setSchemaName(insertSchemaVo.getSchemaName());
            schema.setRemark(insertSchemaVo.getRemark());
            schema.setType(DatabaseType.NORMAL.toString());
            schema.setUserId(userId);
            schema.setCreateTime(new Date());
            schema.setDataId(databaseId);
            schema.setIsDeleted(0);
            schema.setUpdateTime(new Date());
            // 创建 Schema
            baseMapper.insert(schema);
            // 创建字段
            List<Field> fields = insertSchemaVo.getFields();
            String finalSchemaId1 = schemaId;
            fields.forEach(field -> {
                field.setSchemaId(finalSchemaId1);
                field.setId(UUID.randomUUID().toString());
                field.setUpdateTime(new Date());
                field.setCreateTime(new Date());
                fieldMapper.insert(field);
            });
        } else {
            // 如果 schema Id 不为空，则需要将原先的删除，重新创建
            Schema schema = baseMapper.selectById(insertSchemaVo.getSchemaId());
            if (Objects.isNull(schema)) {
                return Result.fail(" Schema 不存在");
            }
            if (!insertSchemaVo.getSchemaName().equalsIgnoreCase(schema.getSchemaName())) {
                schema.setSchemaName(insertSchemaVo.getSchemaName());
            }
            baseMapper.updateById(schema);
            // 查询当前 schema 下的所有的字段
            schemaId = insertSchemaVo.getSchemaId();
            fieldMapper.delete(new QueryWrapper<Field>().eq("schema_id", schemaId));
            // 创建字段
            List<Field> fields = insertSchemaVo.getFields();
            String finalSchemaId = schemaId;
            fields.forEach(field -> {
                field.setSchemaId(finalSchemaId);
                field.setId(UUID.randomUUID().toString());
                field.setUpdateTime(new Date());
                field.setCreateTime(new Date());
                fieldMapper.insert(field);
            });
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("databaseId", databaseId);
        resultMap.put("schemaId", schemaId);
        return Result.success(resultMap);
    }

    /**
     * 删除 Schema
     * @param id 主键 ID
     * @return 操作结果
     */
    @Override
    public Result<?> deleteSchemaById(String id) {
        String userId = providerService.currentUserId();
        Schema schema = baseMapper.selectById(id);
        if (Objects.isNull(schema)) {
            return Result.fail("不存在");
        }
        if (!userId.equalsIgnoreCase(schema.getUserId())) {
            return Result.fail("非法操作");
        }
        // 删除 schema 下的所有的字段
        fieldMapper.delete(new QueryWrapper<Field>().eq("schema_id", id));
        baseMapper.deleteById(id);
        return Result.success();
    }

    /**
     * 自动导入 Schema
     *
     * @param importSchemaVo 导入 Schema
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Result<?> autoImport(AutoImportSchemaVo importSchemaVo) {
        String userId = providerService.currentUserId();
        if (StringUtils.isEmpty(userId)) {
            return Result.fail("当前用户不存在");
        }
        if (StringUtils.isEmpty(importSchemaVo.getImportContent())) {
            return Result.fail("参数错误");
        }
        String databaseId = UUID.randomUUID().toString();
        Database database = new Database();
        database.setUpdateTime(new Date());
        database.setCreateTime(new Date());
        database.setId(databaseId);
        database.setDatabaseName("demo-" + databaseId.substring(0, 3));
        database.setUserId(userId);
        database.setType(DatabaseType.NORMAL.toString());
        database.setIsDeleted(0);
        databaseMapper.insert(database);
        // 获取 Prompt 场景
        PromptPrompts promptPrompts = promptPromptsService.getActivePrompt(PromptScene.TRANSFORM_SQL_TO_JSON.toString());
        String abstractPrompt = promptPrompts.getPromptTemplate();
        String prompt = PromptUtils.createTransformSQLTOJson(importSchemaVo.getImportContent(), abstractPrompt);
        // 获取激活模型
        Model activeModel = modelService.getActiveModel();
        ArrayList<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setContent(prompt);
        message.setRole(ChatRoleConstant.SYSTEM);
        messages.add(message);
        HttpResponse httpResponse = this.createResponse(messages, activeModel);
        String resStr = httpResponse.body();
        ChatDto bean = JSONUtil.toBean(resStr, ChatDto.class);
        String result = bean.getChoices().get(0).getMessage().getContent();
        result = result
                .replace("```json", "")
                .replaceAll("```", "")
                .replace("\n", "");
        List<Schema> schemas = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        // 多表导入
        if (importSchemaVo.isMoreImport()) {
            JSONArray resultArrays = JSONObject.parseArray(result);
            List<AutoImportSchemaDto> autoImportSchemaDtos = new ArrayList<>();
            resultArrays.forEach(item -> {
                AutoImportSchemaDto autoImportSchemaDto = JSON.parseObject(item.toString(), AutoImportSchemaDto.class);
                autoImportSchemaDtos.add(autoImportSchemaDto);
            });
            if (!autoImportSchemaDtos.isEmpty()) {
                autoImportSchemaDtos.forEach(item -> {
                    Schema schema = new Schema();
                    String schemaId = UUID.randomUUID().toString();
                    schema.setSchemaName(item.getSchemaName());
                    schema.setId(schemaId);
                    schema.setType(DatabaseType.NORMAL.toString());
                    schema.setUserId(userId);
                    schema.setDataId(databaseId);
                    schema.setUpdateTime(new Date());
                    schema.setCreateTime(new Date());
                    schema.setIsDeleted(0);
                    schemas.add(schema);
                    if (!item.getFields().isEmpty()) {
                        item.getFields().forEach(fieldDto -> {
                            Field field = new Field();
                            BeanUtils.copyProperties(fieldDto, field);
                            String fieldId = UUID.randomUUID().toString();
                            field.setSchemaId(schemaId);
                            field.setId(fieldId);
                            Integer code = FieldType.getCode(fieldDto.getFieldType());
                            field.setFieldType(code);
                            field.setNull(fieldDto.isPrimaryKey());
                            field.setUpdateTime(new Date());
                            field.setDeleted(false);
                            field.setCreateTime(new Date());
                            fields.add(field);
                        });
                    }
                });
            }
        } else {
            AutoImportSchemaDto autoImportSchemaDto = JSONObject.parseObject(result, AutoImportSchemaDto.class);
            if (Objects.nonNull(autoImportSchemaDto)) {
                Schema schema = new Schema();
                String schemaId = UUID.randomUUID().toString();
                schema.setSchemaName(autoImportSchemaDto.getSchemaName());
                schema.setId(schemaId);
                schema.setType(DatabaseType.NORMAL.toString());
                schema.setUserId(userId);
                schema.setDataId(databaseId);
                schema.setUpdateTime(new Date());
                schema.setCreateTime(new Date());
                schema.setIsDeleted(0);
                baseMapper.insert(schema);
                List<FieldDto> fieldDtos = autoImportSchemaDto.getFields();
                fieldDtos.forEach(fieldDto -> {
                    Field field = new Field();
                    BeanUtils.copyProperties(fieldDto, field);
                    String fieldId = UUID.randomUUID().toString();
                    field.setSchemaId(schemaId);
                    field.setId(fieldId);
                    Integer code = FieldType.getCode(fieldDto.getFieldType());
                    field.setFieldType(code);
                    field.setNull(fieldDto.isPrimaryKey());
                    field.setUpdateTime(new Date());
                    field.setDeleted(false);
                    field.setCreateTime(new Date());
                    fields.add(field);
                });
            }
        }
        boolean success = false;
        if (!schemas.isEmpty()) {
            // 判断是否选择了需要导入的数据库
            if (StringUtils.isNotEmpty(importSchemaVo.getDatabaseId())) {
                schemas.forEach(schema -> {
                    schema.setDataId(importSchemaVo.getDatabaseId());
                });
            }
            success = this.saveBatch(schemas);
        }
        if (!fields.isEmpty()) {
            success = fieldService.saveBatch(fields);
        }
        return success ? Result.success() : Result.fail("导入失败");
    }

    /**
     * 移动数据库
     *
     * @param moveDatabaseVo 移动数据库
     * @return 操作结果
     */
    @Override
    public Result<?> moveDatabase(MoveDatabaseVo moveDatabaseVo) {
        // 校验参数
        String databaseId = moveDatabaseVo.getDatabaseId();
        String targetDatabaseId = moveDatabaseVo.getTargetDatabaseId();
        String schemaId = moveDatabaseVo.getSchemaId();
        // 如果目标数据库 ID 为空，则需要抛出异常
        if (StringUtils.isEmpty(targetDatabaseId)) {
            return Result.fail("目标数据库不能为空");
        }
        // 目标数据库与源数据库不能相同
        if (StringUtils.equals(databaseId, targetDatabaseId)) {
            return Result.fail("目标数据库不能与源数据库相同");
        }
        // 查询目标数据库是否存在
        Integer count = databaseMapper.selectCount(new QueryWrapper<Database>().eq("id", targetDatabaseId));
        Integer originCount = databaseMapper.selectCount(new QueryWrapper<Database>().eq("id", databaseId));
        if (count == 0) {
            return Result.fail("目标数据库不存在");
        }
        if (originCount == 0) {
            return Result.fail("源数据库不存在");
        }
        // 如果移动源数据库 ID 为空，则数据库表 ID 不能为空
        if (!StringUtils.isEmpty(databaseId) && !StringUtils.isEmpty(schemaId)) {
            // 获取需要转移的数据库表
            Schema schema = this.getById(schemaId);
            if (Objects.isNull(schema)) {
                return Result.fail("数据库表不存在");
            }
            schema.setDataId(targetDatabaseId);
            schema.setUpdateTime(new Date());
            int result = baseMapper.updateById(schema);
            return result > 0 ? Result.success() : Result.fail("迁移失败！");
        }
        boolean success = false;
        if (StringUtils.isEmpty(schemaId) && !StringUtils.isEmpty(databaseId)) {
            // 获取所有的数据库表
            List<Schema> schemas = baseMapper.selectList(new QueryWrapper<Schema>().eq("data_id", databaseId));
            if (!schemas.isEmpty()) {
                // 遍历数据库表
                schemas.forEach(schema -> {
                    schema.setDataId(targetDatabaseId);
                    schema.setUpdateTime(new Date());
                });
                // 更新数据库表
                success = this.updateBatchById(schemas);
            }
        }
        return success ? Result.success() : Result.fail("迁移失败！");
    }


    /**
     * 创建响应
     * @param messages
     * @return
     */
    private HttpResponse createResponse(List<Message> messages, Model activeModel) {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("model", activeModel.getAssistantId());
        bodyJson.put("max_tokens", Integer.parseInt(maxTokens));
        bodyJson.put("temperature", Double.parseDouble(temperature));
        bodyJson.put("messages", messages);
        return HttpUtil.createPost(activeModel.getRemoteUrl())
                        .header(Header.AUTHORIZATION, "Bearer " + activeModel.getAuthKey())
                        .header(Header.CONTENT_TYPE, "application/json")
                        .body(JSONUtil.toJsonStr(bodyJson)).execute();
    }
}
