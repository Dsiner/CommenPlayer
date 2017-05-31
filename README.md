# CommenPlayer for Android

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## Demo
<p>
   <img src="https://github.com/Dsiner/CommenPlayer/blob/master/screenshot/screenshot.gif" width="320" alt="Screenshot"/>
</p>

## Usage
```xml
    <com.d.commenplayer.commen.CommenPlayer
        android:id="@+id/player"
        android:layout_width="match_parent"
        android:layout_height="180dp" />
```

#### Operation
```java
        //设置是否是直播模式
        player.setLive(false);
        //setListener
        player.setListenNetChange(true).setNetChangeListener(new OnNetChangeListener() {
            @Override
            ...
        }).setOnPlayListener(new IPlayListener() {
            @Override
            ...
        });
        //播放
        player.play(url1);
```

#### Activity Lifecycle
```java
    @Override
    protected void onResume() {
        player.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        player.onPause();
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        player.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (player.onBackPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        player.onDestroy();
        super.onDestroy();
    }
```


More usage see [Demo](app/src/main/java/com/d/iplayer/MainActivity.java)


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
