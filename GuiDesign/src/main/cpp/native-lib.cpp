#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL

Java_com_otot_mikve_activities_mytests_TestCPP_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
