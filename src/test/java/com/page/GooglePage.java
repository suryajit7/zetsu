package com.page;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Getter
public class GooglePage extends BasePage {

    private static final String GOOGLE_LOGO = "//img[@alt='Google']";
    private static final String SEARCH_INPUT_FIELD = "//input[@title='Search']";
    private static final String GOOGLE_SEARCH_BTN = "//div//input[@name='btnK']";
    private static final String IM_FEELING_LUCKY_BTN = "//div//input[@name='btnI']";

    @FindBy(xpath = GOOGLE_LOGO)
    private WebElement googleLogo;

    @FindBy(xpath = SEARCH_INPUT_FIELD)
    private WebElement searchInputField;

    @FindBy(xpath = GOOGLE_SEARCH_BTN)
    private List<WebElement> googleSearchBtnList;

    @FindBy(xpath = IM_FEELING_LUCKY_BTN)
    private List<WebElement> imFeelingLuckyBtn;

    public GooglePage(WebDriver driver) {
        super(driver);
    }


    public GooglePage enterSearchItem(String searchItem) {
        waitForPageToLoad();
        enterText(searchInputField, searchItem);
        return this;
    }


    public GooglePage clickGoogleSearchButton() {
        this.googleSearchBtnList.stream()
                .filter(btn -> btn.isEnabled() && btn.isDisplayed())
                .findFirst().get().click();
        return this;
    }

    public GooglePage clickImFeelingLuckyButton() {
        this.imFeelingLuckyBtn.stream()
                .filter(btn -> btn.isEnabled() && btn.isDisplayed())
                .findFirst().get().click();
        return this;
    }


    @Override
    public BasePage isPageLoaded() {
        waitForPageToLoad();
        waitForElementToAppear(googleLogo);
        return this;
    }
}
