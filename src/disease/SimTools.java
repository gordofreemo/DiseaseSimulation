package disease;

import java.util.Random;

public class SimTools {

	public SimTools() {
		// TODO Auto-generated constructor stub
	}

	public static int getRandom(int rang) {
		Random r = new Random();
		int randNum = r.nextInt(rang) + 1;
		return randNum;
	}

	/**
	 * 从3个固定概率的区间取数
	 * @param rate_a 50
	 * @param rate_b 75
	 * @return
	 */
	public static int getRandom(int rate_a,int rate_b) {
		// 随机产生[0;1;2],0出现的概率为20%，1出现的概率为60%，2出现的概率为20%
		Random r = new Random();
		int a = r.nextInt(100);// 随机产生[0,100)的整数，每个数字出现的概率为1%
		int b; // 需要的结果数字
		if (a < rate_a) { // 前20个数字的区间，代表20%的几率
			b = 0;
		} else if (a < rate_b) {// [20,80)，60个数字的区间，代表60%的几率
			b = 1;
		} else {// [80,100)，20个数字区间，代表20%的几率
			b = 2;
		}
		return b;
	}
	
	public static void main(String[] args) {
		System.out.println(SimTools.getRandom(100));
	}
}
