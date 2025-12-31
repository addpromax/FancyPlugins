---
icon: dot
order: 9
---

# 条件判断功能 (Conditional Features)

FancyDialogs 支持强大的条件判断系统,允许你根据玩家的权限和 PlaceholderAPI 变量来动态控制对话框元素的可见性和状态。

## 功能概述

### 支持的条件类型

#### 1. 权限判断
- `PERMISSION` - 检查玩家是否拥有指定权限节点
- `NO_PERMISSION` - 检查玩家是否没有指定权限节点

#### 2. PlaceholderAPI 判断
- `PAPI_EQUALS` - 检查占位符值是否等于指定值
- `PAPI_NOT_EQUALS` - 检查占位符值是否不等于指定值
- `PAPI_GREATER_THAN` - 检查占位符值是否大于指定值(数字比较)
- `PAPI_LESS_THAN` - 检查占位符值是否小于指定值(数字比较)
- `PAPI_GREATER_OR_EQUAL` - 检查占位符值是否大于或等于指定值(数字比较)
- `PAPI_LESS_OR_EQUAL` - 检查占位符值是否小于或等于指定值(数字比较)
- `PAPI_CONTAINS` - 检查占位符值是否包含指定字符串
- `PAPI_STARTS_WITH` - 检查占位符值是否以指定字符串开头
- `PAPI_ENDS_WITH` - 检查占位符值是否以指定字符串结尾

### 可应用条件的元素

1. **按钮 (Buttons)** - 控制按钮的可见性
2. **文本输入框 (Text Fields)** - 控制文本框的可见性
3. **选择框 (Select Fields)** - 控制选择框的可见性 + 每个选项的默认选中状态
4. **复选框 (Checkboxes)** - 控制可见性和初始勾选状态

## 使用方法

### 在 Java 代码中使用

#### 1. 按钮条件示例

```java
import com.fancyinnovations.fancydialogs.api.data.DialogButton;
import com.fancyinnovations.fancydialogs.api.data.condition.ConditionType;
import com.fancyinnovations.fancydialogs.api.data.condition.DialogCondition;

// 只有管理员可见的按钮
new DialogButton(
    "<color:#ff0000>Admin Panel</color>",
    "<color:#ff0000>Only admins can see this</color>",
    List.of(
        new DialogButton.DialogAction("message", "Opening admin panel...")
    ),
    List.of(new DialogCondition(ConditionType.PERMISSION, "server.admin"))
)

// 等级大于等于10的玩家可见的按钮
new DialogButton(
    "<color:#00ff00>Level 10+ Reward</color>",
    "<color:#00ff00>Claim your reward</color>",
    List.of(
        new DialogButton.DialogAction("console_command", "give {player} diamond 10")
    ),
    List.of(new DialogCondition(ConditionType.PAPI_GREATER_OR_EQUAL, "%player_level%", "10"))
)

// 没有VIP权限的玩家可见的按钮
new DialogButton(
    "<color:#ffaa00>Become VIP</color>",
    "<color:#ffaa00>Upgrade to VIP</color>",
    List.of(
        new DialogButton.DialogAction("message", "Visit our store to get VIP!")
    ),
    List.of(new DialogCondition(ConditionType.NO_PERMISSION, "server.vip"))
)
```

#### 2. 文本输入框条件示例

```java
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogTextField;

// 只有管理员可见的文本框
new DialogTextField(
    "admin_note",
    "<color:#ff0000>Admin Note</color>",
    1,
    "Enter note...",
    100,
    2,
    List.of(new DialogCondition(ConditionType.PERMISSION, "server.admin"))
)
```

#### 3. 选择框条件示例

