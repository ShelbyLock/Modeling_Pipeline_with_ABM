package jobs;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import stages.Stage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Job {
	//Basic Information
	private Map<Integer, ArrayList<Stage>> pipelines;
	private int num_pipelines;
	private int num_stages;
	
	//Process Information
	private Stage nextStage;
	private int nextStageID;
	public ArrayList<Stage> stageHistoryList = new ArrayList<Stage>();
	private int nextPipelineID;
	private int abortedRestartStageID = 1;
	public boolean isHandled;
	public boolean isAborted;
	
	//Job properties
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
		
		if (isAborted)
			nextStageID = abortedRestartStageID;
		else
			nextStageID = RandomHelper.nextIntFromTo (1, num_stages);
		
		if (isHandled) {
			//Go to a random stage
			nextPipelineID = RandomHelper.nextIntFromTo (0, num_pipelines - 1);
			ArrayList<Stage> sameStageInDifferentPipelines = pipelines.get(nextStageID);
			nextStage = sameStageInDifferentPipelines.get(nextPipelineID);
			stageHistoryList.add(nextStage);
			nextStage.addNewJob(this);
			
			//decide if it is manual
			nextStage.isManual = new Random().nextBoolean();
			isHandled = false;
		} 
	}
}

