```yaml
mybatis:
  param:
    set:
      sql:
        log:
          open: true
          config:
            refresher: NACOS
          predicate:
            allOpen: true
            sqlIds:
              - com.biaoguoworks.dao.UserDao.getOne
              - com.biaoguoworks.dao.UserDao.getById
```