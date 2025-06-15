# 🔧 JPA实体循环引用修复方案

## 📋 问题描述

在分配权限时出现 `StackOverflowError`，错误堆栈显示在 `Role.hashCode()` 和 `Permission.hashCode()` 之间无限循环调用。

### 错误信息
```
java.lang.StackOverflowError: null
	at java.base/java.util.AbstractSet.hashCode(AbstractSet.java:120)
	at org.hibernate.collection.spi.PersistentSet.hashCode(PersistentSet.java:413)
	at com.edu.bcu.entity.Role.hashCode(Role.java:10)
	at java.base/java.util.AbstractSet.hashCode(AbstractSet.java:124)
	at com.edu.bcu.entity.Permission.hashCode(Permission.java:10)
```

## 🎯 问题根因

1. **实体关系**: `Role` 和 `Permission` 之间存在多对多双向映射关系
2. **Lombok注解**: 两个实体都使用了 `@Data` 注解
3. **自动生成方法**: Lombok自动生成的 `hashCode()` 和 `equals()` 方法包含了所有字段
4. **循环调用**: 形成了以下调用链：

```
Role.hashCode()
├── permissions.hashCode()
    ├── Permission.hashCode()
        ├── roles.hashCode()
            └── Role.hashCode() ← 循环开始
```

## ✅ 解决方案

### 方案1: 使用@EqualsAndHashCode.Exclude（推荐）

```java
@Data
@EqualsAndHashCode(exclude = {"permissions"}) // 排除集合字段
@Entity
public class Role {
    // ... 其他字段
    private Set<Permission> permissions;
}

@Data
@EqualsAndHashCode(exclude = {"roles"}) // 排除集合字段
@Entity
public class Permission {
    // ... 其他字段
    private Set<Role> roles;
}
```

### 方案2: 手动重写equals和hashCode（当前使用）

```java
@Data
@EqualsAndHashCode(exclude = {"permissions"})
@Entity
public class Role {
    // ... 字段定义
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
```

## 🔍 修复要点

1. **排除关联字段**: 使用 `@EqualsAndHashCode.Exclude` 排除多对多关联的集合字段
2. **选择唯一标识**: 手动重写的方法只使用业务唯一标识字段（如id、name、code）
3. **保持一致性**: 确保equals和hashCode方法使用相同的字段组合
4. **避免null值**: 使用 `Objects.equals()` 和 `Objects.hash()` 处理null值

## 🎯 最佳实践

### 1. JPA实体equals/hashCode原则
- **持久化实体**: 仅使用业务唯一标识字段
- **避免集合字段**: 永远不要在equals/hashCode中包含集合字段
- **ID字段谨慎使用**: 如果ID是自动生成的，要考虑新实体的情况

### 2. 多对多关系处理
```java
// ✅ 正确：排除关联集合
@EqualsAndHashCode(exclude = {"permissions", "users"})

// ❌ 错误：包含关联集合
@EqualsAndHashCode // 会包含所有字段
```

### 3. 性能优化
```java
// ✅ 推荐：使用业务标识
@Override
public int hashCode() {
    return Objects.hash(code); // 使用唯一的业务字段
}

// ❌ 避免：使用可变字段
@Override
public int hashCode() {
    return Objects.hash(description); // description可能会变化
}
```

## 📊 修复验证

修复后，以下操作应该正常工作：
- ✅ 角色权限分配
- ✅ 权限角色查询
- ✅ 实体比较和集合操作
- ✅ Hibernate集合初始化

## 🚨 注意事项

1. **级联操作**: 确保级联配置正确，避免不必要的级联删除
2. **懒加载**: 保持集合的懒加载配置，避免N+1查询问题
3. **JSON序列化**: 可能需要使用 `@JsonIgnore` 避免JSON序列化时的循环引用

```java
@ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
@JsonIgnore // 避免JSON序列化循环引用
private Set<Role> roles;
```

## 📝 总结

通过排除关联集合字段并手动重写equals/hashCode方法，成功解决了JPA实体间的循环引用问题。这是处理多对多双向映射关系的标准做法。 