```java
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogSelect;

// 等级大于等于5时可见的选择框
new DialogSelect(
    "class_choice",
    "<color:#ffaa00>Choose Your Class (Level 5+ required)</color>",
    1,
    List.of(
        new DialogSelect.Entry("warrior", "<color:#ff0000>Warrior</color>", true),
        new DialogSelect.Entry("mage", "<color:#0000ff>Mage</color>", false),
        new DialogSelect.Entry("archer", "<color:#00ff00>Archer</color>", false)
    ),
    List.of(new DialogCondition(ConditionType.PAPI_GREATER_OR_EQUAL, "%player_level%", "5"))
)

// 动态默认选项 - 根据PAPI值自动选择"开"或"关"
new DialogSelect(
    "toggle_status",
    "<color:#00ddff>Toggle Status</color>",
    2,
    List.of(
        // "开" 选项 - 当游戏模式为创造模式(1)时自动选中
        new DialogSelect.Entry(
            "on",
            "<color:#00ff00>✓ 开</color>",
            false,
            List.of(new DialogCondition(ConditionType.PAPI_EQUALS, "%player_gamemode%", "1"))
        ),
        // "关" 选项 - 当游戏模式为生存模式(0)时自动选中
        new DialogSelect.Entry(
            "off",
            "<color:#ff0000>✗ 关</color>",
            false,
            List.of(new DialogCondition(ConditionType.PAPI_EQUALS, "%player_gamemode%", "0"))
        )
    )
)
```

!!!tip 选择框动态默认选项
每个选项都可以设置 `selectedConditions`,当条件满足时该选项会被自动选中。这对于创建开关、状态指示器等场景非常有用!
!!!

#### 4. 复选框条件示例

```java
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogCheckbox;

// 根据VIP权限自动勾选的复选框
new DialogCheckbox(
    "vip_status",
    "<color:#ffd700>VIP Status</color>",
    1,
    false, // 默认未勾选
    null, // 无可见性条件,所有玩家可见
    List.of(new DialogCondition(ConditionType.PERMISSION, "server.vip")) // 有VIP权限时自动勾选
)

// 只有非封禁玩家可见的复选框
new DialogCheckbox(
    "agree_rules",
    "<color:#00ff00>I agree to server rules</color>",
    2,
    false,
    List.of(new DialogCondition(ConditionType.PAPI_NOT_EQUALS, "%player_is_banned%", "yes")), // 可见性条件
    null // 无勾选条件
)
```

### 在 JSON 文件中使用

虽然当前版本主要支持 Java API,但你可以通过自定义序列化器来支持 JSON 格式。以下是建议的 JSON 结构:

```json
{
  "id": "my_conditional_dialog",
  "title": "Conditional Dialog",
  "canCloseWithEscape": true,
  "body": [
    { "text": "This dialog has conditional elements" }
  ],
  "inputs": {
    "textFields": [
      {
        "key": "admin_input",
        "label": "Admin Input",
        "placeholder": "Enter...",
        "maxLength": 50,
        "maxLines": 1,
        "visibilityConditions": [
          {
            "type": "PERMISSION",
            "value": "server.admin"
          }
        ]
      }
    ],
    "checkboxes": [
      {
        "key": "vip_checkbox",
        "label": "VIP Status",
        "order": 1,
        "initial": false,
        "checkedConditions": [
          {
            "type": "PERMISSION",
            "value": "server.vip"
          }
        ]
      }
    ]
  },
  "buttons": [
    {
      "label": "Admin Button",
      "tooltip": "Only for admins",
      "actions": [
        {
          "name": "message",
          "data": "Admin action!"
        }
      ],
      "visibilityConditions": [
        {
          "type": "PERMISSION",
          "value": "server.admin"
        }
      ]
    },
    {
      "label": "Level 10+ Reward",
      "tooltip": "For level 10+",
      "actions": [
        {
          "name": "console_command",
          "data": "give {player} diamond 10"
        }
      ],
      "visibilityConditions": [
        {
          "type": "PAPI_GREATER_OR_EQUAL",
          "value": "%player_level%",
          "compareValue": "10"
        }
      ]
    }
  ]
}
```

## 多条件组合

所有条件都使用 **AND 逻辑** - 这意味着所有条件必须同时满足才会显示元素。

