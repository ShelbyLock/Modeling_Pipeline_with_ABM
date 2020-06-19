package jobs;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import stages.Stage;
import stages.StageHistory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Job {
	//Basic Information
	public Map<Integer, ArrayList<Stage>> pipelines;
	private int num_pipelines;
	private int num_stages;
	
	//Process Information
	public ArrayList<StageHistory> stageHistoryList = new ArrayList<StageHistory>();
	private int abortedRestartStageID = 0;
	public boolean isHandled;
	public boolean isAborted;
	
	//Job properties
	@SuppressWarnings("unused")
	private int jobID;
	public int processedTime = 0;
	
	public Job (int jobID, int num_pipelines, int num_stages, Map<Integer, ArrayList<Stage>> pipelines){	
		this.jobID = jobID;
		this.pipelines = pipelines;
		this.num_pipelines = num_pipelines;
		this.num_stages = num_stages;
		
		
		this.isHandled = false;
		this.isAborted = false;	
		//this.time = 0;
	}
	
	@ScheduledMethod(start = 2, interval = 1)
	public void changeState() {
		int nextStageID;
		if (this.isAborted)
			nextStageID = abortedRestartStageID;
		else
			nextStageID = RandomHelper.nextIntFromTo (0, this.num_stages-1);
		
		if (this.isHandled) {
			moveToDifferentStage(nextStageID);
		} 
	}
	
	public void moveToDifferentStage(int nextStageID) {
		//Go to a random stage
		int nextPipelineID = RandomHelper.nextIntFromTo (0, this.num_pipelines - 1);
		ArrayList<Stage> sameStageInDifferentPipelines = pipelines.get(nextStageID);
		Stage nextStage = sameStageInDifferentPipelines.get(nextPipelineID);
		this.stageHistoryList.add(new StageHistory(nextPipelineID, nextStageID));
		nextStage.addNewJob(this);
		
		//decide if it is manual
		nextStage.isManual = new Random().nextBoolean();
		this.isHandled = false;
	}
}

