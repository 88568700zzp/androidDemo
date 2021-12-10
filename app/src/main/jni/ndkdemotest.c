//
// Created by zhangzhipeng on 2021/12/9.
//
#include "com_zzp_applicationkotlin_NDKTool.h"

JNIEXPORT jstring JNICALL Java_com_zzp_applicationkotlin_NDKTool_getStringFromNDK
  (JNIEnv *env, jobject obj){
     return (*env)->NewStringUTF(env,"Hellow World，这是隔壁老李头的NDK的第一行代码");
  }
