import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class YatraTest {

	
	@Test
	public void yatraAutomation() throws InterruptedException {
				
				ChromeOptions chromeOption = new ChromeOptions();
				chromeOption.addArguments("--start-maximized");
				chromeOption.addArguments("--disable-notifications");
				WebDriver driver = new ChromeDriver(chromeOption);
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
				driver.get("https://www.yatra.com/");
				//pop-up handling using try catch block
				closePopup(wait);
				//clicking on departure date
				clickOnDepartureDate(wait);
				// get current month and next month
				WebElement currentMonthCalender = selectMonthFromCalender(wait, 0);
				WebElement nextMonthCalender = selectMonthFromCalender(wait, 1);
				Thread.sleep(4000);
				String lowestPricingForCurrentMonth = lowestPrice(currentMonthCalender);
				String lowestPricingForNextMonth = lowestPrice(nextMonthCalender);
				System.out.println(lowestPricingForCurrentMonth);
				System.out.println(lowestPricingForNextMonth);
				compareTwoMonthPrice(lowestPricingForCurrentMonth, lowestPricingForNextMonth);

			}

			public static void clickOnDepartureDate(WebDriverWait wait) {
				By departureButtonLocator = By.xpath("//div[@aria-label=\"Departure Date inputbox\" and @role=\"button\"]");
				WebElement departureButton = wait.until(ExpectedConditions.elementToBeClickable(departureButtonLocator));
				departureButton.click();
			}

			public static void closePopup(WebDriverWait wait) {
				By popUpLocator = By.xpath("//div[contains(@class, 'style_popup')][1]");
				try {
				    WebElement popUpElement = wait.until(ExpectedConditions.visibilityOfElementLocated(popUpLocator));
				   WebElement exitPopup=  popUpElement.findElement(By.xpath(".//img[@alt=\"cross\"]"));
				   exitPopup.click();

				} catch (TimeoutException e) {
				    System.out.println("Pop up not shown the screen!!!");
				}
			}

			public static String lowestPrice(WebElement monthCalenderList) {
				By junePriceList = By.xpath(".//span[contains(@class,\"custom-day-content \")]");
				List<WebElement> prices = monthCalenderList.findElements(junePriceList);
				int LowestPrice = Integer.MAX_VALUE;
				WebElement priceElement = null;
				for (WebElement price : prices) {
					// String manipulation
					String priceString = price.getText();
					if (priceString.length() > 0) {
						priceString = priceString.replace("₹", "").replace(",", "");
						System.out.println(priceString);
						// Tell the lowest Price
						// Parsing string to int
						int priceInt = Integer.parseInt(priceString);
						if (priceInt < LowestPrice) {
							LowestPrice = priceInt;
							priceElement = price;
						}
					}
				}
				// get date of lowest price
				WebElement dateLocator = priceElement.findElement(By.xpath(".//../.."));
				String result = dateLocator.getAttribute("aria-label") + " Price is Rs" + LowestPrice;
				return result;
			}

			public static WebElement selectMonthFromCalender(WebDriverWait wait, int index) {
				By CalenderSectionsLocater = By.xpath("//div[@class=\"react-datepicker__month-container\"]");
				List<WebElement> calenderSectionsList = wait
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(CalenderSectionsLocater));
				System.out.println(calenderSectionsList.size());
				// focus on current month
				WebElement monthCalenderList = calenderSectionsList.get(index);
				return monthCalenderList;
			}

			public static void compareTwoMonthPrice(String lowestPricingForCurrentMonth, String lowestPricingForNextMonth) {
				int currentMonthRsIndex = lowestPricingForCurrentMonth.indexOf("Rs");
				int nextMonthRsIndex = lowestPricingForCurrentMonth.indexOf("Rs");
				System.out.println(currentMonthRsIndex);
				System.out.println(nextMonthRsIndex);
				// get price using substring
				String currentPrice = lowestPricingForCurrentMonth.substring(currentMonthRsIndex + 2).trim().replace("Rs", "");
				String nextPrice = lowestPricingForNextMonth.substring(nextMonthRsIndex + 2).trim().replace("Rs", "");
				// convert into int
				int current = Integer.parseInt(currentPrice);
				int next = Integer.parseInt(nextPrice);
				if (current < next) {
					System.out.println("Lowest Price :" + current);
				} else if (current == next) {
					System.out.println("select any one as we have the same price");
				} else {
					System.out.println("Lowest Price :" + next);
				}
			}
		
			
	}

