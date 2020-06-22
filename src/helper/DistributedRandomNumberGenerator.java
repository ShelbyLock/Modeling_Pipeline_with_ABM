package helper;

import java.util.HashMap;
import java.util.Map;

public class DistributedRandomNumberGenerator {

    private Map<Integer, Double> distribution;
    private double distSum;

    public DistributedRandomNumberGenerator() {
        distribution = new HashMap<>();
    }

    public void addNumber(int value, double distribution) {
        if (this.distribution.get(value) != null) {
            distSum -= this.distribution.get(value);
        }
        this.distribution.put(value, distribution);
        distSum += distribution;
    }

    public int getDistributedRandomNumber() {
        double rand = Math.random();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (Integer i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return 0;
    }
    
    public boolean getDistributedBoolean(double ratio) {
		DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
		drng.addNumber(1, ratio);
		drng.addNumber(0, 1-ratio);
		int temp = drng.getDistributedRandomNumber();
		if (temp == 1)
			return true;
		else 
			return false;
    }

}