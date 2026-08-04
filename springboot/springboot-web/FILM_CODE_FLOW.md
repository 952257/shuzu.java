# Film 模块代码讲解

本文档说明 `film` 相关代码的职责划分、接口用途，以及请求进入系统后在代码内部的调用流程。

## 1. 相关类的职责

### 1.1 `FilmController`

文件位置：`src/main/java/com/springboot/web/contoller/FilmController.java`

职责：

- 提供影片模块的增删改查接口
- 接收前端请求参数
- 在内存 `list` 中模拟数据库操作
- 将 `Film` 转成 `FilmVo` 返回给前端
- 在参数缺失或数据不存在时抛出业务异常

注意：

- 当前没有 `Service` 层，也没有 `Mapper`/`Repository` 层
- 控制器直接操作内存集合 `List<Film>`
- 这些数据只在程序运行期间有效，服务重启后会恢复初始化数据

### 1.2 `Film`

文件位置：`src/main/java/com/springboot/web/common/po/Film.java`

职责：

- 表示影片数据对象
- 当前用于模拟“数据库中的一条记录”

常见字段：

- `filmId`：影片主键
- `title`：标题
- `description`：描述
- `rating`：分级
- `lastUpdate`：最后更新时间

### 1.3 `FilmDto`

文件位置：`src/main/java/com/springboot/web/dto/FilmDto.java`

职责：

- 接收前端传入的新增、修改参数
- 作为请求体对象使用

特点：

- 不包含 `filmId`
- `lastUpdate` 支持 `"yyyy-MM-dd HH:mm:ss"` 格式

### 1.4 `FilmVo`

文件位置：`src/main/java/com/springboot/web/vo/FilmVo.java`

职责：

- 作为返回给前端的影片数据对象

特点：

- 包含 `filmId`
- 用于和 `FilmDto` 做职责区分

### 1.5 `CommonResult`

文件位置：`src/main/java/com/springboot/web/common/CommonResult.java`

职责：

- 统一接口返回格式

返回结构：

- `code`：错误码，默认是 `0`
- `message`：提示信息，默认是 `"操作成功"`
- `data`：真正的业务数据

