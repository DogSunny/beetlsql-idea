# BeetlSqlX

![Build](https://github.com/DogSunny/beetlsql-idea/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/com.github.dogsunny.beetlsqlidea.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/com.github.dogsunny.beetlsqlidea.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [x] Get known with the [template documentation][template].
- [x] Verify the [pluginGroup](/gradle.properties), [plugin ID](/src/main/resources/META-INF/plugin.xml) and [sources package](/src/main/kotlin).
- [x] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html).
- [x] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.

## ToDo list
- [x] Solve the problem that `Could not autowire. No beans of'xxx' type found`
- [ ] sql文件与代码的相互跳转 `ctrl + 鼠标左键`
  - [x] 代码 -> md sql
  - [ ] 代码 -> sql sql
  - [ ] md sql-> 代码
  - [ ] sql sql-> 代码
- [ ] 代码中 `ctrl + q` 可看到sql
- [x] 提示相关 `ctrl + 空格`
  - [x] 在使用`SQLManager.select(sqlId)` sqlId获得提示
  - [x] 在使用`SqlId.of("aaa.bbb");` sqlId获得提示
  - [x] 在使用`SqlId.of("aaa", "bbb");` sqlId获得提示
- [ ] 色彩线上
- [ ] 在找不到对应的sqlId时提示，并提供快速修复功能
- [ ] 根据配置的 Profile 找到 beetlsql 的配置
- [ ] sqlId重构的支持
- [ ] 支持多数据源

<!-- Plugin description -->
A simple plugin for [BeetlSQL](https://gitee.com/xiandafu/beetlsql). Solve the problem that `Could not autowire. No beans of'xxx' type found` will be reported when intellij uses this ORM framework.
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
