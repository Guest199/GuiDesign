
rem LIST FOR ANDROID  5
set files=/system/lib/libbarcode_wdc.so /system/lib/libdpfj.so /system/lib/libdpfpdd.so /system/lib/libdpfpdd5000.so /system/lib/libdpfpdd7k.so /system/lib/libdpfpdd_4k.so /system/lib/libdpfpdd_ptapi.so /system/lib/libdpfr6.so /system/lib/libdpfr7.so /system/lib/libdpuareu_jni.so /system/lib/libftrSDKAndroidJni.so /system/lib/libftrScanAPI.so /system/lib/libftrapi.so /system/lib/libnative-lib.so /system/lib/libserial_port.so /system/lib/libtfm.so /system/lib/libusb-1.0.so
rem set files=/system/lib/libconscrypt_jni.so /system/lib/libdpfj.so /system/lib/libdpfpdd.so /system/lib/libdpfpdd5000.so /system/lib/libdpfpdd7k.so /system/lib/libdpfpdd_4k.so /system/lib/libdpfpdd_ptapi.so /system/lib/libdpfr6.so /system/lib/libdpfr7.so /system/lib/libdpuareu_jni.so /system/lib/libftrSDKAndroidJni.so /system/lib/libftrScanAPI.so /system/lib/libftrapi.so /system/lib/libnative-lib.so /system/lib/libserial_port.so /system/lib/libtfm.so /system/lib/libusb-1.0.so
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
pause
adb reboot

