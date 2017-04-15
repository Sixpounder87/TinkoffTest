package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TimeUtil;

public class CommunalPayments extends CommonPage {

	private static final String URL_MATCH = "https://www.tinkoff.ru/payments/kommunalnie-platezhi/";
	private static final By REGION_LOCATOR = By.xpath("//h1/span[2]");
	private static final By MOSCOW_REGION_LOCATOR = By
			.xpath("//div[2]/div/div[1]/span");
	private static final By PETERBURG_REGION_LOCATOR = By
			.xpath("//div[2]/div/div[2]/span");
	private static final By FIRST_PROVIDER_LOCATOR = By
			.xpath("//li[1]/a[2]/span");
	private static final By ZHKU_MOSCOW_LOCATOR = By
			.cssSelector("a[title='ЖКУ-Москва']");

	private String providerName;

	public CommunalPayments(WebDriver driver) {
		TimeUtil.sleepTimeoutSec(2);
		if (!driver.getCurrentUrl().contains(URL_MATCH)) {
			throw new IllegalStateException(
					"This is not the page you are expected");
		}

		this.driver = driver;
		this.wait = new WebDriverWait(driver, TIMEOUTsec);
	}

	public CommunalPayments setMoscowRegion() {
		WebElement region = retryingFindElement(REGION_LOCATOR);
		TimeUtil.sleepTimeoutSec(2);
		if (!region.getText().equals("Москве")) {
			setRegion(region, MOSCOW_REGION_LOCATOR);
		}
		return this;
	}

	public CommunalPayments setPeterburgRegion() {
		WebElement region = retryingFindElement(REGION_LOCATOR);
		TimeUtil.sleepTimeoutSec(2);
		if (!region.getText().equals("Санкт-Петербурге")) {
			setRegion(region, PETERBURG_REGION_LOCATOR);
		}
		return this;
	}

	private void setRegion(WebElement region, By by) {
		region.click();
		if (!retryingFindClick(by)) {
			throw new NoSuchElementException("Cannot click on the element");
		}
		TimeUtil.sleepTimeoutSec(2);
	}

	private void findProvider() {
		WebElement provider = retryingFindElement(FIRST_PROVIDER_LOCATOR);
		providerName = provider.getText();
	}

	public String getProviderName() {
		findProvider();
		return providerName;
	}

	public ZhkuMoscow getProviderPage() {
		if (!retryingFindClick(FIRST_PROVIDER_LOCATOR)) {
			throw new NoSuchElementException("Cannot click on the element");
		}
		TimeUtil.sleepTimeoutSec(2);
		return new ZhkuMoscow(driver);
	}

	public WebElement getZhkuMoscow() {
		WebElement element;
		try {
			element = retryingFindElement(ZHKU_MOSCOW_LOCATOR);
		} catch (TimeoutException e) {
			element = null;
		}
		return element;
	}
}
