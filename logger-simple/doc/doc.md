```yaml
mybatis:
  param:
    set:
      sql:
        log:
          open: true
          config:
            # 采用什么方式刷新 predicate 配置 默认nacos
            refresher: NACOS
          predicate:
            # 判断的方式 默认为 or
            predictType: OR
            # 是否全开 默认false
            allOpen: false
            # 精确匹配哪些sqlId
            sqlIds:
              - com.biaoguoworks.dao.UserDao.getOne1
              - com.biaoguoworks.dao.UserDao.update
              - com.biaoguoworks.dao.UserDao.insert1
            # 匹配sqlId的前缀
            sqlIdPrefix:
              - com.biaoguoworks.dao
```