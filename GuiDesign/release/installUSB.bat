set package=com.otot.mikve
set fileName=GeneralMikve.apk
adb disconnect
adb uninstall %package%.general
adb install -r %fileName%
pause
adb shell am start -S %package%.general/%package%.activities.SplashActivity