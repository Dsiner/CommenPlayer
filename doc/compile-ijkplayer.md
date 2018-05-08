# Compile ijkplayer for Android

## ijkplayer compilation

## Build Environment
-  Virtual Machine：VirtualBox
-  System：Ubuntu

### Download [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

Download the good JDK extract to /home/username/Dev/App path (where the user name is dsiner). After decompression, you need to configure environment variables for the JDK, right-click, open the terminal,Type the command on the command line:
```xml
    sudo gedit ~/.bashrc
```

Add the following to the bashrc file:
```xml
    export JAVA_HOME=/home/dsiner/Dev/App/jdk1.8.0_131
    export JRE_HOME=${JAVA_HOME}/jre
    export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
    export PATH=${JAVA_HOME}/bin:$PATH
```

### Download [SDK](https://developer.android.com/studio/index.html)

The downloaded SDK is also extracted to the /home/username/Dev/App path. After decompression, continue to configure the SDK environment variables:
```xml
    export ANDROID_SDK=/home/dsiner/Dev/App/android-sdk-linux
    export PATH=${PATH}:$ANDROID_SDK/tools:$ANDROID_SDK/platform-tools
```

### Download [NDK](https://dl.google.com/android/repository/android-ndk-r13-linux-x86_64.zip)

Download the NDK to extract the /home/username/Dev/App path. After decompression, configure the NDK configuration environment variable:
```xml
    export ANDROID_NDK=/home/dsiner/Dev/App/android-ndk-r13
    export PATH=$PATH:ANDROID_NDK
```

### Make the configuration effective
```xml
    source /etc/profile
```

## Compiling ijkplayer

1. Install git and yasm and type in the terminal:
```xml
    sudo apt-get install git
    sudo apt-get install yasm
```

2. Clone ijkplayer repertory
```xml
    cd /home/dsiner/Dev/App
    git clone https://github.com/Bilibili/ijkplayer.git
    cd /home/dsiner/Dev/App/ijkplayer
    git checkout -B k0.7.6
```

3. Switch to the config folder and modify the module-lite.sh file
```xml
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-protocol=rtp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-protocol=tcp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-protocol=udp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-demuxer=rtsp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-demuxer=sdp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-demuxer=rtp"
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --enable-demuxer=udp"
```

ps: 
The above configuration is to support rtsp and udp. Where rtp is changed from disable to enable.
If version is greater than 0.8.2 and does not have this configuration, you need to add to avoid compiling error:

```xml
    export COMMON_FF_CFG_FLAGS="$COMMON_FF_CFG_FLAGS --disable-linux-perf"
```

Then delete the module.sh in the config folder and recreate the link

```xml
    cd /home/dsiner/Dev/App/ijkplayer/config
    rm module.sh
    ln -s module-lite module.sh
```

ps: 
module-lite.sh（If you prefer less codec/format for smaller binary size (by default)）
module-default.sh（If you prefer more codec/format）
module-lite-hevc.sh（If you prefer less codec/format for smaller binary size (include hevc function)）

4. Initialize openSSL and FFMPEG
```xml
    cd /home/dsiner/Dev/App/ijkplayer
    ./init-android-openssl.sh
    ./init-android.sh
```

ps: 
The initialization process seems to be mainly download ffmpeg code, the code package is relatively large.

5. Complete the compilation
```xml
    cd /home/dsiner/Dev/App/ijkplayer/android/contrib
    ./compile-openssl.sh clean
    ./compile-ffmpeg.sh clean
    ./compile-openssl.sh all
    ./compile-ffmpeg.sh all

    cd /home/dsiner/Dev/App/ijkplayer/android
    ./compile-ijk.sh all
```

6. The rtsp connection mode is set in the Android project：tcp (Optional)
```java
    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "rtsp_transport", "tcp");
```

