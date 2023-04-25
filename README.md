# UnityPrivacyAgreementTool

Unity游戏和应用的隐私协议

- GitHub：`https://github.com/ZhaoLiang-Git/PrivacyAgreement`
- 码云(GitEE)：`https://gitee.com/zhaoliangliang/UnityPrivacyAgreementTool`

## 接入指引

最新版本：[![jitpack](https://jitpack.io/v/ZhaoLiang-Git/PrivacyAgreement.svg)](https://jitpack.io/#gzu-liyujiang/Android_CN_OAID)
（[更新日志](/CHANGELOG.md)）

### 依赖配置

```groovy
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```

在Unity mainTemplate.gradle dependencies{}中直接使用如下依赖即可：

```groovy
dependencies {
    implementation 'com.github.ZhaoLiang-Git:PrivacyAgreement:<version>'
}
```