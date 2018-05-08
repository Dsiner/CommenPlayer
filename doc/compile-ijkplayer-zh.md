# Compile ijkplayer for Android

## ijkplayer编译

## 环境
-  虚拟机：VirtualBox
-  系统：Ubuntu

### 下载 [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
    下载好的JDK解压到 /home/用户名/Dev/App路径下 (这里的用户名为dsiner) 。解压好后，需要为JDK配置环境变量，右键，打开终端，
    在命令行中键入指令：
```xml
    sudo gedit ~/.bashrc
```

    在bashrc文件中加入如下内容：
	
```xml
    export JAVA_HOME=/home/dsiner/Dev/App/jdk1.8.0_131
    export JRE_HOME=${JAVA_HOME}/jre
    export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
    export PATH=${JAVA_HOME}/bin:$PATH
```

### 下载 [SDK](https://developer.android.com/studio/index.html)
    下载好的SDK同样解压到了 /home/用户名/Dev/App路径下。解压好后，继续为SDK配置环境变量：
```xml
    export ANDROID_SDK=/home/dsiner/Dev/App/android-sdk-linux
    export PATH=${PATH}:$ANDROID_SDK/tools:$ANDROID_SDK/platform-tools
```

### 下载 [NDK](https://dl.google.com/android/repository/android-ndk-r13-linux-x86_64.zip)
    下载好NDK解压在 /home/用户名/Dev/App路径下。解压好后，配置NDK配置环境变量：
```xml
    export ANDROID_NDK=/home/dsiner/Dev/App/android-ndk-r13
    export PATH=$PATH:ANDROID_NDK
```

###  使配置生效
    source /etc/profile

## 编译IJKPlayer
1. 安装git和yasm，在终端中分别键入：
    sudo apt-get install git
    sudo apt-get install yasm

2. clone ijkplayer 代码
```xml
    cd /home/dsiner/Dev/App
    git clone https://github.com/Bilibili/ijkplayer.git
    cd /home/dsiner/Dev/App/ijkplayer
    git checkout -B k0.7.6
```

3. 切换到config文件夹下，修改module-lite.sh文件
```xml
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-protocol=rtp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-protocol=tcp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-protocol=udp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-demuxer=rtsp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-demuxer=sdp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-demuxer=rtp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-demuxer=udp"
```

    上面的配置只为了支持rtsp和udp，其中rtp是将disable改成了enable。
	另外如果版本>=0.8.2并且没有下面的配置，你可能需要手动添加如下配置以避免编译报错。
	
```xml
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --disable-linux-perf"
```

    然后删除config文件夹下的module.sh，重新创建链接
	
```xml
    cd /home/dsiner/Dev/App/ijkplayer/config
    rm module.sh
    ln -s module-lite module.sh
```

    ps: module-lite.sh（If you prefer less codec/format for smaller binary size (by default)）
        module-default.sh（If you prefer more codec/format）
        module-lite-hevc.sh（If you prefer less codec/format for smaller binary size (include hevc function)）

4. 初始化openSSL和FFMPEG
```xml
    cd /home/dsiner/Dev/App/ijkplayer
    ./init-android-openssl.sh
    ./init-android.sh
```
    ps: 初始化过程似乎主要在下载ffmpeg的代码，代码包比较大，我这边下载速度基本稳定在10kb左右，挂了一晚上才下好......

5. 完成编译
```xml
    cd /home/dsiner/Dev/App/ijkplayer/android/contrib
    ./compile-openssl.sh clean
    ./compile-ffmpeg.sh clean
    ./compile-openssl.sh all
    ./compile-ffmpeg.sh all

    cd /home/dsiner/Dev/App/ijkplayer/android
    ./compile-ijk.sh all
```

6. 在Android项目中设置了rtsp的连接模式为：tcp
```java
    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "rtsp_transport", "tcp");
```

