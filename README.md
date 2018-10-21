# CommenPlayer for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-9%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=9)
[![Readme](https://img.shields.io/badge/README-%E4%B8%AD%E6%96%87-brightgreen.svg)](https://github.com/Dsiner/CommenPlayer/blob/master/README-zh.md)

<a href="https://github.com/Dsiner/CommenPlayer" target="_blank"><p align="center"><img src="https://github.com/Dsiner/CommenPlayer/blob/master/logo/horizontal-color.png" alt="CommenPlayer" height="150px"></p></a>

> Video player for the Android platform based on ijkplayer, Android MediaPlayer, ExoPlayer / Vlc

## Branch
- [master](https://github.com/Dsiner/CommenPlayer)  - `Develop` branch，dependencies，less codec/format for smaller binary size，base on `ijkplayer` ，support ijkplayer、Android MediaPlayer、ExoPlayer
- [ijkplayer-lite-0.7.6 with .so file](https://github.com/Dsiner/CommenPlayer-ijkplayer/tree/ijk-lite-v0.7.6)  - `Release` branch，with .so file，less codec/format for smaller binary size，base on `ijkplayer` ，support ijkplayer、Android MediaPlayer、ExoPlayer
- [ijkplayer-lite-0.8.8 with .so file](https://github.com/Dsiner/CommenPlayer-ijkplayer/tree/ijk-lite-v0.8.8)  - `Release` branch，with .so file，less codec/format for smaller binary size，base on `ijkplayer` ，support ijkplayer、Android MediaPlayer、ExoPlayer
- [ijkplayer-default-0.7.6 with .so file](https://github.com/Dsiner/CommenPlayer-ijkplayer/tree/ijk-default-v0.7.6)  - `Release` branch，with .so file，more codec/format，base on `ijkplayer` ，support ijkplayer、Android MediaPlayer、ExoPlayer
- [ijkplayer-default-0.8.8 with .so file](https://github.com/Dsiner/CommenPlayer-ijkplayer/tree/ijk-default-v0.8.8)  - Release branch，with .so file，more codec/format，base on `ijkplayer` ，support ijkplayer、Android MediaPlayer、ExoPlayer
- [Vlc](https://github.com/Dsiner/CommenPlayer-Vlc)  - `Beta` branch，base on `Vlc`

## Demo
<p>
   <img src="https://github.com/Dsiner/Resouce/blob/master/lib/CommenPlayer/commenplayer.gif" width="320" alt="Screenshot"/>
</p>

## Features
- [x] Supports local and network video playback
- [x] Live streaming support for M3U8, RTMP, RTSP, UDP protocols
- [x] Supports common audio and video file playback (MP4, mp3, flv, etc.)
- [x] Support playback control, progress display
- [x] Support gestures to adjust playback progress, brightness, sound
- [x] Supports screen size switching (16:9, 4:3, adaptive, full, etc.)
- [x] Supports horizontal and vertical screen switching, full screen mode, list playback, list playback/full screen mode switching
- [x] Supports full screen immersive mode

## Usage
```xml
    <com.d.lib.commenplayer.CommenPlayer
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

More usage see [Demo](app/src/main/java/com/d/commenplayer/MainActivity.java)

## Latest Changes
- [Changelog.md](CHANGELOG.md)

## Contributors
- [Anhar Ismail](https://github.com/anharismail)  - Logo design contribution

## Thanks
- [ijkplayer](https://github.com/Bilibili/ijkplayer)  - Video player based on ffplay
- [vlc-android-sdk](https://github.com/mrmaffen/vlc-android-sdk)  - Unofficial VLC Android SDK pushed to JCenter. Supported ABIs are armeabi-v7a, arm64-v8a, x86 and x86_64.
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
