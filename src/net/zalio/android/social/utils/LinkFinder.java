package net.zalio.android.social.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkFinder {
	private static final String TAG = "LinkFinder";
	private static String REGEX_FIND_URL_EMAIL = "([a-zA-Z0-9_]*@)?(https?://)?(ftp://)?(www\\.)?([a-zA-Z0-9_%\\-+!\\(\\)]*)\\b\\.[a-z]{2,3}(\\.[a-z]{2})?((/[a-zA-Z0-9_%\\*&\\-+!,;:.\\(\\)]*)+)?(\\.[a-z]*)?";
	private LinkFinder(){
		
	}
	
	static public ArrayList<String> getLinks(String text){
        Pattern p = Pattern.compile(REGEX_FIND_URL_EMAIL);
        Matcher m = p.matcher(text);
        ArrayList<String> links = new ArrayList<String>();
        while(m.find()){
        	MyLog.i(TAG, "URL found: " + m.group());
        	links.add(m.group());
        }
		if(links.size() == 0){
			return null;
		}else{
			return links;
		}
	}
}
