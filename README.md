# ViewTooltip

[![screen](https://raw.githubusercontent.com/florent37/ViewTooltip/master/medias/with_border.gif)](https://www.github.com/florent37/ViewTooltip)

```java
ViewTooltip
        .on(editText)
        .autoHide(true, 1000)
        .corner(30)
        .position(ViewTooltip.Position.RIGHT)
        .text("Right")
        .show();
```

<a target='_blank' rel='nofollow' href='https://app.codesponsor.io/link/iqkQGAc2EFNdScAzpwZr1Sdy/florent37/ViewTooltip'>
  <img alt='Sponsor' width='888' height='68' src='https://app.codesponsor.io/embed/iqkQGAc2EFNdScAzpwZr1Sdy/florent37/ViewTooltip.svg' />
</a>

# Download

<a href='https://ko-fi.com/A160LCC' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

[ ![Download](https://api.bintray.com/packages/florent37/maven/viewtooltip/images/download.svg) ](https://bintray.com/florent37/maven/viewtooltip/_latestVersion)
```java
dependencies {
    compile 'com.github.florent37:viewtooltip:1.0.8'
}
```

# Methods

[![screen](https://raw.githubusercontent.com/florent37/ViewTooltip/master/medias/autoHide.gif)](https://www.github.com/florent37/ViewTooltip)

```java
ViewTooltip
        .on(editText)
        
        .autoHide(true / false, 1000)
        .clickToHide(true / false)
        
        .align(START / CENTER)
        
        .position(TOP / LEFT / RIGHT / BOTTOM)
        
        .text("The text")
        
        .textColor(Color.WHITE)
        .color(Color.BLACK)
        
        .corner(10)
        
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

Author: Florent Champigny [http://www.florentchampigny.com/](http://www.florentchampigny.com/)

Blog : [http://www.tutos-android-france.com/](http://www.www.tutos-android-france.com/)

<a href="https://play.google.com/store/apps/details?id=com.github.florent37.florent.champigny">
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