示例：

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "filmId": 1,
    "title": "学院恐龙"
  }
}
```

### 1.6 `ServiceException` 和 `ServiceExceptionEnum`

文件位置：

- `src/main/java/com/springboot/web/common/ServiceException.java`
- `src/main/java/com/springboot/web/common/ServiceExceptionEnum.java`

职责：

- `ServiceExceptionEnum` 定义业务错误码和错误信息
- `ServiceException` 用于抛出业务异常

影片模块目前常用的错误：

- `MISSING_REQUEST_PARAM_ERROR`：参数缺失
- `FILM_NOT_EXIST`：影片不存在

### 1.7 `GlobalExceptionHandler`

文件位置：`src/main/java/com/springboot/web/common/GlobalExceptionHandler.java`

职责：

- 统一处理控制器里抛出的异常

处理规则：

- 如果抛出的是 `ServiceException`，就返回对应业务错误码和错误信息
- 如果抛出的是其他异常，就返回 `SYS_ERROR`

### 1.8 `GlobalResponseBodyHandler`

文件位置：`src/main/java/com/springboot/web/common/GlobalResponseBodyHandler.java`

职责：

- 统一处理响应体
- 如果控制器返回值不是 `CommonResult`，自动包装成 `CommonResult`

在当前 `FilmController` 中：

- 各接口本身已经返回 `CommonResult`
- 所以这里主要起兜底作用

## 2. FilmController 中的数据来源

`FilmController` 内部有一个成员变量：

```java
List<Film> list = new ArrayList<>();
```

它不是数据库，而是一个内存集合。

下面这段初始化代码会在控制器创建时往 `list` 中放入 3 条默认影片数据：

- `filmId = 1`
- `filmId = 2`
- `filmId = 3`

这意味着：

- 查询、修改、删除都基于这个 `list`
- 新增只是 `list.add(film)`
- 删除只是 `list.removeIf(...)`

## 3. 接口列表

`FilmController` 对外提供了这些接口：

- `GET /film/{id}`：按 id 查询影片
- `POST /film`：新增影片
- `PUT /film/{id}`：按路径参数修改影片
- `PUT /film?filmid=1`：按查询参数修改影片
- `DELETE /film/{id}`：删除影片
- `GET /film/queryByCondition`：按条件查询影片列表

## 4. 代码内部调用总流程

可以把一次请求理解成下面的流程：

1. 浏览器、Apifox、Postman 发起 HTTP 请求
2. Spring MVC 根据请求路径和请求方法匹配 `FilmController` 中的方法
3. Spring 自动完成参数绑定
4. `FilmController` 执行业务逻辑
5. 如果需要对象转换，使用 `BeanUtils.copyProperties(...)`
6. 正常情况下返回 `CommonResult`
7. 如果中途抛出 `ServiceException`，交给 `GlobalExceptionHandler` 处理
8. 最终由 Spring 把对象序列化成 JSON 响应给前端

可以概括为：

`HTTP请求 -> FilmController -> 内存list处理 -> CommonResult/异常处理 -> JSON响应`

## 5. 各接口详细流程

### 5.1 按 id 查询：`GET /film/{id}`

对应方法：`queryById(Integer id)`

流程：

1. Spring 从路径中取出 `id`
2. 进入 `queryById`
3. 在 `list` 中通过 `stream().filter(...)` 查找目标影片
4. 如果查不到，抛出 `new ServiceException(ServiceExceptionEnum.FILM_NOT_EXIST)`
5. 如果查到了，取出第一个 `Film`
6. 新建 `FilmVo`
7. 使用 `BeanUtils.copyProperties(film, filmVo)` 把字段复制过去
8. 用 `CommonResult<FilmVo>` 包装结果并返回

内部调用关系：

- `queryById()` -> `list.stream().filter(...).toList()`
- `queryById()` -> `BeanUtils.copyProperties(...)`
- `queryById()` -> `CommonResult.setData(...)`
- 异常时：`queryById()` -> `ServiceException` -> `GlobalExceptionHandler.serviceExceptionHandler()`

### 5.2 新增影片：`POST /film`

对应方法：`addOne(FilmDto filmDto)`

流程：

1. 前端发送 JSON 请求体
2. Spring 将 JSON 转成 `FilmDto`
3. 进入 `addOne`
4. 新建 `Film`
5. 使用 `BeanUtils.copyProperties(filmDto, film)`
6. 使用 `Random` 生成一个随机 `filmId`
7. 如果 `lastUpdate` 为空，则补当前时间
8. 调用 `list.add(film)` 保存到内存集合
9. 返回空的 `CommonResult<Void>`

内部调用关系：

- `addOne()` -> `BeanUtils.copyProperties(...)`
- `addOne()` -> `Random.nextInt(...)`
- `addOne()` -> `list.add(...)`
- `addOne()` -> `new CommonResult<>()`

### 5.3 修改影片：`PUT /film/{id}` 或 `PUT /film?filmid=1`

对应方法：`updateOne(Integer id, Integer filmId, FilmDto filmDto)`

这个接口支持两种传参方式：

- 路径参数：`/film/1`
- 查询参数：`/film?filmid=1`

代码里会先确定最终要修改的 id：

- 如果路径变量 `id` 不为空，优先用 `id`
- 否则使用查询参数 `filmid`

流程：

1. Spring 绑定路径参数或查询参数
2. Spring 将请求体 JSON 转成 `FilmDto`
3. 进入 `updateOne`
4. 计算最终 id：`targetId = id != null ? id : filmId`
5. 如果 `targetId` 为空，抛出参数缺失异常
6. 先用 `list.stream().anyMatch(...)` 判断影片是否存在
7. 如果不存在，抛出 `FILM_NOT_EXIST`
8. 如果存在，再用 `list.stream().filter(...).findFirst().ifPresent(...)` 找到影片
9. 在 `ifPresent` 代码块里逐个调用 `setXxx(...)` 更新字段
10. 如果 `lastUpdate` 没传，则自动设置成当前时间
11. 返回空的 `CommonResult<Void>`

内部调用关系：

- `updateOne()` -> 计算 `targetId`
- `updateOne()` -> `list.stream().anyMatch(...)`
- `updateOne()` -> `list.stream().filter(...).findFirst().ifPresent(...)`
- `updateOne()` -> 多个 `po.setXxx(...)`
- 异常时：`updateOne()` -> `ServiceException` -> `GlobalExceptionHandler`

### 5.4 删除影片：`DELETE /film/{id}`

对应方法：`deleteOne(Integer id)`

流程：

1. Spring 从路径中取出 `id`
2. 进入 `deleteOne`
3. 调用 `list.removeIf(f -> f.getFilmId().equals(id))`
4. 删除成功后返回 `CommonResult<Void>`

内部调用关系：

- `deleteOne()` -> `list.removeIf(...)`
- `deleteOne()` -> `new CommonResult<>()`

说明：

- 当前删除接口没有判断影片是否存在
- 即使没有删除到数据，也会返回成功

### 5.5 条件查询：`GET /film/queryByCondition`

对应方法：`queryByCondition(String title, String rating, int curPage, int pageSize)`

流程：

1. Spring 绑定查询参数
2. 进入 `queryByCondition`
3. 先按 `title` 条件过滤
4. 再按 `rating` 条件过滤
5. 过滤后的每个 `Film` 都会转换成 `FilmVo`
6. 最终把 `List<FilmVo>` 放进 `CommonResult`
7. 返回给前端

内部调用关系：

- `queryByCondition()` -> `list.stream()`
- `queryByCondition()` -> `.filter(...)`
- `queryByCondition()` -> `.map(...)`
- `map(...)` -> `BeanUtils.copyProperties(po, vo)`
- `queryByCondition()` -> `CommonResult.setData(listVo)`

说明：

- `curPage` 和 `pageSize` 参数目前只是接收了
- 代码中还没有真正做分页截取
- 所以现在返回的是“符合条件的全部数据”

## 6. 对象之间的调用关系

可以把 `film` 模块理解成下面这张关系图：

```text
前端请求
   |
   v
