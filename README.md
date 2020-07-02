# ViewTooltip

[![screen](https://raw.githubusercontent.com/florent37/ViewTooltip/master/medias/with_border.gif)](https://www.github.com/florent37/ViewTooltip)

```java
ViewTooltip
        .on(this, editText)
        .autoHide(true, 1000)
        .corner(30)
        .position(ViewTooltip.Position.RIGHT)
        .text("Right")
        .show();
```

<a href="https://goo.gl/WXW8Dc">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>


# Download

<a href='https://ko-fi.com/A160LCC' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

[ ![Download](https://api.bintray.com/packages/florent37/maven/viewtooltip/images/download.svg) ](https://bintray.com/florent37/maven/viewtooltip/_latestVersion)
```java
dependencies {
    implementation 'com.github.florent37:viewtooltip:(last version)'
}
```

# Methods

[![screen](https://raw.githubusercontent.com/florent37/ViewTooltip/master/medias/autoHide.gif)](https://www.github.com/florent37/ViewTooltip)

```java
ViewTooltip
        .on(this, editText)
        
        .autoHide(true / false, 1000)
        .clickToHide(true / false)
        
        .align(START / CENTER)
        
        .position(TOP / LEFT / RIGHT / BOTTOM)
        
        .text("The text")
        
        .textColor(Color.WHITE)
        .color(Color.BLACK)
        
        .corner(10)

        .arrowWidth(15)
        .arrowHeight(15)

        .distanceWithView(0)
        
        //change the opening animation
        .animation(new ViewTooltip.TooltipAnimation(){...})
        
        //listeners
        .onDisplay(new ViewTooltip.ListenerDisplay() {
            @Override
            public void onDisplay(View view) {
                
            }
        })
        .onHide(new ViewTooltip.ListenerHide() {
            @Override
            public void onHide(View view) {
                
            }
        })
        .show();
```

# Prevent view to not be outside screen

ViewTooltip will not allow to be outside of screen,
it will automatically adjust his size

[![screen](https://raw.githubusercontent.com/florent37/ViewTooltip/master/medias/clip_screen_large.gif)](https://www.github.com/florent37/ViewTooltip)

# History

# 1.2.0
- Compatible with AndroidX

# 1.1.7
- Set text as Int
- Added shadowColor

# 1.1.5
- Use Fragment V4
- Added aistanceWithView

# 1.1.4
- Added arrowWidth / arrowHeight

## 1.1.3
- Fix align bottom, text out of screen

## 1.1.1
- Added shadow

## 1.0.8 
- Clip tooltip to screen (top / bottom) 
- Text format HTML

## 1.0.6 
- Fix align 

## 1.0.5
- .customView()
- .remove()

## 1.0.3
- Clip tooltip to screen width

## 1.0.2
- Added corner

# Credits   

Author: Florent Champigny 

Blog : [http://www.tutos-android-france.com/](http://www.www.tutos-android-france.com/)

Fiches Plateau Moto : [https://www.fiches-plateau-moto.fr/](https://www.fiches-plateau-moto.fr/)

<a href="https://goo.gl/WXW8Dc">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

<a href="https://plus.google.com/+florentchampigny">
  <img alt="Follow me on Google+"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/gplus.png" />
</a>
<a href="https://twitter.com/florent_champ">
  <img alt="Follow me on Twitter"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/twitter.png" />
</a>
<a href="https://www.linkedin.com/in/florentchampigny">
  <img alt="Follow me on LinkedIn"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/linkedin.png" />
</a>


## Third Party Bindings

### React Native
You may now use this library with [React Native](https://github.com/facebook/react-native) via the module [here](https://github.com/prscX/react-native-tooltips)


License
--------

    Copyright 2017 Florent37, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
