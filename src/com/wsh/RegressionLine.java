package com.wsh;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 
 * @ClassName: RegressionLine
 * @Description: TODO
 * @author BIGMAN
 * @date 2017年1月5日下午5:52:25
 *
 */
public class RegressionLine {

	private double sumX;

	private double sumY;

	private double sumXX;

	private double sumXY;

	private double sumYY;

	private double sumDeltaY;

	private double sumDeltaY2;

	private double sse;

	private double sst;

	private double E;

	private String[] xy;

	private ArrayList listX;

	private ArrayList listY;

	private int XMin, XMax, YMin, YMax;

	private float a0;

	private float a1;

	private int pn;

	private boolean coefsValid;

	/**
	 * Constructor.
	 */
	public RegressionLine() {
		XMax = 0;
		YMax = 0;
		pn = 0;
		xy = new String[2];
		listX = new ArrayList();
		listY = new ArrayList();
	}

	/**
	 * 
	 * RegressionLine.
	 * 
	 * @param data
	 */
	public RegressionLine(DataPoint data[]) {
		pn = 0;
		xy = new String[2];
		listX = new ArrayList();
		listY = new ArrayList();
		for (int i = 0; i < data.length; ++i) {
			addDataPoint(data[i]);
		}
	}

	/**
	 * Return the current number of data points.
	 * 
	 * @return the count
	 */
	public int getDataPointCount() {
		return pn;
	}

	/**
	 * Return the coefficient a0.
	 * 
	 * @return the value of a0
	 */
	public float getA0() {
		validateCoefficients();
		return a0;
	}

	/**
	 * Return the coefficient a1.
	 * 
	 * @return the value of a1
	 */
	public float getA1() {
		validateCoefficients();
		return a1;
	}

	/**
	 * Return the sum of the x values.
	 * 
	 * @return the sum
	 */
	public double getSumX() {
		return sumX;
	}

	/**
	 * Return the sum of the y values.
	 * 
	 * @return the sum
	 */
	public double getSumY() {
		return sumY;
	}

	/**
	 * Return the sum of the x*x values.
	 * 
	 * @return the sum
	 */
	public double getSumXX() {
		return sumXX;
	}

	/**
	 * Return the sum of the x*y values.
	 * 
	 * @return the sum
	 */
	public double getSumXY() {
		return sumXY;
	}

	public double getSumYY() {
		return sumYY;
	}

	public int getXMin() {
		return XMin;
	}

	public int getXMax() {
		return XMax;
	}

	public int getYMin() {
		return YMin;
	}

	public int getYMax() {
		return YMax;
	}

	/**
	 * Add a new data point: Update the sums.
	 * 
	 * @param dataPoint
	 *            the new data point
	 */
	public void addDataPoint(DataPoint dataPoint) {
		sumX += dataPoint.x;
		sumY += dataPoint.y;
		sumXX += dataPoint.x * dataPoint.x;
		sumXY += dataPoint.x * dataPoint.y;
		sumYY += dataPoint.y * dataPoint.y;

		if (dataPoint.x > XMax) {
			XMax = (int) dataPoint.x;
		}
		if (dataPoint.y > YMax) {
			YMax = (int) dataPoint.y;
		}

		xy[0] = (int) dataPoint.x + "";
		xy[1] = (int) dataPoint.y + "";
		if (dataPoint.x != 0 && dataPoint.y != 0) {
			System.out.print(xy[0] + ",");
			System.out.println(xy[1]);

			try {
				listX.add(pn, xy[0]);
				listY.add(pn, xy[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		++pn;
		coefsValid = false;
	}

	public float at(int x) {
		if (pn < 2)
			return Float.NaN;

		validateCoefficients();
		return a0 + a1 * x;
	}

	/**
	 * Reset.
	 */
	public void reset() {
		pn = 0;
		sumX = sumY = sumXX = sumXY = 0;
		coefsValid = false;
	}

	/**
	 * Validate the coefficients. 计算方程系数 y=ax+b 中的a
	 */
	private void validateCoefficients() {
		if (coefsValid)
			return;

		if (pn >= 2) {
			float xBar = (float) sumX / pn;
			float yBar = (float) sumY / pn;

			a1 = (float) ((pn * sumXY - sumX * sumY) / (pn * sumXX - sumX * sumX));
			a0 = (float) (yBar - a1 * xBar);
		} else {
			a0 = a1 = Float.NaN;
		}

		coefsValid = true;
	}

	/**
	 * 返回误差
	 */
	public double getR() {
		// 遍历这个list并计算分母
		for (int i = 0; i < pn - 1; i++) {
			float Yi = (float) Integer.parseInt(listY.get(i).toString());
			float Y = at(Integer.parseInt(listX.get(i).toString()));
			float deltaY = Yi - Y;
			float deltaY2 = deltaY * deltaY;

			sumDeltaY2 += deltaY2;

		}

		sst = sumYY - (sumY * sumY) / pn;
		// System.out.println("sst:" + sst);
		E = 1 - sumDeltaY2 / sst;

		return round(E, 4);
	}

	// 用于实现精确的四舍五入
	public double round(double v, int scale) {

		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	public float round(float v, int scale) {

		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();

	}
}