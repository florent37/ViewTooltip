# ViewTooltip

```java
ViewTooltip
        .on(editText)
        .autoHide(true, 1000)
        .position(ViewTooltip.Position.RIGHT)
        .text("Right")
        .show();
```

# Download

[ ![Download](https://api.bintray.com/packages/florent37/maven/viewtooltip/images/download.svg) ](https://bintray.com/florent37/maven/viewtooltip/_latestVersion)
```java
dependencies {
    compile 'com.github.florent37:viewtooltip:1.0.0'
}
```

# Methods

```java
ViewTooltip
        .on(editText)
        
        .autoHide(true / false, 1000)
        .clickToHide(true / false)
        
        .position(TOP / LEFT / RIGHT / BOTTOM)
        
        .text("The text")
        
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


# Credits   

Author: Florent Champigny [http://www.florentchampigny.com/](http://www.florentchampigny.com/)

Blog : [http://www.tutos-android-france.com/](http://www.www.tutos-android-france.com/)

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
