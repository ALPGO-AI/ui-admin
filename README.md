
# UI-Admin

Stable Diffusion WebUI Admin, based on https://gitee.com/y_project/RuoYi-Vue

### 项目CI

本项目使用 Coding 进行构建和部署，https://alpgo.coding.net/p/alpgo-ai/ci/job

## 本地开发

### 前端启动

1. 需要安装node开发环境和npm
2. 进入前端项目目录安装依赖并启动

    ```
    cd alpgo-ui

    npm install

    npm run dev
    ```
3. 如果你准备将前端部署在域名网站上，可以在vue的环境配置 .env 相关文件中修改备案号
    ```
    # 备案号
    VUE_APP_BACKUP_NO=备案号
    ```
### 后端启动

1. 需要 maven 环境，最好使用 idea 进行开发
1. 准备好 mysql 数据库后，请配置环境变量或修改 `alpgo-admin/src/main/resources/application-druid.yml` 中的参数
    ```
    druid:
        # 主库数据源
        master:
            url: jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/ui_admin?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: ${MYSQL_USERNAME}
            password: ${MYSQL_PASSWORD}
        # 从库数据源
    ```
1. 准备好 redis 服务后，请配置环境变量 `alpgo-admin/src/main/resources/application.yml` 中的参数
    ```
    # redis 配置
    redis:
        # 地址
        host: ${REDIS_HOST}
        # 端口，默认为6379
        port: ${REDIS_PORT}
        # 数据库索引
        database: 0
        # 密码
        password: ${REDIS_PASSWORD}
    ```
1. 准备好腾讯云 COS 服务后，请配置环境变量 `alpgo-admin/src/main/resources/application.yml` 中的参数
    ```
    # 腾讯云cos配置
    cosApiSecretId: ${COS_API_SECRET_ID}
    cosApiSecretKey: ${COS_API_SECRET_KEY}
    cosApiBucketName: ${COS_API_BUCKET_NAME}
    cosApiRegion: ${COS_API_REGION}
    ```
1. 使用 maven 安装依赖并启动
    ```
    mvn install

    mvn spring-boot:run
    ```