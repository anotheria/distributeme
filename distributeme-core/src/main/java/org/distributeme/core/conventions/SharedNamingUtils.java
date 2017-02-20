package org.distributeme.core.conventions;

import net.anotheria.anoprise.metafactory.Service;

/**
 * Utils for conventions like naming.
 *
 * @author another
 * @version $Id: $Id
 */
public class SharedNamingUtils {
	/**
	 * Get package name for generated code.
	 *
	 * @param clazz a {@link java.lang.Class} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getPackageName(Class<? extends Service> clazz){
		return clazz.getPackage().getName()+".generated";
	}

	/**
	 * Get generated remote stub factory name.
	 *
	 * @param serviceClazz a {@link java.lang.Class} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static final String getStubFactoryFullClassName(Class<? extends Service> serviceClazz){
		return getPackageName(serviceClazz)+"."+"Remote"+serviceClazz.getSimpleName()+"Factory";
	}

	/**
	 * <p>getAsynchFactoryFullClassName.</p>
	 *
	 * @param serviceClazz a {@link java.lang.Class} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static final String getAsynchFactoryFullClassName(Class<? extends Service> serviceClazz){
		return getPackageName(serviceClazz)+"."+"Asynch"+serviceClazz.getSimpleName()+"Factory";
	}
}
