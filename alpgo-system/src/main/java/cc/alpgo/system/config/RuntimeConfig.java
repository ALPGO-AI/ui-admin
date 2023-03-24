package cc.alpgo.system.config;

import cc.alpgo.common.core.domain.entity.SysDictData;
import cc.alpgo.common.core.domain.entity.SysDictType;
import cc.alpgo.common.core.redis.RedisCache;
import cc.alpgo.common.utils.DictUtils;
import cc.alpgo.system.domain.SysConfig;
import cc.alpgo.system.mapper.SysConfigMapper;
import cc.alpgo.system.mapper.SysDictDataMapper;
import cc.alpgo.system.mapper.SysDictTypeMapper;
import cc.alpgo.common.constant.Constants;
import cc.alpgo.common.exception.job.TaskException;
import cc.alpgo.quartz.domain.SysJob;
import cc.alpgo.quartz.mapper.SysJobMapper;
import cc.alpgo.quartz.util.ScheduleUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuntimeConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RuntimeConfig.class);

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SysJobMapper jobMapper;

    /**
     * 项目启动时，初始化参数
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOGGER.info("init redis ...");
        this.initRedis();
        LOGGER.info("init dict ...");
        this.initDict();
        try {
            LOGGER.info("init job ...");
            this.initJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (TaskException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化定时任务信息到缓存
     *
     * @throws SchedulerException
     * @throws TaskException
     */
    public void initJob() throws SchedulerException, TaskException {
        scheduler.clear();
        List<SysJob> jobList = jobMapper.selectJobAll();
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    /**
     * 初始化参数到缓存
     */
    public void initRedis() {
        List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
        for (SysConfig config : configsList) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 初始化字典到缓存
     */
    public void initDict() {
        List<SysDictType> dictTypeList = dictTypeMapper.selectDictTypeAll();
        for (SysDictType dictType : dictTypeList) {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dictType.getDictType());
            DictUtils.setDictCache(dictType.getDictType(), dictDatas);
        }
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return Constants.SYS_CONFIG_KEY + configKey;
    }
}
