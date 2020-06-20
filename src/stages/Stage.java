package stages;

import java.util.ArrayList;
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
	
	private int manualProcessTime;
	public int abortWaitingTime = 10;
	//private double abortedRatio;
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

	}

	@SuppressWarnings("unchecked")
	@ScheduledMethod(start = 3, interval = 1)
	public void handlingJobs() {
		//The context and network which jobs are operating on.
		Context<Object> context = ContextUtils.getContext (this); 
		Network<Object> net = ( Network <Object>) context.getProjection ("infection network");		
		int jobsProcessedByStage = 0;
		while (numJobs > 0 && (jobsProcessedByStage < capacity)) {
			Job currentJob = this.jobQueue.get(jobsProcessedByStage);
			boolean shoudStageAbort = new Random().nextBoolean();
	
			if (!shoudStageAbort) 
				currentJob.isAborted = false;
			else {
				currentJob.isAborted = true;
				currentJob.processedTotalTime = currentJob.processedTotalTime + abortWaitingTime;
				currentJob.abortWaitingTime = abortWaitingTime;
			}
			
			if (currentJob.isManual) {
				currentJob.processedTotalTime = currentJob.processedTotalTime + manualProcessTime;
				currentJob.processedTime = manualProcessTime;
			}
			else {
				currentJob.processedTime = currentJob.processedTotalTime + duration;
				currentJob.processedTime = duration;
			}
			
			grid.moveTo(currentJob, this.getX(), this.getY()); 
			int numElement = currentJob.stageHistoryList.size();
			if (numElement > 1) {
				StageHistory sh1 = currentJob.stageHistoryList.get(numElement - 2);
				StageHistory sh2 = currentJob.stageHistoryList.get(numElement - 1);
				net.addEdge(currentJob.pipelines.get(sh1.getStageID()).get(sh1.getPipelineID()), currentJob.pipelines.get(sh2.getStageID()).get(sh2.getPipelineID()));
			}
			
			if (currentJob.processedTime != 0)
				currentJob.processedTime--;
			else {
				currentJob.isHandled = true;
				this.jobQueue.remove(currentJob);
				this.numJobs--;		
				jobsProcessedByStage--;
			}
			
			jobsProcessedByStage++;
		}		
	}
	
	public void addNewJob(Job job) {
		jobQueue.add(job);
		numJobs++; 
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
