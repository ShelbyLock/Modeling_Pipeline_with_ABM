package stages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import jobs.Job;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class Stage {
	//Basic Information
	private int pipelineID;
	private int stageID;
	private Grid <Object > grid;
	private int x;
	private int y;
	
	//Process Information
	private ArrayList<Job> jobQueue = new ArrayList<Job>();
	private int numJobs;
	
	//stage properties
	private int capacity;
	private int duration;
	
	public boolean isManual;
	private int manualProcessTime;
	
	public Stage(int pipelineID, int stageID, Grid <Object > grid) {
		this.setPipelineID(pipelineID);
		this.setStageID(stageID);
		this.grid = grid;
		this.x = stageID * 5;
		this.y = stageID + (pipelineID* 10);
		
		this.numJobs = 0;	
		this.capacity = 5;
		this.duration = 3;
		this.manualProcessTime = 10;
		isManual = false;
	}

	@SuppressWarnings("unchecked")
	@ScheduledMethod(start = 2, interval = 1)
	public void handlingJobs() {
		//The context which the job is operating on.
		Context<Object> context = ContextUtils.getContext (this); 
		//The network that paint the traces of the job
		Network<Object> net = ( Network <Object>) context.getProjection ("infection network");		
		int countHandling = 0;
		
		while (numJobs >= 1 && (countHandling < capacity)) {
			Job currentJob = jobQueue.get(0);
			boolean shoudStageAbort = new Random().nextBoolean();
	
			if (!shoudStageAbort) {
				grid.moveTo(currentJob, this.getX(), this.getY()); 
				int numElement = currentJob.stageHistoryList.size();
				if (1 < numElement) {
					net.addEdge(currentJob.stageHistoryList.get(numElement - 2), currentJob.stageHistoryList.get(numElement - 1));
				}
				currentJob.isAborted = false;
			}else
				currentJob.isAborted = true;			

			
			if (isManual)
				currentJob.processedTime = manualProcessTime;
			else
				currentJob.processedTime = duration;
			
			currentJob.isHandled = true;
			jobQueue.remove(0);
			this.numJobs--;
			countHandling++;	
		}		
	}
	
	public void addNewJob(Job job) {
		jobQueue.add(job);
		numJobs++; 
	}
	
	public void cleanUpNetworkGraph(Network<Object> net, Job currentJob) {
		int count = 1;
		/*
		while (count < currentJob.stageHistoryList.size() - 1) {
			RepastEdge<Object> currentEdge = net.getEdge(currentJob.stageHistoryList.get(count-1), currentJob.stageHistoryList.get(count));
			net.removeEdge(currentEdge);
			//net.addEdge(currentJob.stageHistoryList.get(count - 1), currentJob.stageHistoryList.get(count));
			count++;
		}*/
		int numElement = currentJob.stageHistoryList.size();
		if (count < numElement - 1) {
			net.addEdge(currentJob.stageHistoryList.get(numElement - 2), currentJob.stageHistoryList.get(numElement - 1));
		}
		//net.addEdge(this, currentJob);
	}
	public int getX(){
		return x;
	}
	public int getY() {
		return y;
	}

	public int getStageID() {
		return stageID;
	}

	public void setStageID(int stageID) {
		this.stageID = stageID;
	}

	public int getPipelineID() {
		return pipelineID;
	}

	public void setPipelineID(int pipelineID) {
		this.pipelineID = pipelineID;
	}
	
}
