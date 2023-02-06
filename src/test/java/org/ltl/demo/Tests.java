package org.ltl.demo;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

/**
 * @author Tianlong Liu
 * @date 2023/1/10 14:57
 */
public class Tests {

    @Test
    public void test1(){
        try(Playwright playwright = Playwright.create()){
//            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
            Page page = browser.newPage();
            page.navigate("https://baidu.com");
            Object obj = page.evaluate("window.location.href");
            System.out.println(obj);
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("D:\\test\\paywright_demo\\" + System.nanoTime() + ".png")));
        }
    }
}
