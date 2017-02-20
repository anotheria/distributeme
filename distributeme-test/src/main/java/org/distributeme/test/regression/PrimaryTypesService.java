package org.distributeme.test.regression;

import net.anotheria.anoprise.metafactory.Service;
import org.distributeme.annotation.DistributeMe;

/**
 * This service ensures that we have support for all primary types at compile level.
 * @author lrosenberg
 *
 */
@DistributeMe
public interface PrimaryTypesService extends Service{
	int method(int a, int b);
	short method(short a, short b);
	long method(long a, long b);
	byte method(byte a, byte b);
	float method(float a, float b);
	double method(double a, double b);
	String method(String a, String b);
	boolean method(boolean a, boolean b);
}
