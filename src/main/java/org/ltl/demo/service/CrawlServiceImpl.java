package org.ltl.demo.service;

import com.alibaba.fastjson2.JSON;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.FilePayload;
import com.microsoft.playwright.options.ScreenSize;
import org.apache.commons.lang3.StringUtils;
import org.ltl.demo.util.ClassPathResourceUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tianlong Liu
 * @date 2023/2/1 14:55
 */
@Service
public class CrawlServiceImpl implements CrawlService{

    private final static String userName = "**";
    private final static String password = "***";

    public Object crawl() throws Exception{
        try(Playwright playwright = Playwright.create()){
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setChannel("chrome") // 使用 chrome
                    .setHeadless(false) // 使用浏览器可见模式
                    .setArgs(Arrays.asList("--start-maximized"))); // 窗口最大化
//            Page page = browser.newPage();
            // 窗口最大化才生效
            BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
            Page page = context.newPage();
            // 登录
            doLogin(playwright,page);
            // 采集余额
            Object balance = doCrawlBalance(playwright,page);
            Map<String,Object> result = new HashMap<>();
            result.put("balance",balance);
            // 采集账单明细
            Object orderData = doCrawlOrderData(playwright,page);
            result.put("orderData",orderData);
            // 截图
            String timeFmt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("D:\\test\\paywright_demo\\" + timeFmt.replaceAll(":","_") + ".png")));
            // 上传文件
            doUploadFiles(playwright,page);
            return result;
        }
    }

    private void doLogin(Playwright playwright,Page page) throws Exception{
        page.navigate("https://www.sfcservice.com/login");
        Locator nameEle = page.locator("form#user_login input#username");
        Locator pwdEle = page.locator("form#user_login input#password");
        Locator submit = page.locator("form#user_login input[type=\"submit\"]");
        nameEle.fill(userName);
        pwdEle.fill(password);
        submit.click();
//        Thread.sleep(1000 * 3);
    }

    private Object doCrawlBalance(Playwright playwright,Page page){
        page.locator("#financeMenu > a").getByText("财务管理").click();
        Locator balanceEle = page.locator("div.price > span");
        return balanceEle.innerText();
    }

    private Object doCrawlOrderData(Playwright playwright,Page page) throws Exception{
        String jsCode = ClassPathResourceUtils.getResourceAsString("js_scripts/sfc_order_detail.js");
        Object result = page.evaluate(jsCode);
        return result == null ? null : JSON.parseObject(result.toString());
    }

    private void doUploadFiles(Playwright playwright,Page page){
        // upload 1
//        page.locator("input[type=\"file\"]").setInputFiles(new FilePayload("test.txt",
//                "text/plain","上传文本内容".getBytes(StandardCharsets.UTF_8)));
        // upload 2
//        FileChooser fileChooser = page.waitForFileChooser(() ->{
//            page.locator("file#upoad").click();
//        });
//        fileChooser.setFiles(Paths.get("test.jpg"));
    }
}
