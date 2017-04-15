package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TimeUtil;

public class PaymentPage extends CommonPage {

	private static final String URL_MATCH = "https://www.tinkoff.ru/payments/";
	private static final By COMMUNAL_PAYMENTS_LOCATOR = By
			.xpath("//*[text()='Коммунальные платежи']");
	private static final By SEARCH_LOCATOR = By.cssSelector("div > input");
	private static final By FIRST_ELEMENT_IN_SEARCH_RESULTS_LOCATOR = By
			.cssSelector("div:nth-child(1) > span div.ui-search-flat__title-box");

	public PaymentPage(WebDriver driver) {
		TimeUtil.sleepTimeoutSec(2);
		if (!driver.getCurrentUrl().contains(URL_MATCH)) {
			throw new IllegalStateException(
					"This is not the page you are expected");
		}

		this.driver = driver;
		this.wait = new WebDriverWait(driver, TIMEOUTsec);
	}

	public CommunalPayments payCommunal() {
		if (!retryingFindClick(COMMUNAL_PAYMENTS_LOCATOR)) {
			throw new NoSuchElementException("Cannot click on the element");
		}
		TimeUtil.sleepTimeoutSec(2);
		return new CommunalPayments(driver);
	}

	public void inputSearch(String inputValue) {
		retryingSendKeys(SEARCH_LOCATOR, inputValue);
	}

	public ZhkuMoscow chooseFirstProvider() {
		if (!retryingFindClick(FIRST_ELEMENT_IN_SEARCH_RESULTS_LOCATOR)) {
			throw new NoSuchElementException("Cannot click on the element");
		}
		TimeUtil.sleepTimeoutSec(2);
		return new ZhkuMoscow(driver);
	}

	public String getFirstProviderName() {
		return retryingFindElement(FIRST_ELEMENT_IN_SEARCH_RESULTS_LOCATOR)
				.getText();
	}
}
