/******************************************************************************
*
* JNI wrapper for VCOM calls
*
******************************************************************************/

#include <jni.h>
#include <android/log.h>
#include <cstring>

#include "lumidigm_vcomdroid_VCOM.h"
#include "./VCOMExample/include/TransportLibUSB.h"
#include "./VCOMExample/include/V100_shared_types.h"

// VCOM->Android Log adapter
extern "C" void DumpLogFmt(const char *fileName, const char *fmt...);
#define VCOM_JNI_TAG "[VCOM]"

// VCOM "device handle"
// V100_DEVICE_TRANSPORT_INFO g_ti = { -1, NULL, NULL, 0, 0, NULL };
V100_DEVICE_TRANSPORT_INFO g_ti = { 0, NULL, NULL, 0, 0, NULL };

// data stash
// anything put here will be retained between open/close calls...
_V100_INTERFACE_CONFIGURATION_TYPE g_cfg = { 0 };
uint8_t  g_image[1024 * 1024];
uint8_t  g_tmpl[1024];
uint32_t g_tmplSize = 0;
int32_t  g_spoofScore = 0;
int32_t  g_matchScore = 0;
int32_t  g_serial = 0;

// open the device.
// this method expects a valid USB device FD to be placed in the calling param fd.
// this is obtained via Android Java's UsbManager/UsbDeviceConnection.
jint Java_lumidigm_vcomdroid_VCOM_JNI_1Open(JNIEnv *env, jobject obj, jint fd)
{
	// short-circuit: already open?
	if (g_ti.DeviceNumber != -1)
	{
		DumpLogFmt("", "JNI_Open: already an open device.");
		return -1;
	}

	// prepare device handle and attempt to open device.
	memset(&g_ti, 0, sizeof(g_ti));
	g_ti.DeviceNumber = fd;
	V100_ERROR_CODE rc;
	rc = V100_Open(&g_ti);
	if (rc == GEN_OK)
	{
		// device was opened, get the config structure.
		memset(&g_cfg, 0, sizeof(g_cfg));
		rc = V100_Get_Config(&g_ti, &g_cfg);
		if (rc == GEN_OK)
		{
			// DumpLogFmt("", "JNI_Open: VCOM device successfully opened.");
		}
		else
		{
			DumpLogFmt("", "JNI_Open: V100_Get_Config failed: %d.", rc);
		}
	}
	else
	{
		DumpLogFmt("", "JNI_Open: V100_Open failed: %d.", rc);
	}

	// cleanup on failure...
	if (rc != 0)
	{
		V100_Close(&g_ti);
		memset(&g_ti, 0, sizeof(g_ti));
		g_ti.DeviceNumber = -1;
		memset(&g_cfg, 0, sizeof(g_cfg));
		// DumpLogFmt("", "JNI_Open: cleanup complete.");
	}

	return rc;
}

// here, we close the device, but retain any data that we've stashed.
jint Java_lumidigm_vcomdroid_VCOM_JNI_1Close(JNIEnv *env, jobject object)
{
	// short-circuit: already closed?
	if (g_ti.DeviceNumber == -1)
	{
		DumpLogFmt("", "JNI_Close: no open device.");
		return 0;
	}

	int rc;
	rc = V100_Close(&g_ti);
	if (rc == GEN_OK)
	{
		//DumpLogFmt("", "JNI_Close: device closed.");
	}
	else
	{
		DumpLogFmt("", "JNI_Close: V100_Close failed %d", rc);
	}
	memset(&g_ti, 0, sizeof(g_ti));
	//memset(&g_cfg, 0, sizeof(g_cfg));		// doing this kills the GetCompositeImage[XY]() methods...
	g_ti.DeviceNumber = -1;

	return rc;
}

// "ping" the device by requesting its serial number.
jint Java_lumidigm_vcomdroid_VCOM_JNI_1Ping(JNIEnv *env, jobject obj)
{
	// invalidate.
	g_serial = -1;

	// short-circuit: no open device?
	if (g_ti.DeviceNumber == -1)
	{
		DumpLogFmt("", "JNI_Ping: no open device.");
		return GEN_ERROR_DEVICE_NOT_READY;
	}

	int rc;
	uint32_t serial;
	rc = V100_Get_Serial(&g_ti, &serial);
	if (rc == GEN_OK)
	{
		g_serial = serial;
		//DumpLogFmt("", "JNI_Ping: serial %u.", g_serial);
	}
	else
	{
		DumpLogFmt("", "JNI_Ping: V100_Get_Serial failed %d.", rc);
	}

	return rc;
}

// capture a composite image and template.
jint Java_lumidigm_vcomdroid_VCOM_JNI_1Capture(JNIEnv *env, jobject obj)
{
	// invalidate.
	g_spoofScore = -1;
	g_matchScore = -1;
	g_tmplSize = 0;

	// short-circuit: no open device?
	if (g_ti.DeviceNumber == -1)
	{
		DumpLogFmt("", "JNI_Capture: no open device.");
		return GEN_ERROR_DEVICE_NOT_READY;
	}

	uint32_t tmplSize = sizeof(g_tmpl);
	uint32_t imageSizeX = g_cfg.Composite_Image_Size_X;
	uint32_t imageSizeY = g_cfg.Composite_Image_Size_Y;
	int rc;
	rc = V100_Capture(&g_ti, g_image, imageSizeX, imageSizeY, g_tmpl, tmplSize, g_spoofScore, true, true);
	if (rc == GEN_OK)
	{
		if (imageSizeX == g_cfg.Composite_Image_Size_X || imageSizeY == g_cfg.Composite_Image_Size_Y)
		{
			//DumpLogFmt("", "JNI_Capture: successful.");
			g_tmplSize = tmplSize;
		}
		else
		{
			DumpLogFmt("", "Image size mismatch X: %d != %d or Y: %d != %d",
				imageSizeX, g_cfg.Composite_Image_Size_X, imageSizeY, g_cfg.Composite_Image_Size_Y);
			rc = GEN_ERROR_INTERNAL;
		}
	}
	else
	{
		DumpLogFmt("", "JNI_Capture: V100_Capture failed %d.", rc);
	}

	return rc;
}

