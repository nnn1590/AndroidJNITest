LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := run_pie.c
LOCAL_MODULE := run_pie

include $(BUILD_EXECUTABLE)
