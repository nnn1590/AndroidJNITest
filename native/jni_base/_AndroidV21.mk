LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := addnative.c

LOCAL_MODULE := addnativev21

include $(BUILD_SHARED_LIBRARY)