FilmController
   |---- 使用 FilmDto 接收请求体
   |---- 使用 Film 作为内存中的数据对象
   |---- 使用 FilmVo 作为返回对象
   |---- 使用 BeanUtils 做对象复制
   |---- 使用 CommonResult 统一返回
   |
   |---- 发生业务错误时抛出 ServiceException
   v
GlobalExceptionHandler
   |
   v
CommonResult 错误响应
```

## 7. 一次完整调用示例

### 示例 1：查询影片 `GET /film/1`

执行链路：

1. 请求进入 `/film/1`
2. Spring 路由到 `queryById(1)`
3. 在 `list` 中找到 `filmId = 1` 的影片
4. `Film -> FilmVo`
5. `FilmVo -> CommonResult`
6. 返回 JSON

### 示例 2：修改影片 `PUT /film?filmid=1`

请求体：

```json
{
  "title": "学院恐龙2",
  "description": "重制版剧情",
  "releaseYear": 2025,
  "languageId": 1,
  "originalLanguageId": 1,
  "rentalDuration": 7,
  "rentalRate": 4.99,
  "length": 95,
  "replacementCost": 25.99,
  "rating": "PG",
  "specialFeatures": "Trailers,Deleted Scenes"
}
```

执行链路：

1. 请求进入 `/film`
2. Spring 读取查询参数 `filmid=1`
3. Spring 把请求体转成 `FilmDto`
4. 进入 `updateOne(null, 1, filmDto)`
5. 代码得到 `targetId = 1`
6. 检查 `list` 中是否存在 `filmId = 1`
7. 找到后进入 `ifPresent(...)`
8. 把 `FilmDto` 中的值逐个 set 到目标 `Film`
9. 返回成功结果

## 8. 当前代码的特点和局限

这份代码适合学习 Spring MVC 基础流程，但它还是一个演示版本，主要有这些特点：

- 逻辑全部写在 Controller 中
- 没有 Service 层和 DAO 层
- 没有数据库，使用内存集合模拟数据
- 查询单条和修改时做了影片存在性校验
- 删除接口还没有做“影片不存在”的校验
- 条件查询没有真正分页
- `filmId` 是随机生成的，理论上可能重复

## 9. 后续可以怎么优化

如果以后继续完善，可以按下面方向重构：

1. 把 `FilmController` 中的业务逻辑抽到 `FilmService`
2. 把内存 `list` 换成数据库表
3. 把新增、修改、删除都做成统一的存在性校验
4. 给条件查询补上真正分页逻辑
5. 对请求参数增加校验注解，比如 `@NotNull`、`@NotBlank`
6. 给 Swagger 文档补齐接口说明和字段说明