```java
// 必须同时拥有两个权限才能看到按钮
new DialogButton(
    "Super Admin",
    "Requires both permissions",
    List.of(
        new DialogButton.DialogAction("message", "You are a super admin!")
    ),
    List.of(
        new DialogCondition(ConditionType.PERMISSION, "server.admin"),
        new DialogCondition(ConditionType.PERMISSION, "server.superadmin")
    )
)

// 必须等级>=10 且 拥有VIP权限
new DialogButton(
    "VIP Level 10+ Reward",
    "Special reward",
    List.of(
        new DialogButton.DialogAction("console_command", "give {player} diamond_block 1")
    ),
    List.of(
        new DialogCondition(ConditionType.PAPI_GREATER_OR_EQUAL, "%player_level%", "10"),
        new DialogCondition(ConditionType.PERMISSION, "server.vip")
    )
)
```

## 完整示例

服务器启动后会自动创建一个名为 `conditional_dialog_example` 的示例对话框,你可以通过以下命令打开:

```
/dialog open conditional_dialog_example
```

这个示例展示了:
- ✅ 基于权限的按钮可见性
- ✅ 基于 PlaceholderAPI 的输入框可见性
- ✅ 基于条件的复选框自动勾选
- ✅ **选择框动态默认选项(根据游戏模式自动选择"开"或"关")**
- ✅ 多种条件类型的使用

## 常见应用场景

### 1. VIP 专属功能
```java
List.of(new DialogCondition(ConditionType.PERMISSION, "server.vip"))
```

### 2. 等级限制
```java
List.of(new DialogCondition(ConditionType.PAPI_GREATER_OR_EQUAL, "%player_level%", "50"))
```

### 3. 新手引导
```java
List.of(new DialogCondition(ConditionType.NO_PERMISSION, "server.veteran"))
```

### 4. 区域限制
```java
List.of(new DialogCondition(ConditionType.PAPI_EQUALS, "%player_world%", "spawn"))
```

### 5. 金币要求
```java
List.of(new DialogCondition(ConditionType.PAPI_GREATER_OR_EQUAL, "%vault_eco_balance%", "1000"))
```

### 6. 职业系统
```java
List.of(new DialogCondition(ConditionType.PAPI_EQUALS, "%player_class%", "warrior"))
```

### 7. 开关状态显示(动态选项)
```java
// 根据PAPI值显示当前状态
new DialogSelect.Entry(
    "on",
    "开启",
    false,
    List.of(new DialogCondition(ConditionType.PAPI_EQUALS, "%setting_value%", "1"))
)
```

### 8. PVP模式切换(动态选项)
```java
// PVP开启时显示"开启",关闭时显示"关闭"
List.of(
    new DialogSelect.Entry("enabled", "PVP: 开启", false,
        List.of(new DialogCondition(ConditionType.PAPI_EQUALS, "%player_pvp_enabled%", "true"))),
    new DialogSelect.Entry("disabled", "PVP: 关闭", false,
        List.of(new DialogCondition(ConditionType.PAPI_EQUALS, "%player_pvp_enabled%", "false")))
)
```

## 注意事项

1. **PlaceholderAPI 依赖**: 使用 PAPI 条件时,请确保安装了 PlaceholderAPI 插件和相应的扩展
2. **数字比较**: 对于数字比较(GREATER_THAN, LESS_THAN等),如果值无法解析为数字,条件会返回 false
3. **字符串比较**: 字符串比较(CONTAINS, STARTS_WITH, ENDS_WITH)不区分大小写
4. **AND 逻辑**: 多个条件使用 AND 逻辑,所有条件必须同时满足
5. **性能考虑**: 条件评估在每次打开对话框时进行,频繁的条件检查可能影响性能

## 调试技巧

如果条件判断不符合预期:

1. 检查权限节点是否正确
2. 使用 `/papi parse me %placeholder%` 命令测试 PlaceholderAPI 占位符的值
3. 查看服务器日志中的 FancyDialogs 调试信息
4. 确保条件类型与值匹配(例如数字比较需要数字值)

## 相关链接

- [JSON Schema](json-schema.md) - 对话框 JSON 格式说明
- [Getting Started](../getting-started.md) - 入门指南
- [PlaceholderAPI Wiki](https://github.com/PlaceholderAPI/PlaceholderAPI/wiki) - PlaceholderAPI 文档
