package utils;

public final class TimeUtil {

	public static void sleepTimeoutSec(int timeout) {
		try {
			Thread.sleep(timeout * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
