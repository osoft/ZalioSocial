package net.zalio.android.social.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.drawable.Drawable;

class ImageCacheManager {
	// Singleton
	private static ImageCacheManager INSTANCE = new ImageCacheManager();
	private ImageCacheManager(){
		
	}
	protected static ImageCacheManager getInstance(){
		return INSTANCE;
	}
	
	private static final String CACHE_PATH = "ZalioSocial/";
	private static final String TAG = "ImageCacheManager";
	private static final long CACHE_SIZE = 1024 * 1024 * 8;
	
	public String getCachePath(){
		return FileUtils.getSDPATH() + CACHE_PATH;
	}

	public Drawable downloadImgNoCache(String url){
        try {  
            return Drawable.createFromStream(new URL(url).openStream(),  
                    null);  
        } catch (Exception e) {  
            e.printStackTrace();  
            throw new RuntimeException(e);  
        }  
	}
	
	public boolean downloadImageToCache(String url, String fileName){
		trimCache();
		if(-1 == downFile(url, CACHE_PATH, fileName)){
			return false;
		}else{
			return true;
		}
	}
	
	private void trimCache(){
		File cacheDir = new File(getCachePath());
		File[] cacheFiles = cacheDir.listFiles();
		if(cacheFiles == null){
			MyLog.e(TAG, "Cache Dir fail");
			return;
		}
		
		// Sort file list by file date
	    List<File> list = Arrays.asList(cacheFiles);
	    ArrayList<File> fileList = new ArrayList<File>(list);
	    Collections.sort(fileList, new Comparator<File>(){

			@Override
			public int compare(File lhs, File rhs) {
				long lhsDate = lhs.lastModified();
				long rhsDate = rhs.lastModified();
				return (int) (rhsDate - lhsDate);
			}
	    	
	    });
	    
	    // Remove old files if excesses CACHE_SIZE
	    long totalSize = 0;
	    for(int i = 0; i < fileList.size(); i++){
	    	File currFile = fileList.get(i);
	    	if(totalSize + currFile.length() > CACHE_SIZE){
	    		currFile.delete();
	    	}else{
	    		totalSize += currFile.length();
	    	}
	    }
	    
	    
	}
	
	/**  
     *   
     * @param urlStr  
     * @param path  
     * @param fileName  
     * @return   
     *      -1:文件下载出错  
     *       0:文件下载成功  
     *       1:文件已经存在  
     */  
    private int downFile(String urlStr, String path, String fileName){  
        InputStream inputStream = null;  
        try {  
            FileUtils fileUtils = new FileUtils();  
              
            if(fileUtils.isFileExist(path + fileName)){  
                return 1;  
            } else {  
                inputStream = getInputStreamFromURL(urlStr);  
                File resultFile = fileUtils.write2SDFromInput(path, fileName, inputStream);  
                if(resultFile == null){  
                    return -1;  
                }  
            }  
        }   
        catch (Exception e) {  
            e.printStackTrace();  
            return -1;  
        }  
        finally{  
            try {  
                if(inputStream != null){
                	inputStream.close();  
                }
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return 0;  
    }  
      
    /**  
     * 根据URL得到输入流  
     * @param urlStr  
     * @return  
     */  
    private InputStream getInputStreamFromURL(String urlStr) {  
        HttpURLConnection urlConn = null;  
        InputStream inputStream = null;  
        URL url;
        try {  
            url = new URL(urlStr);  
            urlConn = (HttpURLConnection)url.openConnection();  
            inputStream = urlConn.getInputStream();  
              
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
        return inputStream;  
    }  
}
