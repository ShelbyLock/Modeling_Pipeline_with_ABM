package stages;

import java.util.ArrayList;
import helper.DistributedRandomNumberGenerator;
import jobs.Job;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
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
	private double abortRatio;
	private int numJobs;
	
	//stage properties
	private int capacity;
	private int duration;	
	private int manualProcessTime;
	private int abortWaitingTime;
	//private double abortedRatio;
	public Stage(int pipelineID, int stageID, Parameters params, Grid <Object > grid) {
		this.setPipelineID(pipelineID);
		this.setStageID(stageID);
		this.grid = grid;
		this.x = stageID * 5;
		this.y = stageID + (pipelineID* 10);
		
		this.capacity = params.getInteger("capacity"); 
		this.duration = params.getInteger("duration"); 
		this.abortRatio = params.getDouble("abortRatio");
		this.manualProcessTime = params.getInteger("manualProcessTime"); 
		this.abortWaitingTime = params.getInteger("abortWaitingTime");
		
		this.numJobs = 0;	
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
			System.out.print("(JobID: " +Integer.toString(currentJob.getJobID())+ ") would be manually processed! \n");
		}
		else {
			currentJob.processedTime = currentJob.processedTotalTime + duration;
			currentJob.processedTime = duration;
			System.out.print("Pipeline "+Integer.toString(this.pipelineID)+ " Stage " +Integer.toString(this.stageID)+ " :");
			System.out.print("(JobID: "+Integer.toString(currentJob.getJobID())+") would be automatically processed! \n");
		}
		
		
		grid.moveTo(currentJob, this.getX(), this.getY()); 
		//TODO: I should make this code simpler
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
		System.out.print("(JobID: " +Integer.toString(currentJob.getJobID())+ ") is being handled. Remaining time is "+ Integer.toString(currentJob.processedTime) +"\n");

		if (currentJob.processedTime == 0) {
			currentJob.isHandled = true;
			System.out.print("Pipeline "+Integer.toString(this.pipelineID)+ " Stage " +Integer.toString(this.stageID)+ " :");
			System.out.print("(JobID: " +Integer.toString(currentJob.getJobID())+") is Finished \n");
			shouldIabortCurrentJob(currentJob);
			
			this.jobQueue.remove(currentJob);
			this.numJobs--;		
			jobsProcessedByStage--;
		}
		return jobsProcessedByStage;
	}
	
	public void shouldIabortCurrentJob(Job currentJob) {
		//boolean shoudStageAbort = new Random().nextBoolean();
		boolean isAborted = new DistributedRandomNumberGenerator().getDistributedBoolean(abortRatio); 
		currentJob.isAborted = isAborted;
		if (!isAborted) {
			System.out.print("Pipeline "+Integer.toString(this.pipelineID)+ " Stage " +Integer.toString(this.stageID)+ " :");
			System.out.print("(JobID: " +Integer.toString(currentJob.getJobID())+ ") is not aborted! \n");
		}
		else {
			currentJob.processedTotalTime = currentJob.processedTotalTime + abortWaitingTime;
			currentJob.abortWaitingTime = abortWaitingTime;
			System.out.print("Pipeline "+Integer.toString(this.pipelineID)+ " Stage " +Integer.toString(this.stageID)+ " :");
			System.out.print("(JobID: " +Integer.toString(currentJob.getJobID())+ ") is aborted! \n");
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
