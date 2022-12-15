 

#  Advance训练总文档

### 训练目标：

- 掌握对`hashCode,equals`方法的重写方式

### 考察内容：

现有若干学生和课程，需要根据选课和退课信息(以指令格式输入)建立学生和课程之间的映射关系。请阅读给定代码(详见第一单元Advance训练公共仓库内容)，并对代码进行补全(需要补全的地方已用`TODO`形式标注)，使得容器`recordSet`中不包含重复记录，**即对输入信息去重(学生编号`stuId`和课程编号`cId`均相同的记录视为重复记录)**

### 输入：

输入包含两类指令:

```java
//选课指令
select student_id student_name course_id course_name
//退课指令
unselect student_id student_name course_id course_name
```

### 输出：

去重后的选课信息记录

### 样例：

**输入：**

```
select 1 Bob 1 OO
select 1 Bob 1 OO
```

**输出：**

```
[Bob selects OO]
```

### 提交方式：

请提交补全后的代码至第一单元个人训练仓库（和json文件放至同一仓库即可）
