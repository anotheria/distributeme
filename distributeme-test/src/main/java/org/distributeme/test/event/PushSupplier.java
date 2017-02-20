package org.distributeme.test.event;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.EventChannel;

import java.util.Random;

public class PushSupplier extends AbstractECTester  implements Runnable {
	
	private EventChannel channel;
	private int SLEEP = new Random(System.nanoTime()).nextInt(1500)+1500;
	private int counter = 0;
	
	public PushSupplier(EventChannel aChannel){
		channel = aChannel;
	}
	
	public void start(){
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run(){
		while(true){
			try{
				Thread.sleep(SLEEP);
			}catch(InterruptedException e){}
			Event e = new Event();
			String data = "Event "+(counter++)+" from "+getInstanceNumber()+" sleep "+SLEEP;
			e.setData(data);
			e.setOriginator(this.toString());
			out("Sending "+e);
			channel.push(e);
		}
	}
}
