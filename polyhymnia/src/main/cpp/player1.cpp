//
// Created by yanxx on 2019/1/23.
//
#include "AudioPlayer.h"


bool is_init1= false;
static AudioPlayer *player1;

extern "C" JNIEXPORT void
JNICALL
 Java_com_dennisce_polyhymnia_NdkPlayer1_init(
        JNIEnv *env,
        jobject /* this */, jobjectArray _srcs) {
//将java传入的字符串数组转为c字符串数组
    jsize len = env->GetArrayLength(_srcs);
    char **pathArr = (char **) malloc(len * sizeof(char *));
    int i = 0;
    for (i = 0; i < len; i++) {
        jstring str = static_cast<jstring>(env->GetObjectArrayElement(_srcs, i));
        pathArr[i] = const_cast<char *>(env->GetStringUTFChars(str, 0));
    }
    player1 = new AudioPlayer(pathArr, len);
    is_init1= true;
}

extern "C" JNIEXPORT void
JNICALL
  Java_com_dennisce_polyhymnia_NdkPlayer1_changeVolumes(
        JNIEnv *env,
        jobject /* this */, jobjectArray _volumes) {
//将java传入的字符串数组转为c字符串数组
    jsize len = env->GetArrayLength(_volumes);
    int i = 0;
    for (i = 0; i < len; i++) {
        jstring str = static_cast<jstring>(env->GetObjectArrayElement(_volumes, i));
        char *volume = const_cast<char *>(env->GetStringUTFChars(str, 0));
        player1->volumes[i] = volume;
    }
    player1->change = 1;
}

extern "C" JNIEXPORT void
JNICALL
  Java_com_dennisce_polyhymnia_NdkPlayer1_changeTempo(
        JNIEnv *env,
        jobject /* this */, jstring _tempo) {
    char *tempo = const_cast<char *>(env->GetStringUTFChars(_tempo, 0));
    player1->tempo = tempo;
    player1->change = 1;
}
extern "C" JNIEXPORT void
JNICALL
 Java_com_dennisce_polyhymnia_NdkPlayer1_play(
        JNIEnv *env,
        jobject /* this */) {
    player1->play();
}

extern "C" JNIEXPORT void
JNICALL
  Java_com_dennisce_polyhymnia_NdkPlayer1_pause(
        JNIEnv *env,
        jobject /* this */) {
    player1->pause();
}

extern "C" JNIEXPORT void
JNICALL
  Java_com_dennisce_polyhymnia_NdkPlayer1_release(
        JNIEnv *env,
        jobject /* this */) {
    player1->release();
    is_init1= false;
}
extern "C" JNIEXPORT void
JNICALL
  Java_com_dennisce_polyhymnia_NdkPlayer1_seek(
        JNIEnv *env,
        jobject /* this */, jdouble secs) {
    player1->seek(secs);
}

extern "C" JNIEXPORT jdouble
JNICALL
  Java_com_dennisce_polyhymnia_NdkPlayer1_duration(
        JNIEnv *env,
        jobject /* this */) {
    return player1->total_time;
}

extern "C" JNIEXPORT jdouble
JNICALL
  Java_com_dennisce_polyhymnia_NdkPlayer1_position(
        JNIEnv *env,
        jobject /* this */) {
    return player1->current_time;
}

extern "C" JNIEXPORT jboolean
JNICALL
  Java_com_dennisce_polyhymnia_NdkPlayer1_isInit(
        JNIEnv *env,
        jobject /* this */) {
    return is_init1;
}