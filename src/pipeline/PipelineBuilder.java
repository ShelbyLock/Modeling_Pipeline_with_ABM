package pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import stages.Stages;

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
		//get Pipeline: the first argument is the number of pipeline
		int num_pipelines = 5;
		int num_stages = 6; 
		//*************************************************************************************
		//Build Pipeline
		int total_num_pipeline = num_pipelines;
		ArrayList<Stages> sCommit = new ArrayList<Stages>(total_num_pipeline);
		ArrayList<Stages> sBuild = new ArrayList<Stages>(total_num_pipeline);
		ArrayList<Stages> sQA = new ArrayList<Stages>(total_num_pipeline);
		ArrayList<Stages> sUAT = new ArrayList<Stages>(total_num_pipeline);
		ArrayList<Stages> sSTG = new ArrayList<Stages>(total_num_pipeline);
		ArrayList<Stages> sProd = new ArrayList<Stages>(total_num_pipeline);
				
		int totol_num_stages = num_stages;
		Map<Integer, ArrayList<Stages>> pipeline = new HashMap<>(totol_num_stages);
				
		for (int i = 1; i <= total_num_pipeline; i++) {
			Stages s1 = new Stages(i,1);
			Stages s2 = new Stages(i,2);
			Stages s3 = new Stages(i,3);
			Stages s4 = new Stages(i,4);
			Stages s5 = new Stages(i,5);
			Stages s6 = new Stages(i,6);
				
			sCommit.add(s1);
			sBuild.add(s2);
			sQA.add(s3);
			sUAT.add(s4);
			sSTG.add(s5);
			sProd.add(s6);
					
			context.add(s1);
			context.add(s2);
			context.add(s3);
			context.add(s4);
			context.add(s5);
			context.add(s6);
				
			grid.moveTo(s1, s1.getX(), s1.getY());
			grid.moveTo(s2, s2.getX(), s2.getY());
			grid.moveTo(s3, s3.getX(), s3.getY());
			grid.moveTo(s4, s4.getX(), s4.getY());
			grid.moveTo(s5, s5.getX(), s5.getY());
			grid.moveTo(s6, s6.getX(), s6.getY());
		}
		pipeline.put(1, sCommit);
		pipeline.put(2, sBuild);
		pipeline.put(3, sQA);
		pipeline.put(4, sUAT);
		pipeline.put(5, sSTG);
		pipeline.put(6, sProd);
		
		int jobCount = 100;
		for (int i = 0; i < jobCount ; i++) {
			Job new_job = new Job(num_pipelines, num_stages, grid, pipeline);
			context.add(new_job);
		}
		return context;
	}
	

}
