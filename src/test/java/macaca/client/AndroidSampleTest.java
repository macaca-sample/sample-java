package macaca.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import macaca.client.commands.Element;
import macaca.client.common.ElementSelector;
import macaca.client.common.GetElementWay;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.containsString;

public class AndroidSampleTest {
    MacacaClient driver = new MacacaClient();

    @Before public void setUp() throws Exception {
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
        JSONObject desiredCapabilities = new JSONObject();
        desiredCapabilities.put("desiredCapabilities", porps);
        driver.initDriver(desiredCapabilities);
    }

    @Test public void test_case_1() throws Exception {
        // set screenshot save path
        File directory = new File("");
        String courseFile = directory.getCanonicalPath();

        System.out.println("------------#1 login test-------------------");

        driver.elementByXPath(
                "//android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.EditText[1]")
                .sendKeys("中文+Test+12345678");

        ElementSelector selector = driver.elementsByClassName("android.widget.EditText");
        selector.getIndex(1).sendKeys("111111");
        driver.elementByName("Login").click();
        driver.sleep(1000);

        System.out.println("------------#2 scroll tableview test-------------------");

        driver.elementByName("HOME").click();
        driver.elementByName("list").click();
        driver.sleep(1000);
        driver.drag(200, 420, 200, 10, 50, 100);
        driver.sleep(5000);

//        //  ci tap()
//        // 比如需要点击某个控件的特定位置
//        Element  alertCell = driver.getElement(GetElementWay.NAME, "Alert");
//        JSONObject alertCellRect = (JSONObject) alertCell.getRect();
//    	int x = alertCellRect.getIntValue("x");
//		int y = alertCellRect.getIntValue("y");
//		int width = alertCellRect.getIntValue("width");
//		int height = alertCellRect.getIntValue("height");
//		int centerX = x + width/2;
//		int centerY = y + height/2;
//
//        driver.tap(centerX, centerY);
//        driver.sleep(1000);
//        driver.dismissAlert();
//
//        // 通过右滑的方式返回上一层
//        driver.sleep(1000);
//        driver.back();


        // 拖拽一个元素或者在多个坐标之间移动,实现tableview滚动操作
        driver.drag(200,420,200,20,2, 100);
        driver.sleep(1000);
        driver.back();
        driver.sleep(1000);


        System.out.println("------------#3 webview test-------------------");

        driver.elementByName("Webview").click();
        driver.sleep(3000);
        // save screen shot
        driver.saveScreenshot(courseFile + "/webView.png");

        switchToWebView(driver).elementById("pushView").click();
        driver.sleep(5000);

        switchToWebView(driver).elementById("popView").click();
        driver.sleep(5000);

        System.out.println("------------#4 baidu web test-------------------");
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

        System.out.println("------------#5 logout test-------------------");

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

    @After public void tearDown() throws Exception {
        driver.quit();
    }
}
