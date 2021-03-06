# BeetlSqlX

![Build](https://github.com/DogSunny/beetlsql-idea/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.dogsunny.beetlsqlidea.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.dogsunny.beetlsqlidea.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

[comment]: <> (## Template ToDo list)

[comment]: <> (- [x] Create a new [IntelliJ Platform Plugin Template][template] project.)

[comment]: <> (- [x] Get known with the [template documentation][template].)

[comment]: <> (- [x] Verify the [pluginGroup]&#40;/gradle.properties&#41;, [plugin ID]&#40;/src/main/resources/META-INF/plugin.xml&#41; and [sources package]&#40;/src/main/kotlin&#41;.)

[comment]: <> (- [x] Review the [Legal Agreements]&#40;https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html&#41;.)

[comment]: <> (- [x] [Publish a plugin manually]&#40;https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate&#41; for the first time.)

[comment]: <> (- [x] Set the [Deployment Token]&#40;https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html&#41;.)

[comment]: <> (- [x] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.)

<!-- Plugin description -->
A simple plugin for [BeetlSQL](https://gitee.com/xiandafu/beetlsql). Solve the problem that `Could not autowire. No beans of'xxx' type found` will be reported when intellij uses this ORM framework.
## Features
- [x] 解决Spring注入错误 `Could not autowire. No beans of'xxx' type found`
- [ ] 代码跳转 `ctrl + 鼠标左键`
  - [x] code -> markdown
  - [ ] code -> sql
  - [ ] markdown -> code
  - [ ] sql-> code
- [ ] 代码中 `ctrl + q` 可看到SQL
- [x] 提示相关 `ctrl + 空格`，需要优化，有概率误识别
  - [x] `SQLManager.select("aaa.bbb")` 
  - [x] `SqlId.of("aaa.bbb");`
  - [x] `SqlId.of("aaa", "bbb");`
  - [x] `sqlManager.update("aaa.bbb")`
  - [x] `sqlManager.insert("aaa.bbb")`
- [ ] 错误提示, 并提供快速修复
- [ ] 根据配置的 Profile 找到 beetlsql 的配置
- [ ] sqlId重构的支持
- [ ] 支持多数据源
- [ ] 右键实体类创建Mapper/Dao

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "beetlsql-idea"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/DogSunny/beetlsql-idea/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
