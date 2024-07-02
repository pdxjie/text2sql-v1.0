package com.pdx.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pdx.exception.BusinessException;
import com.pdx.mapper.FieldMapper;
import com.pdx.mapper.UserMapper;
import com.pdx.model.dto.DataBasePageDto;
import com.pdx.model.dto.DatabaseTreeDto;
import com.pdx.model.dto.DatabaseTreeNode;
import com.pdx.model.dto.SchemaDetailResponse;
import com.pdx.model.entity.*;
import com.pdx.mapper.DatabaseMapper;
import com.pdx.model.enums.*;
import com.pdx.model.vo.DataBaseSearchVo;
import com.pdx.model.vo.DatabaseInsertVo;
import com.pdx.model.vo.GenerateSQLStreamVo;
import com.pdx.model.vo.RemoveDatabaseVo;
import com.pdx.response.Result;
import com.pdx.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdx.utils.PromptUtils;
import net.dreamlu.mica.core.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 派同学
 * @since 2024-05-18
 */
@Service
public class DatabaseServiceImpl extends ServiceImpl<DatabaseMapper, Database> implements DatabaseService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ProviderService providerService;

    @Resource
    private SchemaService schemaService;

    @Resource
    private FieldMapper fieldMapper;

    @Resource
    private PromptPromptsService promptPromptsService;

    @Resource
    private OpenAiProvider openAiProvider;

    @Resource
    private ModelService modelService;

    /**
     * 分页查询数据库列表
     *
     * @param searchVo 条件
     * @return 分页结果
     */
    @Override
    public Result<?> databasePages(DataBaseSearchVo searchVo) {
        Page<Database> page = new Page<>(searchVo.getCurrent(), searchVo.getPageSize());
        // 创建查询构造器
        QueryWrapper<Database> wrapper = new QueryWrapper<>();
        // 条件判断
        if (StringUtils.isNotEmpty(searchVo.getDatabaseName())) {
            wrapper.like("database_name", searchVo.getDatabaseName());
        }
        // 非系统模版
        wrapper.ne("type", DatabaseType.SYSTEM.toString());
        // 按照创建时间排序
        wrapper.orderByDesc("create_time", "sort");
        // 查询结果
        Page<Database> databasePage = this.page(page, wrapper);
        List<DataBasePageDto> dtos = new ArrayList<>();
        // 获取所有数据库记录
        List<Database> records = databasePage.getRecords();
        // 重新赋值
        records.forEach(record -> {
            DataBasePageDto dto = new DataBasePageDto();
            BeanUtils.copyProperties(record, dto);
            if (StringUtils.isNotEmpty(record.getUserId())) {
                User userInfo = userMapper.selectById(record.getUserId());
                dto.setNickName(userInfo.getNickName());
                dto.setAvatar(userInfo.getAvatar());
                dtos.add(dto);
            }
        });
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", databasePage.getTotal());
        resultMap.put("records", dtos);
        return Result.success(resultMap);
    }

    /**
     * 获取数据库树
     *
     * @return 数据库树
     */
    @Override
    public Result<?> databaseTree() {
        // 获取当前用户
        String userId = providerService.currentUserId();
        // 首先获取系统模版
        QueryWrapper<Database> dataWrapper = new QueryWrapper<>();
        dataWrapper.eq("is_deleted", 0);
        // 系统的数据库表模版
        QueryWrapper<Schema> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        // 如果用户未登录，则获取系统模版
        if (!StringUtil.isBlank(userId)) {
            dataWrapper.and(wrapper ->
                    wrapper.eq("type", DatabaseType.SYSTEM.toString())
                            .or(w -> w.eq("user_id", userId).eq("type", DatabaseType.NORMAL.toString()))
            );
            queryWrapper.and(wrapper ->
                    wrapper.eq("type", DatabaseType.SYSTEM.toString())
                            .or(w -> w.eq("user_id", userId).eq("type", DatabaseType.NORMAL.toString()))
            );
        } else {
            dataWrapper.eq("type", DatabaseType.SYSTEM.toString());
            queryWrapper.eq("type", DatabaseType.SYSTEM.toString());
        }
        // 获取所有数据库记录
        List<Database> databases = this.list(dataWrapper);
        List<Schema> schemas = schemaService.list(queryWrapper);
        Map<String, List<Schema>> dataIdToSchemaListMap = schemas.stream().filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Schema::getDataId // 根据每个 Schema 对象的 dataId 分组
                ));
        List<DatabaseTreeDto> treeDtos = new ArrayList<>();
        databases.forEach(database -> {
            DatabaseTreeDto databaseTreeDto = new DatabaseTreeDto();
            databaseTreeDto.setKey(database.getId());
            databaseTreeDto.setTitle(database.getDatabaseName());
            databaseTreeDto.setLeaf(false);
            databaseTreeDto.setType(database.getType());
            // 获取数据库表
            List<Schema> schemaList = dataIdToSchemaListMap.get(database.getId());
            if (CollectionUtils.isNotEmpty(schemaList)) {
                List<DatabaseTreeNode> nodes = new ArrayList<>();
                schemaList.forEach(schema -> {
                    DatabaseTreeNode schemaTreeDto = new DatabaseTreeNode();
                    schemaTreeDto.setKey(schema.getId());
                    schemaTreeDto.setTitle(schema.getSchemaName());
                    schemaTreeDto.setType(schema.getType());
                    schemaTreeDto.setLeaf(true);
                    nodes.add(schemaTreeDto);
                });
                databaseTreeDto.setChildren(nodes);
            }
            treeDtos.add(databaseTreeDto);
        });

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("treeNodes", treeDtos);
        return Result.success(resultMap);
    }

    /**
     * 新增数据库
     *
     * @param insertVo 数据库信息
     * @return 新增结果
     */
    @Override
    public Result<?> insertDatabase(DatabaseInsertVo insertVo) {
        // 校验参数
        if (StringUtils.isEmpty(insertVo.getDatabaseName())) {
            return Result.fail("数据库名称不能为空");
        }
        String userId = providerService.currentUserId();
        Database database = new Database();
        String databaseId = UUID.randomUUID().toString();
        database.setId(databaseId);
        database.setDatabaseName(insertVo.getDatabaseName());
        database.setType(DatabaseType.NORMAL.toString());
        database.setSort(0);
        database.setUserId(userId);
        database.setIsDeleted(0);
        database.setCreateTime(new Date());
        database.setUpdateTime(new Date());
        int result = baseMapper.insert(database);
        return result > 0 ? Result.success(databaseId) : Result.fail();
    }

    /**
     * 删除数据库
     *
     * @param removeDatabaseVo 数据库 id
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public Result<?> removeDatabase(RemoveDatabaseVo removeDatabaseVo) {
        // 获取用户 ID
        String userId = providerService.currentUserId();
        // 获取数据库 ID
        String databaseId = removeDatabaseVo.getDatabaseId();
        boolean force = removeDatabaseVo.isForce();
        // 校验参数
        if (StringUtils.isEmpty(databaseId)) {
            return Result.fail("数据库ID不能为空");
        }
        // 校验数据库是否存在
        Database database = this.getById(databaseId);
        if (Objects.isNull(database)) {
            return Result.fail("数据库不存在");
        }
        if (!userId.equalsIgnoreCase(database.getUserId())) {
            return Result.fail("您没有权限删除该数据库");
        }
        if (database.getType().equals(DatabaseType.SYSTEM.toString())) {
            return Result.fail("系统模版不能删除");
        }
        // 获取当前数据库下的所有表
        QueryWrapper<Schema> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_id", databaseId);
        List<Schema> schemas = schemaService.list(queryWrapper);
        // 判断是否强制删除
        if (force) {
            if (CollectionUtils.isNotEmpty(schemas)) {
                // 获取所有的 schema Id
                List<String> schemaIds = schemas.stream().map(Schema::getId).collect(Collectors.toList());
                // 获取所有的字段
                QueryWrapper<Field> fieldQueryWrapper = new QueryWrapper<>();
                fieldQueryWrapper.in("schema_id", schemaIds);
                fieldMapper.delete(fieldQueryWrapper);
                // 删除数据库表
                schemaService.remove(queryWrapper);
            } else {
                // 数据库的表不为空
                if (CollectionUtils.isNotEmpty(schemas)) {
                    return Result.fail("请先删除数据库下的表");
                }
                return this.removeById(databaseId) ? Result.success() : Result.fail();
            }
        }
        // 删除数据库
        return this.removeById(databaseId) ? Result.success() : Result.fail();
    }

    /**
     * 分析生成 SQL
     *
     * @param streamVo 数据库 id
     * @return 操作结果
     */
    @Override
    public SseEmitter generateSQLStream(GenerateSQLStreamVo streamVo) {
        // 判断用户是否输入了需求描述
        if (StringUtils.isEmpty(streamVo.getDemand())) {
            throw new BusinessException("请输入需求描述");
        }
        // 获取数据库驱动
        Integer sqlDriver = streamVo.getSqlDriver();
        if (Objects.isNull(sqlDriver)) {
            throw new BusinessException("请选择数据库驱动");
        }
        // 获取数据库驱动类型
        String driverType = SQLType.getType(sqlDriver);
        // 需求描述
        String demand = streamVo.getDemand();
        // 获取模型
        Model model = modelService.getActiveModel();
        if (Objects.isNull(model)) {
            throw new BusinessException("请先激活模型");
        }
        // 获取 Prompt 场景
        PromptPrompts promptEntity = promptPromptsService.getActivePrompt(PromptScene.GENERATE_QUERY_SQL.toString());
        // 获取 Prompt 模版
        String promptTemplate = promptEntity.getPromptTemplate();
        // 数据库表内容
        String schemaContent = "";
        // 判断方式
        if (GenerateType.CUSTOM.toString().equalsIgnoreCase(streamVo.getType())) {
            if (StringUtils.isEmpty(streamVo.getSchemas())) {
                throw new BusinessException("请输入数据库表内容");
            }
            schemaContent = streamVo.getSchemas();
        }
        if (GenerateType.SYSTEM.toString().equalsIgnoreCase(streamVo.getType())) {
            if (CollectionUtils.isEmpty(streamVo.getSchemaIds()) || StringUtils.isEmpty(streamVo.getDatabaseId())) {
                throw new BusinessException("请选择数据库表");
            }
            StringBuilder schemaBuilder = new StringBuilder();
            streamVo.getSchemaIds().forEach(schemaId -> {
                SchemaDetailResponse response = schemaService.schemaDetail(streamVo.getDatabaseId(), schemaId);
                String tableInfo = JSONUtil.toJsonStr(response);
                String generateCreateTableSQL = com.pdx.utils.StringUtil.generateCreateTableSQL(tableInfo);
                schemaBuilder.append(generateCreateTableSQL).append("\n");
            });
            schemaContent = schemaBuilder.toString();
        }
        // 获取 Prompt
        String prompt = PromptUtils.createGenerateSQLPrompt(schemaContent, promptTemplate, driverType, demand);
        return openAiProvider.chatCommonRequest(prompt, true, model);
    }
}
