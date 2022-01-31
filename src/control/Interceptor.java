package control;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Interceptor extends Thread{
	
    private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);

	private List<Future<Integer>> tasks;
	
	public Interceptor(List<Future<Integer>> tasks) {
		this.tasks = tasks;
	}

	@Override
	public void run() {
		
		while(!Thread.interrupted()) {
			for(Future<Integer> task: tasks) {
				try {
					if(task.isDone() && task.get() > 500) {
						for(Future<Integer> each: tasks) {
							each.cancel(true);
						}
					}
				}
				catch (InterruptedException | ExecutionException exc) {
					logger.warn(exc.getMessage(), Interceptor.class.getSimpleName());
					Thread.currentThread().interrupt();
				}
			}
		}		
	}
	
}
