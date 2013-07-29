#######################################################
##########              ffmpeg-prebuilt         #######
#######################################################
LOCAL_PATH := $(call my-dir)
#declare the prebuilt library
include $(CLEAR_VARS)
LOCAL_MODULE := ffmpeg-prebuilt
LOCAL_SRC_FILES := ffmpeg/android/armv7-a/libffmpeg-neon.so
LOCAL_EXPORT_C_INCLUDES := ffmpeg/android/armv7-a/include
LOCAL_EXPORT_LDLIBS := ffmpeg/android/armv7-a/libffmpeg-neon.so
LOCAL_PRELINK_MODULE := true
include $(PREBUILT_SHARED_LIBRARY)

########################################################
##              ffmpeg-test-neno.so             ########
########################################################
include $(CLEAR_VARS)
TARGET_ARCH_ABI=armeabi-v7a
LOCAL_ARM_MODE=arm
LOCAL_ARM_NEON=true
LOCAL_ALLOW_UNDEFINED_SYMBOLS=false
LOCAL_MODULE := ffmpeg-test-neon
LOCAL_SRC_FILES := jniffmpeg/Decodec_Audio.c

LOCAL_C_INCLUDES := $(LOCAL_PATH)/ffmpeg/android/armv7-a/include \
                    $(LOCAL_PATH)/ffmpeg \
                    $(LOCAL_PATH)/ffmpeg/libavutil \
                    $(LOCAL_PATH)/ffmpeg/libavcodec \
                    $(LOCAL_PATH)/ffmpeg/libavformat \
                    $(LOCAL_PATH)/ffmpeg/libavcodec \
                    $(LOCAL_PATH)/ffmpeg/libswscale \
                    $(LOCAL_PATH)/jniffmpeg \
                    $(LOCAL_PATH)
LOCAL_SHARED_LIBRARY := ffmpeg-prebuilt
LOCAL_LDLIBS    := -llog -ljnigraphics -lz -lm $(LOCAL_PATH)/ffmpeg/android/armv7-a/libffmpeg-neon.so
include $(BUILD_SHARED_LIBRARY)

#include $(call all-subdir-makefiles)