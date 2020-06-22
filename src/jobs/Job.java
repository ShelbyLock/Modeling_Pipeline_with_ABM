package jobs;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import stages.Stage;
import stages.StageHistory;
import java.util.ArrayList;
import java.util.Map;

import helper.DistributedRandomNumberGenerator;

public class Job {

	//Basic Information
	public Map<Integer, ArrayList<Stage>> pipelines;
	private int num_pipelines;
	private int num_stages;
	private int abortedRestartStageID = 0;
	private double manualRatio;
	
	//Process Information
	public ArrayList<StageHistory> stageHistoryList = new ArrayList<StageHistory>();
	public boolean isHandled;
	public boolean isAborted;
	public boolean isManual;
	public int abortWaitingTime = 0;
	
	//Job properties
	private int jobID;
	public int processedTotalTime = 0;
	public int processedTime = 0;
	//TODO: Record waiting Time
	public int waitingTIme;
	//TODO: List or single duration Process determined by distribution, overwrite duration attributes in stage
	public int timeProfileInStage;
	//TODO: The probability that after it deploys and it goes to build again 1) it could be a new component 2) or a requested rework component;
	double reworkProbability;
	
	
	public Job (int jobID, Parameters params, Map<Integer, ArrayList<Stage>> pipelines){	
		this.jobID = jobID;
		this.pipelines = pipelines;
		
		this.num_pipelines = params.getInteger("num_pipelines"); 
		this.num_stages = params.getInteger("num_stages"); 
		this.abortedRestartStageID = params.getInteger("abortedRestartStageID");
		this.abortWaitingTime = 0;
		this.manualRatio = params.getDouble("manualRatio");
		
		this.isHandled = false;
		this.isAborted = false;	
	}
	
	@ScheduledMethod(start = 2, interval = 1)
	public void changeStage() {
		if (this.isHandled) {
			int nextStageID;
			if (this.isAborted) {
				abortWaiting();
				//TODO: Build is with h the highest probability, but I can resatrt in other stages.  
				nextStageID = abortedRestartStageID;
			}
			else {
				nextStageID = RandomHelper.nextIntFromTo (0, this.num_stages-1);
				moveNextStage(nextStageID);
			}
		} 
	}
	
	public void moveNextStage(int nextStageID) {
		//Go to a random stage
		int nextPipelineID = RandomHelper.nextIntFromTo (0, this.num_pipelines - 1);
		System.out.print("Job: (JobID: "+Integer.toString(this.getJobID())+") next stage is "+Integer.toString(nextStageID)+ "\n");
		System.out.print("Job: (JobID: "+Integer.toString(this.getJobID())+") next pipeline is "+Integer.toString(nextPipelineID)+ "\n");
		
		ArrayList<Stage> sameStageInDifferentPipelines = pipelines.get(nextStageID);
		Stage nextStage = sameStageInDifferentPipelines.get(nextPipelineID);
		this.stageHistoryList.add(new StageHistory(nextPipelineID, nextStageID));
		nextStage.addNewJob(this);
		
		//decide if it is manual
		//this.isManual = new Random().nextBoolean();
		this.isManual = new DistributedRandomNumberGenerator().getDistributedBoolean(manualRatio); 
		this.isHandled = false;
	}
	
	public void abortWaiting() {
		this.abortWaitingTime--;
		System.out.print("Job: (JobID: "+Integer.toString(this.getJobID())+")'s remaining abort handling time "+Integer.toString(this.abortWaitingTime)+ "\n");
		if (this.abortWaitingTime == 0) {
			this.isAborted = false;
		}
	}
	
	
	public int getJobID() {
		return jobID;
	}

	public void setJobID(int jobID) {
		this.jobID = jobID;
	}
}