jint Java_lumidigm_vcomdroid_VCOM_JNI_1GetSpoofScore(JNIEnv *env, jobject obj)
{
	return g_spoofScore;
}

jint Java_lumidigm_vcomdroid_VCOM_JNI_1GetMatchScore(JNIEnv *env, jobject obj)
{
	return g_matchScore;
}

jint Java_lumidigm_vcomdroid_VCOM_JNI_1GetCompositeImageSizeX(JNIEnv *env, jobject obj)
{
	return g_cfg.Composite_Image_Size_X;
}

jint Java_lumidigm_vcomdroid_VCOM_JNI_1GetCompositeImageSizeY(JNIEnv *env, jobject obj)
{
	return g_cfg.Composite_Image_Size_Y;
}

jint Java_lumidigm_vcomdroid_VCOM_JNI_1GetCompositeImage(JNIEnv *env, jobject obj, jintArray image)
{
	// short-circuit: image buffer too small?
	uint32_t pixels = g_cfg.Composite_Image_Size_X * g_cfg.Composite_Image_Size_Y;
	if (env->GetArrayLength(image) < pixels)
	{
		DumpLogFmt("", "JNI_GetCompositeImage: pixel buffer too small.");
		return GEN_ERROR_PARAMETER;
	}

	// get pointer to target.
	jint *cimage = env->GetIntArrayElements(image, NULL);
	{
		for (uint32_t i = 0; i < pixels; i++)
		{
			// naive chrominance algorithm
			uint8_t luminosity = g_image[i];
			uint32_t color = 0xFF;	// alpha -- full opacity
			color <<= 8;
			color |= luminosity;	// red
			color <<= 8;
			color |= luminosity;	// green
			color <<= 8;
			color |= luminosity;	// blue
			cimage[i] = color;
		}
	}
	env->ReleaseIntArrayElements(image, cimage, 0);
	//DumpLogFmt("", "JNI_GetCompositeImage: successful.");

	return GEN_OK;
}

jint Java_lumidigm_vcomdroid_VCOM_JNI_1GetTemplateSize(JNIEnv *env, jobject obj)
{
	return g_tmplSize;
}

jint Java_lumidigm_vcomdroid_VCOM_JNI_1GetTemplate(JNIEnv *env, jobject obj, jbyteArray tmpl)
{
	// short-circuit: template buffer too small?
	if (env->GetArrayLength(tmpl) < g_tmplSize)
	{
		DumpLogFmt("", "JNI_GetTemplate: template buffer too small.");
		return GEN_ERROR_PARAMETER;
	}

	jbyte *ctmpl = env->GetByteArrayElements(tmpl, NULL);
	{
		memcpy(ctmpl, g_tmpl, g_tmplSize);
	}
//	env->ReleaseByteArrayElements(tmpl, ctmpl, NULL);
	env->ReleaseByteArrayElements(tmpl, ctmpl, 0);

	return GEN_OK;
}

jint JNICALL Java_lumidigm_vcomdroid_VCOM_JNI_1Match(JNIEnv *env, jobject obj, jbyteArray tmpl1, jbyteArray tmpl2)
{
	// invalidate
	g_matchScore = -1;

	// short-circuit: no open device?
	if (g_ti.DeviceNumber == -1)
	{
		DumpLogFmt("", "JNI_Capture: no open device.");
		return GEN_ERROR_DEVICE_NOT_READY;
	}
	// short-circuit: no template 1?
	if (!env->GetArrayLength(tmpl1))
	{
		DumpLogFmt("", "JNI_Match: tmpl1 is zero-length.");
		return GEN_ERROR_PARAMETER;
	}
	// short-circuit: no template 1?
	if (!env->GetArrayLength(tmpl2))
	{
		DumpLogFmt("", "JNI_Match: tmpl2 is zero-length.");
		return GEN_ERROR_PARAMETER;
	}

	int rc;

	// get pointers to templates.
	jbyte *ctmpl1 = env->GetByteArrayElements(tmpl1, NULL);
	jbyte *ctmpl2 = env->GetByteArrayElements(tmpl2, NULL);
	{
		uint ltmpl1 = env->GetArrayLength(tmpl1);
		uint ltmpl2 = env->GetArrayLength(tmpl2);
		uint matchScore;
		rc = V100_Match(&g_ti, (uchar*) ctmpl1, ltmpl1, (uchar*) ctmpl2, ltmpl2, matchScore);
		if (rc == GEN_OK)
			g_matchScore = matchScore;
	}
//	env->ReleaseByteArrayElements(tmpl1, ctmpl1, NULL);
	env->ReleaseByteArrayElements(tmpl1, ctmpl1, 0);
//	env->ReleaseByteArrayElements(tmpl2, ctmpl2, NULL);
	env->ReleaseByteArrayElements(tmpl2, ctmpl2, 0);

	return rc;
}

