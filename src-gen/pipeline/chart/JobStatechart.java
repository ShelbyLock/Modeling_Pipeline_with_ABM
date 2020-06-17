
package pipeline.chart;

import java.util.Map;
import java.util.HashMap;

import repast.simphony.statecharts.*;
import repast.simphony.statecharts.generator.GeneratedFor;

import pipeline.*;

@GeneratedFor("_FOTdkKsuEeqdKOUctkVUVA")
public class JobStatechart extends DefaultStateChart<pipeline.Job> {

	public static JobStatechart createStateChart(pipeline.Job agent, double begin) {
		JobStatechart result = createStateChart(agent);
		StateChartScheduler.INSTANCE.scheduleBeginTime(begin, result);
		return result;
	}

	public static JobStatechart createStateChart(pipeline.Job agent) {
		JobStatechartGenerator generator = new JobStatechartGenerator();
		return generator.build(agent);
	}

	private JobStatechart(pipeline.Job agent) {
		super(agent);
	}

	private static class MyStateChartBuilder extends StateChartBuilder<pipeline.Job> {

		public MyStateChartBuilder(pipeline.Job agent, AbstractState<pipeline.Job> entryState, String entryStateUuid) {
			super(agent, entryState, entryStateUuid);
			setPriority(0.0);
		}

		@Override
		public JobStatechart build() {
			JobStatechart result = new JobStatechart(getAgent());
			setStateChartProperties(result);
			return result;
		}
	}

	private static class JobStatechartGenerator {

		private Map<String, AbstractState<Job>> stateMap = new HashMap<String, AbstractState<Job>>();

		public JobStatechart build(Job agent) {
			throw new UnsupportedOperationException("Statechart has not been defined.");

		}

		private void createTransitions(MyStateChartBuilder mscb) {

		}

	}
}
