#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>



#include "android/log.h"
static const char *TAG="barcode_wdc";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)
// ****************************************************************************
// Implement native function
//
// path = /dev/barcode_enable
//
// ****************************************************************************
JNIEXPORT jint JNICALL Java_com_otot_barcode_BarcodeWdc_BarcodePower(JNIEnv * env,
		jobject thiz, jstring path, jstring data) {
	int fd = 0;
	const char *cPath, *cData;
	if (path == NULL)
		return -1;

	// Convert jstring to char pointer
	cPath = (*env)->GetStringUTFChars(env, path, NULL);
	cData = (*env)->GetStringUTFChars(env, data, NULL);

	// Open node �/dev/barcode_enable�
	fd = open(cPath, O_WRONLY | O_NOCTTY | O_NDELAY | O_NONBLOCK);
	if (fd < 0) {
		// Release char pointer
		(*env)->ReleaseStringUTFChars(env, path, cPath);
		(*env)->ReleaseStringUTFChars(env, data, cData);
		return -1;
	}

	// Write data to the node �/dev/barcode_enable�,
	// Enable Barcode Power => write data = �1�;
	// Disable Barcode Power => write data = �0�;
	write(fd, cData, strlen(cData));

	// Release char pointer
	(*env)->ReleaseStringUTFChars(env, path, cPath);
	(*env)->ReleaseStringUTFChars(env, data, cData);
	close(fd);
	return 0;
}
