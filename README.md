# 宠物领养系统（PetAdoption）

基于 Spring Boot + Vue 2 的宠物领养管理系统，包含前台（用户领养、发帖、评论）与后台（宠物、用户、公告、领养申请审核等）两部分。

> 完整的功能清单、接口说明、数据库设计与改动记录见 [开发文档.md](./开发文档.md)；本文只保留最短的跑通步骤。

## 技术栈

| 层 | 技术 |
| --- | --- |
| 后端 | Spring Boot 2.2.4、MyBatis-Plus 3.5.3、MySQL、JWT、EasyExcel、Lombok |
| 前端 | Vue 2.6、Vue Router、Vuex、Element UI 2.15、Axios、ECharts |
| 构建 | Maven 3.9、JDK 1.8；npm / vue-cli 4.5 |

- 根包名：`com.petadoption`
- 后端 artifactId：`pet-adoption-api`
- 接口前缀：`/api/v1.0/pet-adoption-api`
- 端口：后端 21090，前端 21091

## 目录结构

```
PetAdoption/
├── api/                 # 后端 Spring Boot 工程
│   ├── src/main/java/com/petadoption/
│   ├── src/main/resources/          # application.yml、mapper/*.xml
│   ├── file/                        # 上传图片存放目录（已含初始宠物图片）
│   └── pom.xml
├── view/                # 前端 Vue 工程
│   ├── src/
│   ├── public/
│   └── package.json
├── sql/
│   ├── init.sql         # 建库脚本
│   └── pet_adoption.sql # 表结构与初始数据
└── README.md
```

## 环境要求

- JDK 1.8（必须，Spring Boot 2.2 与 Lombok 1.18.10 不支持高版本 JDK 直接编译）
- Maven 3.6+
- Node.js 16.x / 18.x（20 以下；本项目依赖较老，Node 22+ 可能编译失败）
- MySQL 5.7 或 8.x

## 一、初始化数据库

```bash
cd sql
mysql -uroot -p < init.sql                          # 创建 pet_adoption 库
mysql -uroot -p pet_adoption < pet_adoption.sql      # 导入表结构与初始数据
```

导入后应有 11 张表 + 1 个 `users` 视图，`user` 表 7 条初始数据。

如果本机 MySQL 的 root 密码不是 `123456`，请修改 `api/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pet_adoption?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true
    username: root
    password: 你的密码
```

> 密码以 0 开头时需加单引号，例如 `password: '0123456'`。

## 二、启动后端

用 IDEA 打开 `api` 目录（不是外层 PetAdoption），确认：

1. Project SDK 选择 **JDK 1.8**（Corretto 8 / Oracle 8 均可），Language Level 8；
2. Maven 自动导入依赖完成后，运行 `com.petadoption.PetAdoptApplication`；
3. 控制台出现 `Tomcat started on port(s): 21090` 即成功。

> 不要在命令行直接 `java -jar target/*.jar` 运行。`FileController` 通过
> `PathUtils.getClassLoadRootPath()` 定位上传目录，打成 jar 后该路径会指向 jar 内部，
> 导致「无法创建上传目录」、图片 404。用 IDEA 运行（class 目录方式）则一切正常。

## 三、启动前端

```bash
cd view
npm install          # 首次运行；已配置 npmmirror 源
npm run dev
```

浏览器访问 <http://localhost:21091>。

未登录即可浏览首页（落地页）与宠物大厅：大厅支持类别、关键词、性别、疫苗筛选与排序、分页；点进宠物详情只露出部分描述，并提示登录后查看完整信息。

前端 `src/utils/request.js` 中开发环境的后端地址是
`http://localhost:21090/api/v1.0/pet-adoption-api`，如后端端口有变需同步修改。

## 四、默认账号

| 角色 | 账号 | 密码 |
| --- | --- | --- |
| 管理员 | admin | 123456 |
| 普通用户 | lichunran / zhouzhiruo / guihua / zhangsan / zhangsan2 | 123456 |

前端登录时会先对密码做一次 MD5，服务端再与库中已经过 MD5 的值比较，因此库中存的是
`md5(md5("123456")) = 14e1b600b1fd579f47433b88e8d85291`。直接用接口调试时也要传这个值。

## 五、图片与上传

宠物图片、头像等静态资源存放在 `api/file/`，数据库里保存的是
`/api/v1.0/pet-adoption-api/file/getFile?fileName=xxx` 形式的相对地址。
新增图片通过页面上传即可，文件会写入 `api/file/`。

## 六、常见问题

| 现象 | 原因与处理 |
| --- | --- |
| 页面白屏 | 前端未启动或端口不对；看 21091 是否在监听 |
| 登录报错 | `application.yml` 的库名/账号/密码不正确，或没导入 `pet_adoption` 库 |
| 图片不显示 | 未上传过图片，或后端是以 jar 方式启动的（见上文） |
| `digital envelope routines::unsupported` | Node 17+ 与旧 webpack 的 OpenSSL 兼容问题，换 Node 16/18 |
| 编译报「找不到符号」且指向 Lombok | IDEA 未开启 Annotation Processing，或 JDK 不是 8 |
| 项目路径含中文导致找不到主类 | 把工程放到纯英文路径下 |

## 七、版本管理

仓库已 `git init`，首个提交为改造前的原始代码基线，可直接 `git diff` 查看包名、
数据库名、接口前缀与水印清理的全部改动。
