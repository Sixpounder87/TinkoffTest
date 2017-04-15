package pages;

import org.openqa.selenium.WebDriver;

import utils.TimeUtil;

public class HomePage extends CommonPage {

	private static final String URL_MATCH = "https://www.tinkoff.ru/";

	public HomePage(WebDriver driver) {
		TimeUtil.sleepTimeoutSec(2);
		if (!driver.getCurrentUrl().contains(URL_MATCH)) {
			throw new IllegalStateException(
					"This is not the page you are expected");
		}

		this.driver = driver;
	}
}
