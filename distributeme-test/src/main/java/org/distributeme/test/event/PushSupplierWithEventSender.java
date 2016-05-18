package org.distributeme.test.event;

import net.anotheria.anoprise.eventservice.Event;
import net.anotheria.anoprise.eventservice.util.QueueFullException;
import net.anotheria.anoprise.eventservice.util.QueuedEventSender;

import java.util.Random;

public class PushSupplierWithEventSender extends AbstractECTester  implements Runnable {
	
	private QueuedEventSender sender;
	private int SLEEP = new Random(System.nanoTime()).nextInt(1500)+1500;
	private int counter = 0;
	
	public PushSupplierWithEventSender(QueuedEventSender aSender){
		sender = aSender;
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
			String data = "Event "+(counter++)+" from "+getInstanceNumber()+" sleep "+SLEEP+" through QueuedEventSender";
			e.setData(data);
			e.setOriginator(this.toString());
			out("Sending "+e);
			try{
				sender.push(e);
			}catch(QueueFullException ex){
				out("QUEUE FULL!!! "+ex.getMessage());
			}
		}
	}
}
