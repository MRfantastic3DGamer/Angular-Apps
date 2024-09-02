
With this app you can group your most used apps and access them with just one gesture whenever you want.

### Access
You can get this app on play store ```link will be awailable soon```

### History
>This app was initially supposed to be an android launcher.
>
>built with the scope creep kincking in, It got bigger and bigger.

>This is one part of that extremely bloated android launcher used as a forground service.

## Some fun visualizations

### Code Volume Visualization
![Visualization of the codebase](./diagram.svg)

### Project Structure Visualization
<!---BETTER_FILES_TREE-->
```
┏ Angular-Apps━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┣diagram.svg                                     ┃
┣settings.gradle.kts                             ┃
┣gradlew.bat                                     ┃
┣gradlew                                         ┃
┣better-tree                                     ┃
┣README.md                                       ┃
┣┏ app━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃
┃┣proguard-rules.pro                           ┃ ┃
┃┣┏ src━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃
┃┃┣┏ main━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃
┃┃┃┣g_ic_app-playstore.png                 ┃ ┃ ┃ ┃
┃┃┃┣┏ java━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃
┃┃┃┃┣┏ com━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┣┏ dhruv━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┣┏ angularapps━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣AnimatedFloat.kt           ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ apps━━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣AppManager.kt           ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣AppsIconsPositioning.kt ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ utils━━━━━━━━━━━━━┓      ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣DrawablePainter.kt ┃      ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━┛      ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ settings_app━━━━━━━━┓    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣┏ settings━━━━━┓     ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣SettingsVM.kt ┃     ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Settings.kt   ┃     ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━┛     ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣Home.kt              ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣SettingsArt.kt       ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣┏ groups━━━━━━━━━━━┓ ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣GroupsManager.kt  ┃ ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣GroupsEditorVM.kt ┃ ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣GroupIcons.kt     ┃ ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣GroupsEditor.kt   ┃ ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━┛ ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣SettingsApp.kt       ┃    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━┛    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ di━━━━━━━━━━┓            ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣AppModule.kt ┃            ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━┛            ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ ui━━━━━━━━━┓             ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣┏ theme━━━┓ ┃             ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Type.kt  ┃ ┃             ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Color.kt ┃ ┃             ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Theme.kt ┃ ┃             ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┗━━━━━━━━━┛ ┃             ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━┛             ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ views━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣SliderView.kt           ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣PositionedLayoutView.kt ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣AppsAreaView.kt         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣OverlayService.kt          ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣MainActivity.kt            ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ data━━━━━━━━━━━┓         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣UserPrefImpl.kt ┃         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣┏ models━━━━━┓  ┃         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Position.kt ┃  ┃         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Group.kt    ┃  ┃         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━┛  ┃         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣Breaks.kt       ┃         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣UserPref.kt     ┃         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━┛         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣MyApp.kt                   ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃
┃┃┃┣ic_flat_app-playstore.png              ┃ ┃ ┃ ┃
┃┃┃┣AndroidManifest.xml                    ┃ ┃ ┃ ┃
┃┃┃┣ic_app-playstore.png                   ┃ ┃ ┃ ┃
┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃
┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃
┃┣build.gradle.kts                             ┃ ┃
┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃
┣gradle.properties                               ┃
┣build.gradle.kts                                ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
```
