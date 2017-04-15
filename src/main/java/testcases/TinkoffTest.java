package testcases;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.CommunalPayments;
import pages.HomePage;
import pages.PaymentPage;
import pages.ZhkuMoscow;
import utils.ReadPropertyData;

public class TinkoffTest {

	private final static String PROVIDER_NAME = "ЖКУ-Москва";
	private final static String WRONG_FIELD_VALUE = "Поле неправильно заполнено";
	private final static String MANDATORY_FIELD_VALUE = "Поле обязательное";
	private final static String NOT_CORRECT_FIELD_VALUE = "Поле заполнено некорректно";
	private final static String MIN_FIELD_VALUE = "Минимальная сумма перевода - 10";
	private final static String MAX_FIELD_VALUE = "Максимальная сумма перевода - 15";

	private WebDriver driver;
	private String paymentURL;

	@BeforeClass
	public void preconditions() {
		System.setProperty("webdriver.chrome.driver",
				ReadPropertyData.getDriverPath());
		driver = new ChromeDriver();
	}

	@Test
	public void test01GetZhkuMoscowPaymentTab() {

		driver.get(ReadPropertyData.getBaseUrl());

		final HomePage homePage = new HomePage(driver);
		final PaymentPage paymentPage = homePage.getPaymentPage();
		final CommunalPayments communalPayments = paymentPage.payCommunal();
		communalPayments.setMoscowRegion();

		assertEquals(communalPayments.getProviderName(), PROVIDER_NAME);

		final ZhkuMoscow zhkuMoscow = communalPayments.getProviderPage();
		paymentURL = driver.getCurrentUrl();
		zhkuMoscow.getPayTab();
	}

	@Test(dataProvider = "payerCodeData")
	public void test02ZhkuMoscowPayerCodeData(final String data,
			final String errorMessage) {

		driver.get(driver.getCurrentUrl());

		final ZhkuMoscow zhkuMoscow = new ZhkuMoscow(driver);
		final String message = zhkuMoscow.getInputErrorPayerCode(data);
		assertEquals(message, errorMessage);
	}

	@Test(dataProvider = "periodData")
	public void test03ZhkuMoscowPeriodData(final String data,
			final String errorMessage) {

		driver.get(driver.getCurrentUrl());

		final ZhkuMoscow zhkuMoscow = new ZhkuMoscow(driver);
		final String message = zhkuMoscow.getInputErrorPeriod(data);
		assertEquals(message, errorMessage);
	}

	@Test(dataProvider = "paymentData")
	public void test04ZhkuMoscowPaymentSumData(final String data,
			final String errorMessage) {

		driver.get(driver.getCurrentUrl());

		final ZhkuMoscow zhkuMoscow = new ZhkuMoscow(driver);
		zhkuMoscow.setPayerCode("2727637623");
		zhkuMoscow.setPeriod("042017");
		final String message = zhkuMoscow.getInputErrorPayment(data);
		assertTrue(message.contains(errorMessage));
	}

	@Test
	public void test05SearchProvider() {
		driver.get(driver.getCurrentUrl());
		final ZhkuMoscow zhkuMoscow = new ZhkuMoscow(driver);
		final PaymentPage paymentPage = zhkuMoscow.getPaymentPage();
		paymentPage.inputSearch(PROVIDER_NAME);
		final String providerName = paymentPage.getFirstProviderName();
		assertEquals(providerName, PROVIDER_NAME);
		paymentPage.chooseFirstProvider();
		assertEquals(driver.getCurrentUrl(), paymentURL);
	}

	@Test
	public void test06StPeterburg() {
		driver.get(driver.getCurrentUrl());
		final ZhkuMoscow zhkuMoscow = new ZhkuMoscow(driver);
		final PaymentPage paymentPage = zhkuMoscow.getPaymentPage();
		final CommunalPayments communalPayments = paymentPage.payCommunal();
		communalPayments.setPeterburgRegion();
		assertNull(communalPayments.getZhkuMoscow());
	}

	@AfterClass
	public void postconditions() {
		driver.quit();
	}

	@DataProvider
	private String[][] payerCodeData() {
		return new String[][] { { "123", WRONG_FIELD_VALUE },
				{ "jhcdjc", WRONG_FIELD_VALUE }, { "&^%#", WRONG_FIELD_VALUE },
				{ "123cfdf43орп ус", WRONG_FIELD_VALUE },
				{ " ", MANDATORY_FIELD_VALUE } };
	}

	@DataProvider
	private String[][] periodData() {
		return new String[][] { { "456345", NOT_CORRECT_FIELD_VALUE },
				{ "123", NOT_CORRECT_FIELD_VALUE },
				{ "11.999", NOT_CORRECT_FIELD_VALUE } };
	}

	@DataProvider
	private String[][] paymentData() {
		return new String[][] { { "20000\n", MAX_FIELD_VALUE },
				{ "0\n", MIN_FIELD_VALUE } };
	}
}
