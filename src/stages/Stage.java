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
	@ScheduledMethod(start = 1, interval = 1)
	public void handlingJobs() {	
		int jobsProcessedByStage = 0;
		while (numJobs > jobsProcessedByStage && (jobsProcessedByStage < capacity)) {
			Job currentJob = this.jobQueue.get(jobsProcessedByStage);
			
			//Determine if this is a new job or an ongoing job
			if (currentJob.processedTime > 0)
			{
				jobsProcessedByStage = handlingOnGoingJob(currentJob, jobsProcessedByStage);				
			} else
			{
				handlingNewJob(currentJob);
			}		
			jobsProcessedByStage++;
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void handlingNewJob(Job currentJob){
		//The context and network which jobs are operating on.
		Context<Object> context = ContextUtils.getContext (this); 
		Network<Object> net = ( Network <Object>) context.getProjection ("infection network");	
		if (currentJob.isManual) {
			currentJob.processedTotalTime = currentJob.processedTotalTime + manualProcessTime;
			currentJob.processedTime = manualProcessTime;
			System.out.print("Pipeline "+Integer.toString(this.pipelineID)+ " Stage " +Integer.toString(this.stageID)+ " :");
			System.out.print("Job (JobID: " +Integer.toString(currentJob.getJobID())+ ") is manually processed! \n");
		}
		else {
			currentJob.processedTime = currentJob.processedTotalTime + duration;
			currentJob.processedTime = duration;
			System.out.print("Pipeline "+Integer.toString(this.pipelineID)+ " Stage " +Integer.toString(this.stageID)+ " :");
			System.out.print("Job (JobID: "+Integer.toString(currentJob.getJobID())+") is automatically processed! \n");
		}
		
		
		grid.moveTo(currentJob, this.getX(), this.getY()); 
		//下面能简化
		int numElement = currentJob.stageHistoryList.size();
		if (numElement > 1) {
			StageHistory sh1 = currentJob.stageHistoryList.get(numElement - 2);
			StageHistory sh2 = currentJob.stageHistoryList.get(numElement - 1);
			net.addEdge(currentJob.pipelines.get(sh1.getStageID()).get(sh1.getPipelineID()), currentJob.pipelines.get(sh2.getStageID()).get(sh2.getPipelineID()));
		}
	}
	
	public int handlingOnGoingJob(Job currentJob, int jobsProcessedByStage) {
		
		currentJob.processedTime--;
		System.out.print("Pipeline "+Integer.toString(this.pipelineID)+ " Stage " +Integer.toString(this.stageID)+ " :");
		System.out.print("Handling Job (JobID: " +Integer.toString(currentJob.getJobID())+ ") the remaining time is "+ Integer.toString(currentJob.processedTime) +"\n");

		if (currentJob.processedTime == 0) {
			currentJob.isHandled = true;
			System.out.print("Pipeline "+Integer.toString(this.pipelineID)+ " Stage " +Integer.toString(this.stageID)+ " :");
			System.out.print("Finish Job (JobID: " +Integer.toString(currentJob.getJobID())+") \n");
			shouldIabortCurrentJob(currentJob);
			
			this.jobQueue.remove(currentJob);
			this.numJobs--;		
			jobsProcessedByStage--;
		}
		return jobsProcessedByStage;
	}
	
	public void shouldIabortCurrentJob(Job currentJob) {
		boolean shoudStageAbort = new Random().nextBoolean();
		if (!shoudStageAbort) {
			currentJob.isAborted = false;
			System.out.print("Pipeline "+Integer.toString(this.pipelineID)+ " Stage " +Integer.toString(this.stageID)+ " :");
			System.out.print("Job (JobID: " +Integer.toString(currentJob.getJobID())+ ") is not aborted! \n");
		}
		else {
			currentJob.isAborted = true;
			currentJob.processedTotalTime = currentJob.processedTotalTime + abortWaitingTime;
			currentJob.abortWaitingTime = abortWaitingTime;
			System.out.print("Pipeline "+Integer.toString(this.pipelineID)+ " Stage " +Integer.toString(this.stageID)+ " :");
			System.out.print("Job (JobID: " +Integer.toString(currentJob.getJobID())+ ") is aborted! \n");
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
