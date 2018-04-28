package macaca.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import macaca.client.commands.Element;

import static org.hamcrest.CoreMatchers.containsString;

public class AndroidSampleTest {
    MacacaClient driver = new MacacaClient();
    // set screenshot save path
    File directory = new File("");
    public String courseFile = directory.getCanonicalPath();

    public AndroidSampleTest() throws IOException {
    }

    @Before
    public void setUp() throws Exception {
        // platform: android or ios
        String platform = "android";

        /*
           Desired Capabilities are used to configure webdriver when initiating the session.
           Document URL: https://macacajs.github.io/desired-caps.html
         */
        JSONObject porps = new JSONObject();
        porps.put("platformName", platform);
        porps.put("app", "https://npmcdn.com/android-app-bootstrap@latest/android_app_bootstrap/build/outputs/apk/android_app_bootstrap-debug.apk");
        porps.put("reuse", 1);
        // device id
//        porps.put("udid","0715f7ea12391134");
        JSONObject desiredCapabilities = new JSONObject();
        desiredCapabilities.put("desiredCapabilities", porps);
        driver.initDriver(desiredCapabilities);
    }

    @Test
    public void testCaseOne() throws Exception {

        System.out.println("------------#1 login test-------------------");
        loginTest();

        System.out.println("------------#2 scroll tableview test-------------------");
        scrollTableViewTest();

        System.out.println("------------#3 webview test-------------------");
        webViewTest();

        System.out.println("------------#4 baidu web test-------------------");
        baiduWebTest();

        System.out.println("------------#5 logout test-------------------");
        logoutTest();

    }

    public void loginTest() throws Exception {
        driver.elementById("com.github.android_app_bootstrap:id/mobileNoEditText")
                .sendKeys("中文+Test+12345678");

        List<Element> elements = driver.elementsByClassName("android.widget.EditText");
        elements.get(1).sendKeys("111111");
        driver.elementByName("Login").click();
        driver.sleep(1000);
    }


    public void scrollTableViewTest() throws Exception {
        driver.elementByName("HOME").click();
        driver.elementByName("list").click();
        driver.sleep(1000);
        driver.drag(200, 420, 200, 10, 0.5);
        driver.sleep(5000);


        // 拖拽一个元素或者在多个坐标之间移动,实现tableview滚动操作
        driver.drag(200, 420, 200, 20, 0.5);
        driver.sleep(1000);
        driver.back();
        driver.sleep(1000);
    }

    public void webViewTest() throws Exception {
        driver.elementByName("Webview").click();
        driver.sleep(3000);
        // save screen shot
        driver.saveScreenshot(courseFile + "/webView.png");

        switchToWebView(driver).elementById("pushView").click();
        driver.sleep(5000);

        switchToWebView(driver).elementById("popView").click();
        driver.sleep(5000);
    }

    public void baiduWebTest() throws Exception {
        switchToNative(driver).elementByName("Baidu").click();
        driver.sleep(5000);
        driver.saveScreenshot(courseFile + "/baidu.png");

//        switchToWebView(driver).elementById("index-kw").sendKeys("中文+TesterHome");

        Element input = switchToWebView(driver).elementById("index-kw");
        input.sendKeys("中文+TesterHome");
        driver.sleep(1000);

        // 测试清理API
        input.clearText();
        driver.sleep(1000);

        // 重新输入
        input.sendKeys("中文+TesterHome");
        driver.sleep(1000);

        driver.elementById("index-bn").click();
        driver.sleep(5000);
        String source = driver.source();
        Assert.assertThat(source, containsString("TesterHome"));
    }


    public void logoutTest() throws  Exception {

        switchToNative(driver).elementByName("PERSONAL").click();
        driver.sleep(1000).elementByName("Logout").click();
        driver.sleep(1000);
    }


    // switch to the context of the last pushed webview
    public MacacaClient switchToWebView(MacacaClient driver) throws Exception {
        JSONArray contexts = driver.contexts();
        return driver.context(contexts.get(contexts.size() - 1).toString());
    }

    // switch to the context of native
    public MacacaClient switchToNative(MacacaClient driver) throws Exception {
        JSONArray contexts = driver.contexts();
        return driver.context(contexts.get(0).toString());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}
