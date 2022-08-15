
rem LIST FOR ANDROID  5
set files=libbarcode_wdc.so libdpfj.so libdpfpdd.so libdpfpdd5000.so libdpfpdd7k.so libdpfpdd_4k.so libdpfpdd_ptapi.so libdpfr6.so libdpfr7.so libdpuareu_jni.so libftrSDKAndroidJni.so libftrScanAPI.so libftrapi.so libnative-lib.so libserial_port.so libtfm.so libusb-1.0.so 



rem LIST FOR ANDROID  4
rem set files=/system/lib/libbarcode_wdc.so /system/lib/libconscrypt_jni.so /system/lib/libftrSDKAndroidJni.so /system/lib/libftrScanAPI.so /system/lib/libftrapi.so /system/lib/libnative-lib.so /system/lib/libserial_port.so /system/lib/libusb-1.0.so
adb install -r GeneralMikve.apk

adb shell "su -c mount -o rw,remount /system"
adb shell "su -c cp -R /data/app/com.otot.mikve.general-1/ /system/priv-app/com.otot.mikve.general-1"
adb shell "su -c rm -rf /data/app/com.otot.mikve.general-1"
adb shell "su -c chmod 644 /system/priv-app/com.otot.mikve.general-1/base.apk"
adb shell "su -c chown system:system /system/priv-app/com.otot.mikve.general-1/base.apk"
adb shell "su -c cp -R /system/priv-app/com.otot.mikve.general-1/lib/arm/*.*  /system/lib"

adb shell "su -c cd /system/lib/"
adb shell "su -c chown system:system  %files%"
adb shell "su -c chmod -R 644  %files%"
adb shell "su -c mount -o ro,remount /system"


adb shell "su -c mount -o ro,remount /system"

pause

 ls /system/priv-app/com.otot.mikve.general-1/lib/arm/ | busybox head -40
 adb shell "ls /system/priv-app/com.otot.mikve.general-1/lib/arm/ | busybox head -40"

 
 hell@g4418:/ $ exit

:\ANDROID_PROJECTS\MIKVE-V3.1\GeneralMikve\release>set ss=heoold

:\ANDROID_PROJECTS\MIKVE-V3.1\GeneralMikve\release>echo %ss%
eoold

(for /f %%G in ('adb shell "ls /system/priv-app/com.otot.mikve.general-1/lib/arm/ | busybox head -40"') ^
do ^
adb shell "su -c chown system:system /system/lib/%%G" &^
adb shell "su -c chmod -R 644  /system/lib/%%G" )


SETLOCAL ENABLEDELAYEDEXPANSION
(for /f %%G in ('adb shell "ls /system/priv-app/com.otot.mikve.general-1/lib/arm/ | busybox head -40"') ^
do ^
set myvar1=shell "su -c chown system:system /system/lib/%%G" &^
!myvar1!
)

