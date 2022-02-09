package control;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.core.util.StatusPrinter;

class Interceptor extends Thread{
	
    private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);
    
//    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();	   

	private List<Future<Integer>> tasks;
	
	public Interceptor(List<Future<Integer>> tasks) {
		this.tasks = tasks;
	}

//	{
//	    StatusPrinter.print(context);		
//	}
	
	@Override
	public void run() {
		
		while(!Thread.interrupted()) {
			for(Future<Integer> task: tasks) {
				try {
					if(task.isDone() && task.get() > 500) {
						logger.warn("Calculation cancelled");
						for(Future<Integer> each: tasks) {
							each.cancel(true);
						}
					}
				}
				catch (InterruptedException | ExecutionException exc) {
					logger.warn(exc.getMessage());
					Thread.currentThread().interrupt();
				}
			}
		}		
	}
	
}
