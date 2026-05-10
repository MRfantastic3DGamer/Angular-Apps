
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
┏ Angular-Apps━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┣README.md                                             ┃
┣settings.gradle.kts                                   ┃
┣┏ SS━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓                  ┃
┃┣┏ phone━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃                  ┃
┃┃┣Screenshot_20240813_233040.png ┃ ┃                  ┃
┃┃┣Screenshot_20240813_232525.png ┃ ┃                  ┃
┃┃┣Screenshot_20240813_232900.png ┃ ┃                  ┃
┃┃┣Screenshot_20240813_232437.png ┃ ┃                  ┃
┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃                  ┃
┃┣┏ Tablet━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃                  ┃
┃┃┣Screenshot_20240813_234323.png ┃ ┃                  ┃
┃┃┣Screenshot_20240813_234359.png ┃ ┃                  ┃
┃┃┣Screenshot_20240813_234429.png ┃ ┃                  ┃
┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃                  ┃
┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛                  ┃
┣better-tree                                           ┃
┣gradlew.bat                                           ┃
┣┏ dependency-graph-reports━━━━━━━━━┓                  ┃
┃┣android_cicd-build.json           ┃                  ┃
┃┣android_cicd-build.json.processed ┃                  ┃
┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛                  ┃
┣build.gradle.kts                                      ┃
┣gradlew                                               ┃
┣┏ app━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃
┃┣┏ src━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃
┃┃┣┏ main━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃
┃┃┃┣Feature graphic.png                          ┃ ┃ ┃ ┃
┃┃┃┣ic_flat_app-playstore.png                    ┃ ┃ ┃ ┃
┃┃┃┣ic_app-playstore.png                         ┃ ┃ ┃ ┃
┃┃┃┣g_ic_app-playstore.png                       ┃ ┃ ┃ ┃
┃┃┃┣┏ java━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃
┃┃┃┃┣┏ com━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┣┏ dhruv━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┣┏ angularapps━━━━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣MainActivity.kt                  ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ ui━━━━━━━━━━━━━━━━━━━━━┓       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣┏ components━━━━━━━━━━┓ ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Stepper.kt           ┃ ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣PhonePreviewFrame.kt ┃ ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣SectionHeader.kt     ┃ ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣SettingsListItem.kt  ┃ ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━┛ ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣┏ theme━━━━┓            ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Type.kt   ┃            ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Color.kt  ┃            ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Theme.kt  ┃            ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Shapes.kt ┃            ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┗━━━━━━━━━━┛            ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━┛       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ settings_app━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣SettingsApp.kt                ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣┏ groups━━━━━━━━━━━━━┓        ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣GroupsManager.kt    ┃        ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣GroupsEditorVM.kt   ┃        ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣GroupIcons.kt       ┃        ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣GroupsEditor.kt     ┃        ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣AutoGroupBuilder.kt ┃        ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━┛        ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣SettingsArt.kt                ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣┏ settings━━━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Settings.kt                ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣┏ editors━━━━━━━━━━━━━━━━┓ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┃┣RadiusEditors.kt        ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┃┣SliderPositionEditor.kt ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┃┣TouchOffsetEditor.kt    ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┃┣SliderSizeEditor.kt     ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣SettingsVM.kt              ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣Home.kt                       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ utils━━━━━━━━━━━━━┓            ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣DrawablePainter.kt ┃            ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━┛            ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣MyApp.kt                         ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ di━━━━━━━━━━┓                  ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣AppModule.kt ┃                  ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━┛                  ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ apps━━━━━━━━━━━━━━━━━━━┓       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣AppsIconsPositioning.kt ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣AppManager.kt           ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━┛       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣OverlayService.kt                ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ views━━━━━━━━━━━━━━━━━━┓       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣AppsAreaView.kt         ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣PositionedLayoutView.kt ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣SliderView.kt           ┃       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━┛       ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣┏ data━━━━━━━━━━━┓               ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣Breaks.kt       ┃               ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣UserPrefImpl.kt ┃               ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣UserPref.kt     ┃               ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┣┏ models━━━━━┓  ┃               ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Position.kt ┃  ┃               ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┣Group.kt    ┃  ┃               ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━┛  ┃               ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━┛               ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┣AnimatedFloat.kt                 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃ ┃
┃┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃ ┃
┃┃┃┣AndroidManifest.xml                          ┃ ┃ ┃ ┃
┃┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃ ┃
┃┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃ ┃
┃┣build.gradle.kts                                   ┃ ┃
┃┣proguard-rules.pro                                 ┃ ┃
┃┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛ ┃
┣gradle.properties                                     ┃
┣diagram.svg                                           ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
```
