set files=/system/lib/libconscrypt_jni.so 		 /system/lib/libdpfj.so 		 /system/lib/libdpfpdd.so 		 /system/lib/libdpfpdd5000.so 		 /system/lib/libdpfpdd7k.so 		 /system/lib/libdpfpdd_4k.so  		 /system/lib/libdpfpdd_ptapi.so 		 /system/lib/libdpfr6.so 		 /system/lib/libdpfr7.so 		 /system/lib/libdpuareu_jni.so 		 /system/lib/libftrSDKAndroidJni.so 		 /system/lib/libftrScanAPI.so 		 /system/lib/libftrapi.so 		 /system/lib/libnative-lib.so 		 /system/lib/libserial_port.so 		 /system/lib/libtfm.so 		 /system/lib/libusb-1.0.so

adb disconnect
set /p ip="Enter Device's IP: "
adb kill-server
adb connect %ip%


rem FOR ANDROID  5 REMOVE
adb shell "su -c mount -o rw,remount /system"
adb shell "su -c cp -rf /system/priv-app/com.otot.mikve.general-1/ /data/app/com.otot.mikve.general-1"
adb shell "su -c rm -rf /system/priv-app/com.otot.mikve.general-1"
adb shell "su -c rm -rf /data/app/com.otot.mikve.general-1"
adb shell "su -c rm %files%"
adb shell "su -c mount -o ro,remount /system"
adb uninstall com.otot.mikve.general
pause
adb reboot
 