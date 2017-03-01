package org.distributeme.core.util;

import org.distributeme.core.RegistryLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Base class for registry utilities.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class BaseRegistryUtil {
	/**
	 * Location config object.
	 */
	protected static final RegistryLocation registryLocation = RegistryLocation.create();

	/**
	 * Logger.
	 */
	private static Logger LOG = LoggerFactory.getLogger(BaseRegistryUtil.class);


	
	/**
	 * Returns the base url of the specified registry application.
	 *
	 * @param app a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getRegistryBaseUrl(String app){
		return getRegistryBaseUrl(app,
				registryLocation.getRegistryContainerHost(),
				registryLocation.getRegistryContainerPort(),
				registryLocation.getRegistryContainerProtocol(),
				registryLocation.getRegistryContainerContext()
		);
	}
	
	/**
	 * <p>getRegistryBaseUrl.</p>
	 *
	 * @param app a {@link java.lang.String} object.
	 * @param host a {@link java.lang.String} object.
	 * @param port a int.
	 * @param protocol a {@link java.lang.String} object.
	 * @param context a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getRegistryBaseUrl(String app, String host, int port, String protocol, String context){
		String url = protocol+"://"+host+":"+port;
		if (context!=null && context.length()>0) {
			url += "/" + context + "/" + app + "/";
		}else {
			url += "/" + app + "/";
		}
		return url;
	}

	/**
	 * Reads the server reply to an url and returns it as string.
	 *
	 * @param url as string.
	 * @return an array of byte.
	 */
	public static byte[] getUrlContent(String url){
		return getUrlContent(url, false);
	}
	
	/**
	 * <p>getUrlContent.</p>
	 *
	 * @param url a {@link java.lang.String} object.
	 * @param silently a boolean.
	 * @return an array of byte.
	 */
	protected static byte[] getUrlContent(String url, boolean silently){
		try{
			URL myURL = new URL(url);
			URLConnection con = myURL.openConnection();

			InputStream inp = con.getInputStream();
			int expectedLength = con.getContentLength();
	
			int length = expectedLength;
			if (length<=0)
				length = 3000;
	//		System.out.println("Opened input stream.");
			//System.out.print(" ");
			byte[] b = new byte[length];
			byte[] result = new byte[length];
			int sum = 0;
			do{
				//System.out.println("trying to read "+length+" bytes");
				int r = inp.read(b,0,length);
				//System.out.println("have read "+r);
				if (r==-1)
					break;
				System.arraycopy(b,0,result,sum,r);
				sum += r;
			}while(sum<length);
			byte[] ret = new byte[sum];
			System.arraycopy(result, 0, ret, 0, sum);
			return ret;
		}catch(Exception e){
			if (!silently)
				LOG.error("getPageContent, url=" + url, e);
			return null;
		}

	}

	/**
	 * URLEncodes a string.
	 *
	 * @param urlPart string to encode.
	 * @return a {@link String} object.
	 */
	public static String encode(String urlPart){
		try{
			return URLEncoder.encode(urlPart, "UTF-8");
		}catch(UnsupportedEncodingException e){
			throw new AssertionError("WTF, UTF-8 is not supported?");
		}
	}

	public static RegistryLocation getRegistryLocation() {
		return registryLocation;
	}
}
 
