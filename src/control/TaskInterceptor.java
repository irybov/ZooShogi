package control;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class TaskInterceptor extends Thread{

	private List<Future<Integer>> tasks;
	
	public TaskInterceptor(List<Future<Integer>> tasks) {
		this.tasks = tasks;
	}

	@Override
	public void run() {
		
		while(!Thread.interrupted()) {
			for(Future<Integer> task : tasks) {
				try {
					if(task.isDone() && task.get() > 999) {
						for(Future<Integer> each : tasks) {
							each.cancel(true);
						}
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
