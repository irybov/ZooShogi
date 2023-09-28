package control;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import ai.type.AI;

class TaskInterceptor extends Thread{

	private List<AI> ais;
	private ExecutorService es;
	private List<Future<Integer>> tasks;
	
	public TaskInterceptor(List<AI> ais, ExecutorService es) {
		this.ais = ais;
		this.es = es;		
		tasks = new CopyOnWriteArrayList<>();
		for(AI ai : ais) {
			tasks.add(es.submit(ai));
		}
	}

	@Override
	public void run() {

		while(!tasks.isEmpty()) {
			for(Future<Integer> task : tasks) {
				try {
					if(task.isDone()) {
						if(task.get() > 999) {
							for(Future<Integer> each : tasks) {
								each.cancel(false);
								tasks.remove(each);
							}
						}
						tasks.remove(task);
					}
					else if(task.isCancelled()) {
						tasks.remove(task);
					}
				}
				catch (InterruptedException | ExecutionException exc) {
					exc.printStackTrace();
				}
			}
		}
	}
	
}
