package stages;

public class Stages {
	private int pipelineID;
	private int stageID;
	private int x;
	private int y;
	private int capacity = 0;
	private int currentJobs = 0;
	
	public Stages(int pipelineID, int stageID) {
		this.pipelineID = pipelineID;
		this.stageID = stageID;
		getInfo();
	}
	
	public void getInfo(){
		int num = this.pipelineID;
		switch(this.stageID) {
		case 1: 
			this.x = 5;
			this.y = 5 + ((num - 1)* 10);
			break;
		case 2:
			this.x = 10;
			this.y = 9 + ((num - 1)* 10);
			this.capacity = 5;
			this.currentJobs = 0;
			break;
		case 3:
			this.x = 20;
			this.y = 1 + ((num - 1)* 10);
			this.capacity = 5;
			this.currentJobs = 0;
			break;
		case 4:
			this.x = 30;
			this.y = 3 + ((num - 1)* 10);
			this.capacity = 5;
			this.currentJobs = 0;
			break;
		case 5:
			this.x = 40;
			this.y = 7 + ((num - 1)* 10);
			this.capacity = 5;
			this.currentJobs = 0;
			break;
		case 6:
			this.x = 45;
			this.y = 5 + ((num - 1)* 10);
			this.capacity = 5;
			this.currentJobs = 0;
			break;
		}
		
	}
	
	public int getX(){
		return x;
	}
	public int getY() {
		return y;
	}
	
	public boolean isNewJobAllowed() {
		return capacity > currentJobs;
	}
	public void addNewJob() {
		currentJobs = currentJobs + 1; 
	}
	
}
