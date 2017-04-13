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

		for (String inputValue : errorPayerCodeValues) {
			retryingSendKeys(By.cssSelector("#payerCode"), inputValue);
			retryingFindClick(By.cssSelector("button.ui-button_provider-pay"));
			final WebElement errorMessage = wait.until(ExpectedConditions
					.presenceOfElementLocated(By
							.xpath("//form/div[1]/div/div[2]")));
			assertEquals(errorMessage.getText(), "Поле неправильно заполнено");
		}

		final List<String> emptyPayerCodeValues = Arrays.asList("", " ", "	");
		for (String inputValue : emptyPayerCodeValues) {
			retryingSendKeys(By.cssSelector("#payerCode"), inputValue);
			retryingFindClick(By.cssSelector("button.ui-button_provider-pay"));
			final WebElement errorMessage = wait.until(ExpectedConditions
					.presenceOfElementLocated(By
							.xpath("//form/div[1]/div/div[2]")));
			assertEquals(errorMessage.getText(), "Поле обязательное");
		}
		// ///////////////////////////////////////////////////////////////////////////////////////////
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

}
