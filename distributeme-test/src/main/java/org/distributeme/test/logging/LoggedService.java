package org.distributeme.test.logging;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.generator.logwriter.SL4JLogWriter;

@DistributeMe(logWriterClazz=SL4JLogWriter.class )
public interface LoggedService extends Service{
	void dummyMethod();
	
	boolean anotherMethod(long parameter);
}
