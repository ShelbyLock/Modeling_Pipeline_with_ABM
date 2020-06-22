package stages;

public class StageHistory {
	private int pipelineID;
	private int stageID;
	
	public StageHistory(int pipelineID, int stageID) {
		super();
		this.pipelineID = pipelineID;
		this.stageID = stageID;
	}
	
	public int getPipelineID() {
		return pipelineID;
	}
	public void setPipelineID(int pipelineID) {
		this.pipelineID = pipelineID;
	}
	public int getStageID() {
		return stageID;
	}
	public void setStageID(int stageID) {
		this.stageID = stageID;
	}
	
}
