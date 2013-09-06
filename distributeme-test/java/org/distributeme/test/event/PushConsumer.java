package org.distributeme.test.event;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventServicePushConsumer;

public class PushConsumer extends AbstractECTester implements EventServicePushConsumer{
	
	@Override
	public void push(Event event) {
		out("Received "+event);
	}
	
	
}
