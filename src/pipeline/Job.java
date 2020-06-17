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
	private Map<Integer, ArrayList<Stages>> pipelines;
	private int num_pipelines;
	private int num_stages;
	private boolean justStarted = true;
	
	private int time = 0;
	public Job (int num_pipelines, int num_stages, Grid <Object > grid, Map<Integer, ArrayList<Stages>> pipelines){	
		this.grid = grid;
		this.pipelines = pipelines;
		this.num_pipelines = num_pipelines;
		this.num_stages = num_stages;
		
		this.time = 0;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void changeState() {
		if (justStarted) {
			int pipelineID = RandomHelper.nextIntFromTo (0, num_pipelines - 1);
			//went to the commit stage in a random pipeline
			Stages commit = pipelines.get(1).get(pipelineID);
			grid.moveTo(this, commit.getX(), commit.getY());
			justStarted = false;
		}else
			moveStage();
		
	}
	@SuppressWarnings("unchecked")
	private void moveStage() {
		//The context which the job is operating on.
		Context<Object> context = ContextUtils.getContext (this); 
		//The network that paint the traces of the job
		Network<Object> net = ( Network <Object>) context.getProjection ("infection network");		
		GridPoint pt = grid.getLocation (this);
		Object temp = grid.getObjectAt(pt.getX(), pt.getY());	
		
		//Go to a random stage : this does not include the commit stage
		int stageID = RandomHelper.nextIntFromTo (2, num_stages);
		ArrayList<Stages> stage = pipelines.get(stageID);
		int pipelineID = RandomHelper.nextIntFromTo (0, num_pipelines - 1);	
		Stages s = stage.get(pipelineID);
		grid.moveTo(this, s.getX(), s.getY()); 
		net.addEdge(temp, this);
		s.addNewJob();
	}
	/*
	
	public void MoveToStage(Network<Object> net, Object temp, ArrayList<Stages> stage) {

		while(true) {
			int pipelineID = RandomHelper.nextIntFromTo (1, num_pipelines);	
			Stages s = stage.get(pipelineID);
			Object mutex = new Object();
			synchronized (mutex) {
				if (s.isNewJobAllowed()) {
					grid.moveTo(this, s.getX(), s.getY()); 
					net.addEdge(temp, this);
					s.addNewJob();
					break;
				}
			}
		}			
	}*/
}

