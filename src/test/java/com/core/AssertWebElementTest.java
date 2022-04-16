package com.core;

import com.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.core.AssertWebElement.assertThat;


public class AssertWebElementTest extends BaseTest {

    @BeforeMethod
    public void setupTestData(){
        googlePage.goTo(APP_URL)
                .isPageLoaded();
    }


    @Test(priority = 0)
    public void verifySearchUserFunctionality(){

        WebElement elementButton = googlePage.getGoogleSearchBtnList().stream()
                .filter(btn -> btn.isDisplayed() && btn.isEnabled())
                .findFirst().get();

        assertThat(elementButton)
                .isDisplayed()
                .isEnabled()
                .isButton();

    assertThat(googlePage.getSearchInputField())
            .isDisplayed()
            .isEnabled();



    }
}
