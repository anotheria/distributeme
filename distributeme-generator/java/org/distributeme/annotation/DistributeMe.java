package org.distributeme.annotation;

import net.anotheria.anoprise.metafactory.Extension;
import org.distributeme.core.ServiceDescriptor.Protocol;
import org.distributeme.generator.logwriter.SysErrorLogWriter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Main annotation of the distributeme package that indicates that a service has to be distributed.
 * @author lrosenberg
 */
@Retention (RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface DistributeMe {
	Extension extension() default Extension.LOCAL;
	/**
	 * Possibilitiy to insert some lines of code.
	 * @return
	 */
	String[] initcode() default {};
	/**
	 * If true generate support for moskito.
	 * @return
	 */
	boolean moskitoSupport() default true;

	/**
	 * Class of the factory implementation of the service.
	 * @return
	 */
	Class factoryClazz() default DummyFactory.class;

	/**
	 * If true the event service is enabled and started, false otherwise.
	 * @return
	 */
	boolean enableEventService() default true;

    /**
     * Supported protocols, RMI used as default.
     * @return list of protocols
     */
    Protocol[] protocols() default {Protocol.RMI};

	Class logWriterClazz() default SysErrorLogWriter.class;
	
	/**
	 * If true the support for agent transportation is included into the service. You will need distributeme-agents packet for it to work.
	 * @return
	 */
	boolean agentsSupport() default true;
	
	/**
	 * If true enables generation of asynchronous call support. Default false. Note, the default value is subject of changes in next releases, better specify desired behaviour explicitely. 
	 * @return
	 */
	boolean asynchSupport() default false;
	
	/**
	 * Customizes the asynch call timeout -> timeout after which synch called to asynch interface are cancelled. If none is specified the default is taken. 
	 * @return
	 */
	long asynchCallTimeout() default -1;
	/**
	 * Size of the threadpool for the internal executor threadpool size.
	 * @return
	 */
	long asynchExecutorPoolSize() default -1;

	/**
	 * If true all additional features will be disabled to reduce overhead. Useful for peer 2 peer communication.
	 * @return
	 */
	boolean stripToEssential() default false;
}
 