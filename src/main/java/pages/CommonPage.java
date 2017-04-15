package pages;

import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TimeUtil;

public abstract class CommonPage {

	protected final static int TIMEOUTsec = 20;
	protected final static By PAYMENTS_LOCATOR = By
			.cssSelector("nav > div:nth-child(5) > a > span");

	protected WebDriver driver;

	protected WebDriverWait wait;

	public PaymentPage getPaymentPage() {
		if (!retryingFindClick(PAYMENTS_LOCATOR)) {
			throw new NoSuchElementException("Cannot click on the element");
		}
		TimeUtil.sleepTimeoutSec(2);
		return new PaymentPage(driver);
	}

	protected boolean retryingFindClick(By by) {
		return retryingWebElementAction(() -> driver.findElement(by).click());
	}

	protected WebElement retryingFindElement(By by) {
		return retryingGetWebElement(() -> wait.until(ExpectedConditions
				.presenceOfElementLocated(by)));
	}

	protected boolean retryingSendKeys(By by, String inputeValue) {
		return retryingWebElementAction(() -> {
			final WebElement inputField = wait.until(ExpectedConditions
					.presenceOfElementLocated(by));
			inputField.clear();
			inputField.sendKeys(inputeValue);
		});
	}

	private boolean retryingWebElementAction(Runnable action) {
		boolean result = false;
		int attempts = 0;
		while (attempts < 3) {
			try {
				action.run();
				System.out.println("pressed");
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
				attempts++;
			}
		}
		return result;
	}

	private WebElement retryingGetWebElement(Supplier<WebElement> action) {
		WebElement result = null;
		int attempts = 0;
		while (attempts < 3) {
			try {
				result = action.get();
				break;
			} catch (StaleElementReferenceException e) {
				attempts++;
			}
		}
		return result;
	}
}
