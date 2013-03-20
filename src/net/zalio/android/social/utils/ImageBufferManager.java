package net.zalio.android.social.utils;

import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class ImageBufferManager {
	private static final String TAG = "ImageBufferManager";
	protected static final String AFFIX_THUMB = "_thumb";
	protected static final String AFFIX_IMAGE = "";
	
	//Singleton
	private static ImageBufferManager INSTANCE = new ImageBufferManager();
	private ImageBufferManager(){
		
	}
	public static ImageBufferManager getInstance(){
		return INSTANCE;
	}
	
	public interface OnImageDownloadedListener{
		abstract public void onImageDownloaded(String socialName, String statusId);
	}
	
	public interface OnThumbDownloadedListener{
		abstract public void onThumbDownloaded(String socialName, String statusId);
	}
	
	private HashMap<String, SoftReference<Drawable>> imgBuffer = 
			new HashMap<String, SoftReference<Drawable>>(); 
	private ImageCacheManager icm = ImageCacheManager.getInstance();
	private OnImageDownloadedListener mImageDownlaodedListener;
	private OnThumbDownloadedListener mThumbDownloadedLIstener;
	private ImageDownloadTask mImageTask;
	private ThumbDownloadTask mThumbTask;
	
	private Drawable loadThumbnailFromURL(String socialName, String statusId, String urlString){
		URL url;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			MyLog.e(TAG, "URL invalid");
			return null;
		}
		String fileName = url.getFile();
		String affix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		if(affix == null || affix.equals("")){
			affix = ".jpg";
		}
		
		String downloadFilename = socialName + "_" + statusId + "_thumb" + affix;
		if(icm.downloadImageToCache(urlString, downloadFilename)){
			return Drawable.createFromPath(icm.getCachePath() + downloadFilename);
		}else{
			MyLog.e(TAG, "Download failed");
			return null;
		}
	}
	
	private Drawable loadImageFromURL(String socialName, String statusId, String urlString){
		URL url;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			MyLog.e(TAG, "URL invalid");
			return null;
		}
		String fileName = url.getFile();
		String affix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		if(affix == null || affix.equals("")){
			affix = ".jpg";
		}
		
		String downloadFilename = socialName + "_" + statusId + affix;
		if(icm.downloadImageToCache(urlString, downloadFilename)){
			return Drawable.createFromPath(icm.getCachePath() + downloadFilename);
		}else{
			MyLog.e(TAG, "Download failed");
			return null;
		}
	}
	/**
	 * Load image to a Drawable object. If the image needs to be downloaded, it will be downloaded in the background.
	 * OnImageDownloadedListener is used to get notification.
	 * @param socialName
	 * @param statusId
	 * @param urlString
	 * @return null - if the image needs to be downloaded.
	 *     <BR>Drawable  if the image is already downloaded.
	 */
	public Drawable loadImageThumb(String socialName, String statusId, String urlString){
		if(imgBuffer.containsKey(getKeyForThumb(socialName, statusId))){
			SoftReference<Drawable> softReference = imgBuffer.get(getKeyForThumb(socialName, statusId));
			if(softReference.get() != null){
				return softReference.get();
			}
		}
		addThumbTaskToQueue(socialName, statusId, urlString);
		return null;
	}
	
	public Drawable loadImage(String socialName, String statusId, String urlString){
		if(imgBuffer.containsKey(getKeyForImage(socialName, statusId))){
			SoftReference<Drawable> softReference = imgBuffer.get(getKeyForImage(socialName, statusId));
			if(softReference.get() != null){
				return softReference.get();
			}
		}
		
		addImageTaskToQueue(socialName, statusId, urlString);
		return null;
	}
		
	private void addThumbTaskToQueue(String socialName, String statusId, String urlString){
		if(mThumbTask == null || mThumbTask.getStatus() == AsyncTask.Status.FINISHED){
			mThumbTask = new ThumbDownloadTask();
			mThumbTask.add(socialName, statusId, urlString);
			mThumbTask.execute();
		}else if(mThumbTask.getStatus() == AsyncTask.Status.RUNNING){
			mThumbTask.add(socialName, statusId, urlString);	
		}
		MyLog.i(TAG, "Task Added: " + statusId + urlString);
	}
	
	private void addImageTaskToQueue(String socialName, String statusId, String urlString){
		if(mImageTask == null || mImageTask.getStatus() == AsyncTask.Status.FINISHED){
			mImageTask = new ImageDownloadTask();
			mImageTask.add(socialName, statusId, urlString);
			mImageTask.execute();
		}else if(mImageTask.getStatus() == AsyncTask.Status.FINISHED){
			mImageTask.add(socialName, statusId, urlString);	
		}
		MyLog.i(TAG, "Task Added: " + statusId + urlString);
	}
	
	protected OnImageDownloadedListener getImageDownloadedListener() {
		return mImageDownlaodedListener;
	}
	public void setImageDownlaodedListener(OnImageDownloadedListener mImageDownlaodedListener) {
		this.mImageDownlaodedListener = mImageDownlaodedListener;
	}
	
	protected OnThumbDownloadedListener getThumbDownloadedListener() {
		return mThumbDownloadedLIstener;
	}
	public void setThumbDownloadedLIstener(OnThumbDownloadedListener mThumbDownloadedLIstener) {
		this.mThumbDownloadedLIstener = mThumbDownloadedLIstener;
	}
	
    class InnerTask{
		String socialName;
		String statusId;
		String url;
	}
    
	class ImageDownloadTask extends AsyncTask<Void, InnerTask, Void>{
		private ArrayList<InnerTask> innerTasks = new ArrayList<InnerTask>();
		
		public void add(String socialName, String statusId, String urlString){
			for(InnerTask it:innerTasks){
				if(it.statusId.equals(statusId)
						&& it.url.equals(urlString)
						&& it.socialName.equals(socialName)){
					return;
				}
			}
			InnerTask task = new InnerTask();
			task.socialName = socialName;
			task.statusId = statusId;
			task.url = urlString;
			innerTasks.add(task);
		}


		@Override
		protected Void doInBackground(Void... params) {
			while(!innerTasks.isEmpty()){
				InnerTask task = innerTasks.get(0);
				Drawable d = loadImageFromURL(task.socialName, task.statusId, task.url);
				if(d != null){
					imgBuffer.put(getKeyForImage(task.socialName, task.statusId), new SoftReference<Drawable>(d));
					MyLog.i(TAG, "Task Finished: " + task.statusId);
					this.publishProgress(task);
				}
				innerTasks.remove(0);
			}
			mImageTask = null;
			return null;
		}
		
		@Override
		protected void onProgressUpdate(InnerTask ...param){
			InnerTask task = param[0];
			if(getImageDownloadedListener() != null){
				getImageDownloadedListener().onImageDownloaded(task.socialName, task.statusId);
			}
		}
		
	}
	
	class ThumbDownloadTask extends AsyncTask<Void, InnerTask, Void>{
		private ArrayList<InnerTask> innerTasks = new ArrayList<InnerTask>();
		
		public void add(String socialName, String statusId, String urlString){
			for(InnerTask it:innerTasks){
				if(it.statusId.equals(statusId)
						&& it.url.equals(urlString)
						&& it.socialName.equals(socialName)){
					return;
				}
			}
			InnerTask task = new InnerTask();
			task.socialName = socialName;
			task.statusId = statusId;
			task.url = urlString;
			innerTasks.add(task);
		}


		@Override
		protected Void doInBackground(Void... params) {
			while(!innerTasks.isEmpty()){
				InnerTask task = innerTasks.get(0);
				Drawable d = loadThumbnailFromURL(task.socialName, task.statusId, task.url);
				if(d != null){
					imgBuffer.put(getKeyForThumb(task.socialName, task.statusId), new SoftReference<Drawable>(d));
					MyLog.i(TAG, "Thumb Task Finished: " + task.statusId);
					this.publishProgress(task);
				}
				innerTasks.remove(0);
			}
			mThumbTask = null;
			return null;
		}
		
		@Override
		protected void onProgressUpdate(InnerTask ...param){
			InnerTask task = param[0];
			if(getThumbDownloadedListener() != null){
				getThumbDownloadedListener().onThumbDownloaded(task.socialName, task.statusId);
			}
		}
		
	}
	
	private String getKeyForImage(String socialName, String statusId){
		return socialName + statusId + AFFIX_IMAGE;
	}
	
	private String getKeyForThumb(String socialName, String statusId){
		return socialName + statusId + AFFIX_THUMB;
	}
}
