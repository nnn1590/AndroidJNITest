#include <stdio.h>
#include "jniheader/org_nnn1590_jnitest_MainActivity.h"

#ifdef __cplusplus
extern "C" {
#endif

// int main() { return -1; }

JNIEXPORT jint JNICALL Java_org_nnn1590_jnitest_MainActivity_addNative(JNIEnv* env, jobject obj, jint x, jint y) {
	return x + y;
}

JNIEXPORT jint JNICALL Java_org_nnn1590_jnitest_MainActivity_addStaticNative(JNIEnv* env, jclass class, jint x, jint y) {
	return x + y;
}

JNIEXPORT jintArray JNICALL Java_org_nnn1590_jnitest_MainActivity_addsNative(JNIEnv* env, jclass class, jint x, jintArray y) {
	const jsize arrayLength = (*env)->GetArrayLength(env, y);
	jint* ys = (*env)->GetIntArrayElements(env, y, JNI_FALSE);
	jintArray result;
	if (ys == NULL) return NULL;
	result = (*env)->NewIntArray(env, arrayLength);
	if (result == NULL) return NULL;
	jint result2[arrayLength];
	jsize i;
	for (i = 0; i < arrayLength; i++) result2[i] = x + ys[i];
	(*env)->SetIntArrayRegion(env, result, 0, arrayLength, result2);
	(*env)->ReleaseIntArrayElements(env, y, ys, JNI_ABORT);
	return result;
}

#ifdef __cplusplus
}
#endif
