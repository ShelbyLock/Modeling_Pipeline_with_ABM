package pipeline;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import stages.Stages;

import java.util.ArrayList;
import java.util.Map;

import pipeline.chart.JobStatechart;
import repast.simphony.ui.probe.ProbedProperty;

public class Job {
	public Grid <Object > grid;
	//private ArrayList<Stages> sCommit;
	//private ArrayList<Stages> sBuild;
	//private ArrayList<Stages> sQA;
	//private ArrayList<Stages> sUAT;
	//private ArrayList<Stages> sSTG;
	//private ArrayList<Stages> sProd;
	//private ArrayList<Stages> allStages;
	private Map<Integer, ArrayList<Stages>> pipelines;	 
	private int time = 0;
	private boolean justStarted = true;
	public Job (Grid <Object > grid, Map<Integer, ArrayList<Stages>> pipelines){	
		this.grid = grid;
		this.pipelines = pipelines;
		this.time = 0;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void changeState() {
		if (justStarted) {
			int stage_index1 = RandomHelper.nextIntFromTo (0, 4);
			//Stage1_Commit s1 = sc.get(stage_index1);
			//grid.moveTo(this, s1.getX(), s1.getY());
			justStarted = false;
		}else
			moveStage();
		
	}
	@SuppressWarnings("unchecked")
	private void moveStage() {
		int index = RandomHelper.nextIntFromTo (2, 6);
		//The context which the job is operating on.
		Context<Object> context = ContextUtils.getContext (this); 
		//The network that paint the traces of the job
		Network<Object> net = ( Network <Object>) context.getProjection ("infection network");
		
		GridPoint pt = grid.getLocation (this);
		Object temp = grid.getObjectAt(pt.getX(), pt.getY());
		/*
		switch(index) {

		case 2:
			MoveToStage2(net, temp);
			time++; // replace with a function to calculate incremented time
			break;
		case 3: 
			MoveToStage3(net, temp);
			time++; // replace with a function to calculate incremented time
			break;
		case 4: 
			MoveToStage4(net, temp);
			time++; // replace with a function to calculate incremented time
			break;
		case 5:	
			MoveToStage5(net, temp);
			time++; // replace with a function to calculate incremented time
			break;
		case 6: 
			MoveToStage6(net, temp);
			time++; // replace with a function to calculate incremented time
			break;
		case 7: 
			//The job is either aborted or finished
			context.remove (this);
			time++; // replace with a function to calculate incremented time
			//Add method to determined if this job is aborted or finished
			//Add method to log the time for this job (timestamp)
			break;
		}	*/	
		
	}
	/*
	public void MoveToStage2(Network<Object> net, Object temp) {

		while(true) {
			int stage_index2 = RandomHelper.nextIntFromTo (0, 4);	
			Stage2_Build s2 = sb.get(stage_index2);
			Object mutex = new Object();
			synchronized (mutex) {
				if (s2.isNewJobAllowed()) {
					grid.moveTo(this, s2.getX(), s2.getY()); 
					net.addEdge(temp, this);
					s2.addNewJob();
					break;
				}
			}
		}			
	}
	public void MoveToStage3(Network<Object> net, Object temp) {

		while(true) {
			int stage_index3 = RandomHelper.nextIntFromTo (0, 4);	
			Stage3_QA s3 = sq.get(stage_index3);
			Object mutex = new Object();
			synchronized (mutex) {
				if (s3.isNewJobAllowed()) {
					grid.moveTo(this, s3.getX(), s3.getY()); 
					net.addEdge(temp, this);
					s3.addNewJob();
					break;
				}
			}
		}			
	}
	public void MoveToStage4(Network<Object> net, Object temp) {
		int attempt = 10;
		while(true) {
			int stage_index4 = RandomHelper.nextIntFromTo (0, 4);	
			Stage4_UAT s4 = su.get(stage_index4);
			Object mutex = new Object();
			synchronized (mutex) {
				if (s4.isNewJobAllowed() && attempt <= 10) {
					grid.moveTo(this, s4.getX(), s4.getY()); 
					net.addEdge(temp, this);
					s4.addNewJob();
					break;
				}else if (attempt > 10){
					
				}
				
			}
			attempt++;
		}			
	}
	public void MoveToStage5(Network<Object> net, Object temp) {

		while(true) {
			int stage_index5 = RandomHelper.nextIntFromTo (0, 4);	
			Stage5_STG s5 = ss.get(stage_index5);
			Object mutex = new Object();
			synchronized (mutex) {
			if (s5.isNewJobAllowed()) {
				grid.moveTo(this, s5.getX(), s5.getY()); 
				net.addEdge(temp, this);
					s5.addNewJob();
					break;
				}
			}
		}			
	}
	public void MoveToStage6(Network<Object> net, Object temp) {

		while(true) {
			int stage_index6 = RandomHelper.nextIntFromTo (0, 4);	
			Stage6_PROD s6 = sp.get(stage_index6);
			Object mutex = new Object();
			synchronized (mutex) {
				if (s6.isNewJobAllowed()) {
					grid.moveTo(this, s6.getX(), s6.getY()); 
					net.addEdge(temp, this);
					s6.addNewJob();
					break;
				}
			}
		}			
	}*/
}
