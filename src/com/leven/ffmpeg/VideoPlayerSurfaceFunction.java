package com.leven.ffmpeg;

import com.leven.ffmpeg.jni.VideoPlayerDecode;

public class VideoPlayerSurfaceFunction
{
	private static VideoPlayerSurfaceFunction appAppCameraSurfaceFunctionInstance = null;
	public VideoPlayerSurfaceFunction(){
		
	}
	public static VideoPlayerSurfaceFunction getAppCameraSurfaceFunctionInstance(){
		if (appAppCameraSurfaceFunctionInstance == null)
		{
			appAppCameraSurfaceFunctionInstance = new VideoPlayerSurfaceFunction();
		}
		return appAppCameraSurfaceFunctionInstance;
	}
	/*
	 * 初始化Opengl es 2.0配置
	 */
	public void VideoPlayerShow_GLCreate(){
		VideoPlayerDecode.GlCreate();
	}
	/*
	 * 初始化Opengl es 2.0 SurfaceChanged
	 */
	public void VideoPlayerShow_GLInit(int width, int height){
		VideoPlayerDecode.GlInit(width, height);
	}
	/*
	 * 将解码后的数据渲染到Opengl es 上面
	 */
	public void VideoPlayerShow_Render(){
		VideoPlayerDecode.GlRender();
	}
}
