package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import utils.TimeUtil;

public class ZhkuMoscow extends CommonPage {

	private static final String URL_MATCH = "https://www.tinkoff.ru/zhku-moskva/";
	private static final By PAY_TAB_LOCATOR = By
			.cssSelector("div > li:nth-child(2) span > span");
	private static final By PAYER_CODE_LOCATOR = By.cssSelector("#payerCode");
	private static final By ERROR_PAYER_CODE_LOCATOR = By
			.xpath("//form/div[1]/div/div[2]");
	private static final By PERIOD_LOCATOR = By
			.cssSelector("input[name='provider-period']");
	private static final By ERROR_PERIOD_LOCATOR = By
			.xpath("//form/div[2]/div/div[2]");
	private static final By PAYMENT_LOCATOR = By
			.xpath("//div/div/div/div/div/div/label/div[1]/input");
	private static final By ERROR_PAYMENT_LOCATOR = By
			.xpath("//div[4]//div/div/div/div[2]");
	private static final By PAY_BUTTON = By
			.cssSelector("button.ui-button_provider-pay");

	public ZhkuMoscow(WebDriver driver) {
		TimeUtil.sleepTimeoutSec(2);
		if (!driver.getCurrentUrl().contains(URL_MATCH)) {
			throw new IllegalStateException(
					"This is not the page you are expected");
		}

		this.driver = driver;
	}

	public ZhkuMoscow getPayTab() {
		if (!retryingFindClick(PAY_TAB_LOCATOR)) {
			throw new NoSuchElementException("Cannot click on the element");
		}
		TimeUtil.sleepTimeoutSec(2);
		return this;
	}

	public String getInputErrorPayerCode(String inputValue) {
		retryingSendKeys(PAYER_CODE_LOCATOR, inputValue);
		retryingFindClick(PAY_BUTTON);
		return retryingFindElement(ERROR_PAYER_CODE_LOCATOR).getText();
	}

	public void setPayerCode(String inputValue) {
		retryingSendKeys(PAYER_CODE_LOCATOR, inputValue);
		TimeUtil.sleepTimeoutSec(2);
	}

	public String getInputErrorPeriod(String inputValue) {
		retryingSendKeys(PERIOD_LOCATOR, inputValue);
		retryingFindClick(PAY_BUTTON);
		return retryingFindElement(ERROR_PERIOD_LOCATOR).getText();
	}

	public void setPeriod(String inputValue) {
		retryingSendKeys(PERIOD_LOCATOR, inputValue);
		TimeUtil.sleepTimeoutSec(2);
	}

	public String getInputErrorPayment(String inputValue) {
		retryingSendKeys(PAYMENT_LOCATOR, inputValue);
		retryingFindClick(PAY_BUTTON);
		return retryingFindElement(ERROR_PAYMENT_LOCATOR).getText();
	}
}
