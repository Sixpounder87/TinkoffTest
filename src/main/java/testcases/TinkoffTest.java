package testcases;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import utils.ReadPropertyData;

public class TinkoffTest {
	private WebDriver driver;
	private WebDriverWait wait;
	private final static int TIMEOUTsec = 20;

	@BeforeClass
	public void preconditions() {
		System.setProperty("webdriver.chrome.driver",
				ReadPropertyData.getDriverPath());
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, TIMEOUTsec);
	}

	@Test
	public void testTinkoff() {

		driver.get(ReadPropertyData.getBaseUrl());
		// //////////////////////////////////////////////////////////////////////////////////
		retryingFindClick(By.cssSelector("div:nth-child(5) > a > span"));
		// //////////////////////////////////////////////////////////////////////////////////
		retryingFindClick(By.xpath("//*[text()='Коммунальные платежи']"));
		// ////////////////////////////////////////////////////////////////////////////////////
		
		
		WebElement region = wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//h1/span[2]")));
		String regionValue = region.getText();

		if (!regionValue.equals("Москве")) {
			region.click();
			retryingFindClick(By.xpath("//div[2]/div[1]/div[1]/span"));
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// //////////////////////////////////////////////////////////////////////////////////////////
		final WebElement provider = wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//li[1]/a[2]/span")));
		assertEquals(provider.getText(), "ЖКУ-Москва");
		provider.click();
		retryingFindClick(By.cssSelector("div > li:nth-child(2) span > span"));
		// ////////////////////////////////////////////////////////////////////////////////////////
		final List<String> errorPayerCodeValues = Arrays.asList("123",
				"jhcdjc", "&^%#", "123cfdf43орп ус");
		checkInputValues(errorPayerCodeValues, By.cssSelector("#payerCode"),
				"Поле неправильно заполнено");
		/*
		 * checkInputValues(errorPayerCodeValues,
		 * By.cssSelector("input[name='provider-period']"),
		 * "Поле заполнено некорректно"); checkInputValues(
		 * errorPayerCodeValues, By.cssSelector(
		 * "div > div > div > div > div.ui-input.ui-input_error input"),
		 * "Поле неправильно заполнено]");
		 */

		/*
		 * final List<String> emptyPayerCodeValues = Arrays.asList("", " ",
		 * "	"); checkInputValues(emptyPayerCodeValues,
		 * By.cssSelector("#payerCode"), "Поле обязательное");
		 */
		// ///////////////////////////////////////////////////////////////////////////////////////////

		retryingFindClick(By.cssSelector("div:nth-child(5) > a > span"));

		// retryingSendKeys(By.cssSelector("span.ui-search-input__placeholder"),
		// "ЖКУ-Москва");
		final WebElement searchFiled = wait.until(
				ExpectedConditions.presenceOfElementLocated(By
						.cssSelector("input.ui-search-input__input")));
		searchFiled.sendKeys("ЖКУ-Москва");
		
		final WebElement searchResult = wait.until(
				ExpectedConditions.presenceOfElementLocated(By
						.cssSelector("div:nth-child(1) > span div.ui-search-flat__title-box")));
		assertEquals(searchResult.getText(), "ЖКУ-Москва");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@AfterClass
	public void postconditions() {
		driver.quit();
	}

	private boolean retryingFindClick(By by) {
		return retryingWebElementAction(() -> wait.until(
				ExpectedConditions.presenceOfElementLocated(by)).click());
	}

	private boolean retryingSendKeys(By by, String inputeValue) {
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
		while (attempts < 2) {
			try {
				action.run();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
				attempts++;
			}
		}
		return result;
	}

	private void checkInputValues(List<String> data, By by, String errorMessage) {
		for (String inputValue : data) {
			retryingSendKeys(by, inputValue);
			retryingFindClick(By.cssSelector("button.ui-button_provider-pay"));
			final WebElement currentErrorMessage = wait
					.until(ExpectedConditions.presenceOfElementLocated(By
							.xpath("//form/div[1]/div/div[2]")));
			assertEquals(currentErrorMessage.getText(), errorMessage);
		}
	}
}
