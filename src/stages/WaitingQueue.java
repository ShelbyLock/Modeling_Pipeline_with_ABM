package stages;

public class WaitingQueue {
	private int waitingForWhichStage;
	private int x;
	private int y;
	private int currentJobs;
	public WaitingQueue(int waitingForWhichStage, int x, int y, int currentJobs) {
		super();
		this.waitingForWhichStage = waitingForWhichStage;
		this.x = x;
		this.y = y;
		this.currentJobs = currentJobs;
	}
	public int getWaitingForWhichStage() {
		return waitingForWhichStage;
	}
	public void setWaitingForWhichStage(int waitingForWhichStage) {
		this.waitingForWhichStage = waitingForWhichStage;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getCurrentJobs() {
		return currentJobs;
	}
	public void setCurrentJobs(int currentJobs) {
		this.currentJobs = currentJobs;
	}
	
}
