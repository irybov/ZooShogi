package control;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Interceptor extends Thread{

	private ExecutorService es;
	private List<Future<Integer>> tasks;
	private boolean stopped;
	
	public Interceptor(ExecutorService es, List<Future<Integer>> tasks, boolean stopped) {
		this.es = es;
		this.tasks = tasks;
		this.stopped = stopped;
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
						es.shutdownNow();
						es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
						stopped = true;
						System.out.println("Interrupted in " + 
								Thread.currentThread());
					}
				}
				catch (InterruptedException | ExecutionException exc) {
					exc.printStackTrace();
					Thread.currentThread().interrupt();
				}
			}
		}		
	}
	
}
