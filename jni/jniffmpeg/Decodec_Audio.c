#include <stdio.h>
#include <stdlib.h>

#include <android/log.h>

#include "include/com_leven_ffmpeg_jni_VideoPlayerDecode.h"
#include "../ffmpeg/libavutil/avutil.h"
#include "../ffmpeg/libavcodec/avcodec.h"
#include "../ffmpeg/libavformat/avformat.h"

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "graduation", __VA_ARGS__))

AVFormatContext *pFormatCtx = NULL;
int             audioStream, delay_time, videoFlag = 0;
AVCodecContext  *aCodecCtx;
AVCodec         *aCodec;
AVFrame         *aFrame;
AVPacket        packet;
int  frameFinished = 0;

JNIEXPORT jint JNICALL Java_com_leven_ffmpeg_jni_VideoPlayerDecode_VideoPlayer
  (JNIEnv *env, jclass clz, jstring fileName) {
	const char* local_title = (*env)->GetStringUTFChars(env, fileName, NULL);
	// Register all formats and codecs
	av_register_all();

	//int avformat_open_input(AVFormatContext **ps, const char *filename, AVInputFormat *fmt, AVDictionary **options);
	/*
	 *只读取文件头，并不会填充流信息
	 */
	// Open video file
	if(avformat_open_input(&pFormatCtx, local_title, NULL, NULL)!=0)
		return -1; // Couldn't open file

	//int avformat_find_stream_info(AVFormatContext *ic, AVDictionary **options);
	/*
	 *获取文件中的流信息，此函数会读取packet，并确定文件中所有流信息，
	 *设置pFormatCtx->streams指向文件中的流，但此函数并不会改变文件指针，
	 *读取的packet会给后面的解码进行处理。
	 */
	if(avformat_find_stream_info(pFormatCtx, NULL) < 0)
			return -1;

	/*void av_dump_format(AVFormatContext *ic,
	                    int index,
	                    const char *url,
	                    int is_output);*/
	/*
	 *输出文件的信息，也就是我们在使用ffmpeg时能够看到的文件详细信息，
	 *第二个参数指定输出哪条流的信息，-1代表ffmpeg自己选择。最后一个参数用于
	 *指定dump的是不是输出文件，我们的dump是输入文件，因此一定要为0
	 */
	av_dump_format(pFormatCtx, -1, local_title, 0);

	// Find the first video stream
	int i = 0;
	for(i=0; i< pFormatCtx->nb_streams; i++)
	{
		if(pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO){
			audioStream = i;
			break;
		}
	}

	if(audioStream < 0)return -1;
	aCodecCtx = pFormatCtx->streams[audioStream]->codec;
	aCodec = avcodec_find_decoder(aCodecCtx->codec_id);
	if(avcodec_open2(aCodecCtx, aCodec, NULL) < 0)return -1;
	aFrame = avcodec_alloc_frame();
	if(aFrame == NULL)return -1;
	int ret;
	while(videoFlag != -1)
	{
		if(av_read_frame(pFormatCtx, &packet) < 0)break;
		if(packet.stream_index == audioStream)
		{
			ret = avcodec_decode_audio4(aCodecCtx, aFrame, &frameFinished, &packet);
			if(ret > 0 && frameFinished)
			{
				int data_size = av_samples_get_buffer_size(
						aFrame->linesize,aCodecCtx->channels,
						aFrame->nb_samples,aCodecCtx->sample_fmt, 0);
				LOGI("audioDecodec  :%d",data_size);
			}

		}
		usleep(50000);
		while(videoFlag != 0)
		{
			if(videoFlag == 1)//暂停
			{
				sleep(1);
			}else if(videoFlag == -1) //停止
			{
				break;
			}
		}
		av_free_packet(&packet);
	}
	av_free(aFrame);
	avcodec_close(aCodecCtx);
	avformat_close_input(&pFormatCtx);
	(*env)->ReleaseStringUTFChars(env, fileName, local_title);
}

JNIEXPORT jint JNICALL Java_com_leven_ffmpeg_jni_VideoPlayerDecode_VideoPlayerPauseOrPlay
 (JNIEnv *env, jclass clz, jboolean opt) {
	if(videoFlag == 1) {
		videoFlag = 0;
	} else if (videoFlag == 0) {
		videoFlag = 1;
	}
	return videoFlag;
}

JNIEXPORT jint JNICALL Java_com_leven_ffmpeg_jni_VideoPlayerDecode_VideoPlayerStop
 (JNIEnv *env, jclass clz) {
	videoFlag = -1;
}
