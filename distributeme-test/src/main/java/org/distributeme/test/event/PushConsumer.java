package org.distributeme.test.event;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventServicePushConsumer;

/**
 * A push consumer which simply prints out all events.
 */
public class PushConsumer extends AbstractECTester implements EventServicePushConsumer{
	
	@Override
	public void push(Event event) {
		out("Received "+event);
	}
	
	
}
