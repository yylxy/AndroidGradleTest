Gradle 是Android 主要的编译工具，学习Android 了解Grdle是非常必要的。

#### 对于一个gradle 项目，最基础的文件配置如下：
![image.png](https://upload-images.jianshu.io/upload_images/12693685-c6227a1405a4c10e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


* 一个项目有一个setting.gradle、包括一个顶层的 build.gradle文件、每个Module 都有自己的一个build.gradle文件。
* setting.gradle:这个 setting 文件定义了哪些module 应该被加入到编译过程，这个文件的代码在初始化阶段就会被执行。
* 顶层的build.gradle:顶层的build.gradle文件的配置最终会被应用到所有项目中。

#### Gradle 的编译周期
* **初始化阶段**：创建 Project 对象，如果有多个build.gradle，也会创建多个project.

* **配置阶段**：在这个阶段，会执行所有的编译脚本，同时还会创建project的所有的task，为后一个阶段做准备。

* **执行阶段**：在这个阶段，gradle 会根据传入的参数决定如何执行这些task,真正action的执行代码就在这里.

```gradle
buildscript {
    ext.kotlin_version = '1.2.50'
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.3'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```
**buildscript**：定义了 Android 编译工具的类路径。repositories中,jCenter是一个著名的 Maven 仓库。

**allprojects**:中定义的属性会被应用到所有 moudle 中，但是为了保证每个项目的独立性，我们一般不会在这里面操作太多共有的东西。

**每个项目单独的 build.gradle**：针对每个moudle 的配置，如果这里的定义的选项和顶层build.gradle定义的相同，后者会被覆盖。典型的 配置内容如下：

```gradle
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'

android {
    compileSdkVersion 27
    defaultConfig {
        applicationId "com.example.lyxs9.androidthreaddemo"
        minSdkVersion 19
        targetSdkVersion 27
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation"org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version"
    implementation 'com.android.support:appcompat-v7:27.1.1'
    implementation 'com.android.support.constraint:constraint-layout:1.1.2'
    implementation 'com.android.support:design:27.1.1'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
}
```
* **apply plugin**:第一行代码应用了Android 程序的gradle插件.

* **android**:关于android 的所有特殊配置都在这里，这就是又我们前面的声明的 plugin 提供的。

* **defaultConfig**就是程序的默认配置，注意，如果在AndroidMainfest.xml里面定义了与这里相同的属性，会以这里的为主。

* **buildTypes**:定义了编译类型，针对每个类型我们可以有不同的编译配置，不同的编译配置对应的有不同的编译命令。默认的有debug、release 的类型。

* **dependencies**:是属于gradle 的依赖配置。它定义了当前项目需要依赖的其他库。



#BuildConfig
这个类就是由gradle 根据 配置文件生成的。为什么gradle 可以直接生成一个Java 字节码类，这就得益于我们的 gradle 的编写语言是Groovy, Groovy 是一种 JVM 语言，JVM 语言的特征就是，虽然编写的语法不一样，但是他们最终都会编程 JVM 字节码文件。同是JVM 语言的还有 Scala,Kotlin 等等。
这个功能非常强大，我们可以通过在这里设置一些key-value对

#`在BuildConfig 添加字段`
```gradle
       /**
         * buildConfigField:配置字段
         * "String"：类型
         * "MY_VERSION_NAME"：名称
         * "\"My_App\""：内容
         * */
        buildConfigField "String", "MY_VERSION_NAME", "\"My_App\""

        //引用内容
        buildConfigField "String", "PAY_VIEW_VERSION_NAME", "\"${MY_VERSION_NAME}\""
```
在项目中的build.gradle文件添加
```gradle
    ext{
    MY_VERSION_NAME="8888888"
}
```
**获取方法**
![image.png](https://upload-images.jianshu.io/upload_images/12693685-1f79b84938569246.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


## Repositories

Repositories 就是代码仓库，这个相信大家都知道，我们平时的添加的一些 dependency 就是从这里下载的，Gradle 支持三种类型的仓库：Maven,Ivy和一些静态文件或者文件夹。在编译的**执行阶段**，gradle 将会从仓库中取出对应需要的依赖文件，当然，gradle 本地也会有自己的缓存，不会每次都去取这些依赖。

gradle 支持多种 Maven 仓库，一般我们就是用共有的`jCenter`就可以了。
有一些项目，可能是一些公司私有的仓库中的，这时候我们需要手动加入仓库连接：

[![Android Gradle 完整指南](http://upload-images.jianshu.io/upload_images/12693685-1653f6cd5e6fa5b4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://images2015.cnblogs.com/blog/909970/201609/909970-20160914124057977-1657791588.png) 

如果仓库有密码，也可以同时传入用户名和密码

[![Android Gradle 完整指南](http://upload-images.jianshu.io/upload_images/12693685-fd0db3d90ae7eec4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://images2015.cnblogs.com/blog/909970/201609/909970-20160914124058430-2105639217.png) 

我们也可以使用相对路径配置本地仓库，我们可以通过配置项目中存在的静态文件夹作为本地仓库：

[![Android Gradle 完整指南](http://upload-images.jianshu.io/upload_images/12693685-266326d07a52c59d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://images2015.cnblogs.com/blog/909970/201609/909970-20160914124058883-734641533.png) 

## Dependencies

我们在引用库的时候，每个库名称包含三个元素：`组名:库名称:版本号`,如下：

[![Android Gradle 完整指南](http://upload-images.jianshu.io/upload_images/12693685-92727bd2e5b5fbfc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://images2015.cnblogs.com/blog/909970/201609/909970-20160914124059664-1859639105.png) 

如果我们要保证我们依赖的库始终处于最新状态，我们可以通过添加通配符的方式，比如：

[![Android Gradle 完整指南](http://upload-images.jianshu.io/upload_images/12693685-906cb0f79e30bb29.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://images2015.cnblogs.com/blog/909970/201609/909970-20160914124100211-1020745240.png) 

**但是我们一般不要这么做，这样做除了每次编译都要去做网络请求查看是否有新版本导致编译过慢外，最大的弊病在于我们使用过的版本很很困难是测试版，性能得不到保证，所以，在我们引用库的时候一定要指名依赖版本。**


### Local dependencies

File dependencies

通过`files()`方法可以添加文件依赖，如果有很多jar文件，我们也可以通过`fileTree()`方法添加一个文件夹，除此之外，我们还可以通过通配符的方式添加，如下：

[![Android Gradle 完整指南](http://upload-images.jianshu.io/upload_images/12693685-50909d94b299561c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://images2015.cnblogs.com/blog/909970/201609/909970-20160914124100664-1097531416.png) 

Native libraries

配置本地 `.so`库。在配置文件中做如下配置，然后在对应位置建立文件夹，加入对应平台的`.so`文件。

[![Android Gradle 完整指南](http://upload-images.jianshu.io/upload_images/12693685-0e7bf08ad7a41586.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://images2015.cnblogs.com/blog/909970/201609/909970-20160914124101289-1691014994.png) 

文件结构如下：

[![Android Gradle 完整指南](http://upload-images.jianshu.io/upload_images/12693685-bd676e46845a8d88.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://images2015.cnblogs.com/blog/909970/201609/909970-20160914124101898-1340716535.png) 

Library projects

如果我们要写一个library项目让其他的项目引用，我们的bubild.gradle的plugin 就不能是andrid plugin了，需要引用如下plugin

```
apply plugin: 'com.android.library'
```

引用的时候在setting文件中`include`即可。

如果我们不方便直接引用项目，需要通过文件的形式引用，我们也可以将项目打包成`aar`文件，注意，这种情况下，我们在项目下面新建`arrs`文件夹，并在build.gradle 文件中配置 仓库：

[![Android Gradle 完整指南](http://upload-images.jianshu.io/upload_images/12693685-e981435c5d2076a8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://images2015.cnblogs.com/blog/909970/201609/909970-20160914124102445-449366357.png) 

当需要引用里面的某个项目时，通过如下方式引用：

[![Android Gradle 完整指南](http://upload-images.jianshu.io/upload_images/12693685-b5b32bec6a2599f0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://images2015.cnblogs.com/blog/909970/201609/909970-20160914124103055-16543983.png)

#引用配置文件
```gradle

//网络地址（行程地址）
apply from: 'http://gitlab.ops.*****.so/android_cashier/mvn-repo/raw/oldDev/config.gradle'

//本机地址（绝对路径）
apply from: file('D:\\MyAndroidStudioProjects\\****\\*****\\mvn\\mvn-repo\\mvn-repo\\config.gradle')

//在同一个包下（相对路径）
apply from: file('gradle-mvn-push.gradle')


```

###Android Studio3.xx新的依赖方式 implementation、api、compileOnly详解

####implementation
只能在内部使用此模块，比如我在一个libiary中使用implementation依赖了gson库，然后我的主项目依赖了libiary，那么，我的主项目就无法访问gson库中的方法。这样的好处是编译速度会加快，推荐使用implementation的方式去依赖，如果你需要提供给外部访问，那么就使用api依赖即可

####compile（api）
这种是我们最常用的方式，使用该方式依赖的库将会参与编译和打包。 
当我们依赖一些第三方的库时，可能会遇到com.android.support冲突的问题，就是因为开发者使用的compile依赖的com.android.support包，而他所依赖的包与我们本地所依赖的com.android.support包版本不一样，所以就会报All com.android.support libraries must use the exact same version specification (mixing versions can lead to runtime crashes这个错误。

#### provided（compileOnly）

**只在编译时有效，不会参与打包** 
可以在自己的moudle中使用该方式依赖一些比如com.android.support，gson这些使用者常用的库，避免冲突。

#### apk（runtimeOnly）
只在生成apk的时候参与打包，编译时不会参与，很少用。

#### testCompile（testImplementation）
testCompile 只在单元测试代码的编译以及最终打包测试apk时有效。

#### debugCompile（debugImplementation）
debugCompile 只在debug模式的编译和最终的debug apk打包时有效

#### releaseCompile（releaseImplementation）
Release compile 仅仅针对Release 模式的编译和最终的Release apk打包。



[博客地址](https://www.jianshu.com/p/deb7278e48a1)


