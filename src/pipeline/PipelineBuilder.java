package pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jobs.Job;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import stages.Stage;

public class PipelineBuilder implements ContextBuilder<Object>{

	@SuppressWarnings("rawtypes")
	@Override
	public Context build(Context<Object> context) {
		//*************************************************************************************
		//set the context
		context.setId ("pipeline");
		GridFactory gridFactory = GridFactoryFinder.createGridFactory ( null );
		//*************************************************************************************
		//Initialize the grid
		// Correct import : import repast . simphony . space . grid . WrapAroundBorders ;
		Grid<Object> grid = gridFactory.createGrid ("grid", context , 
													new GridBuilderParameters <Object>( new WrapAroundBorders(),
															new SimpleGridAdder <Object>(),
															true, 50, 50));
		//*************************************************************************************
		//Initialize the network
		NetworkBuilder<Object> netBuilder = new NetworkBuilder <Object> ("infection network", context , true );
		netBuilder.buildNetwork ();
		//*************************************************************************************
		//Build Pipeline
		int num_stages = 5; 
		int num_pipelines = 6;
		Map<Integer, ArrayList<Stage>> pipeline = new HashMap<>(num_stages);
		for (int j = 1; j <= num_stages; j++) {
			ArrayList<Stage> sameStageInDifferentPipelines = new ArrayList<Stage>(num_pipelines);
			for (int i = 1; i <= num_pipelines; i++) {
				Stage tempStages = new Stage(i,j,grid);
				sameStageInDifferentPipelines.add(tempStages);
				
				context.add(tempStages);
				grid.moveTo(tempStages, tempStages.getX(), tempStages.getY());
			}
			pipeline.put(j, sameStageInDifferentPipelines);
		}
		//*************************************************************************************
		//Create Jobs -> commit Jobs
		int jobCount = 100;
		for (int i = 0; i < jobCount ; i++) {
			Job new_job = new Job(i,num_pipelines, num_stages, grid, pipeline);
			context.add(new_job);
			int pipelineID = RandomHelper.nextIntFromTo (0, num_pipelines - 1);
			Stage build = pipeline.get(1).get(pipelineID);
			build.addNewJob(i, new_job);
		}
		return context;
	}
	

}
