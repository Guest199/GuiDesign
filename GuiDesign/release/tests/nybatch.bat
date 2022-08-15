
@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

adb shell "su -c cd /system/lib/"

(for /f %%G in ('adb shell "ls /system/priv-app/com.otot.mikve.general-1/lib/arm/ | busybox head -40"') ^
do ^
set myvar1= shell "su -c chown system:system /system/lib/%%G" &^
echo !myvar1! &^
!myvar1!
)
rem adb shell "su -c chown system:system | echo "/system/lib/libserial_port.so"
rem echo "/system/lib/libserial_port.so" | adb shell "su -c chown system:system "