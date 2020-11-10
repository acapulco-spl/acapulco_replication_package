package acapulco.evaluation.paretotruefront;

import java.util.Arrays;

/**
 * Representation of a point in the pareto front.
 *
 */
public class Point {
	private double[] point;
	
	public Point(double[] p) {
		this.point = p;
	}

	public double[] getPoint() {
		return point;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(point);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (!Arrays.equals(point, other.point))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [point=" + Arrays.toString(point) + "]";
	}
	
	
}
