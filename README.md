# CommenPlayer for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

CommenPlayer 是一个适用于 Android 平台的视频播放器，基于ijkplayer、vlc、Android MediaPlayer、ExoPlayer

## Branch
- [master](https://github.com/Dsiner/CommenPlayer)  - 支持格式最全，基于 `ijkplayer` ，支持ijkplayer、Android MediaPlayer、ExoPlayer切换
- [lite](https://github.com/Dsiner/CommenPlayer/tree/lite)  - lite，基于 `ijkplayer` ，支持ijkplayer、Android MediaPlayer、ExoPlayer切换
- [vlc](https://github.com/Dsiner/CommenPlayer/tree/vlc)  - 基于 `vlc` , 测试阶段

## Demo
<p>
   <img src="https://github.com/Dsiner/Resouce/blob/master/lib/CommenPlayer/commenplayer.gif" width="320" alt="Screenshot"/>
</p>

## Features
- [x] 支持本地和网络视频播放
- [x] 支持 M3U8、RTMP 、RTSP 协议的直播流媒体播放
- [x] 支持常见的音视频文件播放（MP4、mp3、flv 等）
- [x] 支持播放控制，进度显示
- [x] 支持手势滑动调节播放进度、亮度、声音
- [x] 支持画面尺寸切换（16：9，4：3，自适应，撑满等）
- [x] 支持横竖屏切换、全屏模式、列表播放、列表播放/全屏模式切换
- [x] 支持全屏沉浸式模式

## Usage
```xml
    <com.d.commenplayer.CommenPlayer
        android:id="@+id/player"
        android:layout_width="match_parent"
        android:layout_height="180dp" />
```

#### Operation
```java
        player.setLive(false);
        player.setOnNetListener(new OnNetListener() {
            @Override
            public void onIgnoreMobileNet() {
                ignoreNet = true;
            }
        }).setOnPlayerListener(new IPlayerListener() {
            @Override
            public void onLoading() {
                player.getControl().setState(ControlLayout.STATE_LOADING);
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                player.getControl().setState(ControlLayout.STATE_COMPLETION);
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (!ignoreNet && NetConstans.NET_STATUS == NetConstans.CONNECTED_MOBILE) {
                    player.pause();
                    player.getControl().setState(ControlLayout.STATE_MOBILE_NET);
                } else {
                    player.getControl().setState(ControlLayout.STATE_PREPARED);
                }
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                player.getControl().setState(ControlLayout.STATE_ERROR);
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {

            }
        });
        player.play(url);
```

#### Activity Lifecycle
```java
    Override onResume()、onPause()、onConfigurationChanged()、onBackPressed()、onDestroy()
```

More usage see [Demo](app/src/main/java/com/d/iplayer/MainActivity.java)

## Thanks
- [ijkplayer](https://github.com/Bilibili/ijkplayer)  -Video player based on ffplay
- [vlc-android-sdk](https://github.com/mrmaffen/vlc-android-sdk)  -Unofficial VLC Android SDK pushed to JCenter. Supported ABIs are armeabi-v7a, arm64-v8a, x86 and x86_64.
- [JiaoZiVideoPlayer](https://github.com/lipangit/JiaoZiVideoPlayer)
- [NiceVieoPlayer](https://github.com/xiaoyanger0825/NiceVieoPlayer)

## Licence

```txt
Copyright 2017 D

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
