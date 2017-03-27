package macaca.client;

import com.alibaba.fastjson.JSONObject;
import macaca.client.commands.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;

// API doc https://macacajs.github.io/wd.java/

public class DesktopSampleTest {

    MacacaClient driver = new MacacaClient();

    @Before public void setUp() throws Exception {

		/*
           Desired Capabilities are used to configure webdriver when initiating the session.
           Document URL: https://macacajs.github.io/desired-caps.html
         */
        JSONObject porps = new JSONObject();
        porps.put("browserName", "electron");
        porps.put("platformName", "desktop");
        JSONObject desiredCapabilities = new JSONObject();
        desiredCapabilities.put("desiredCapabilities", porps);
        //desiredCapabilities.put("host", "127.0.0.1"); // custom remote host
        //desiredCapabilities.put("port", 3456);        // custom remote port
        driver.initDriver(desiredCapabilities).setWindowSize(1280, 800)
                .get("https://www.baidu.com");
    }

    @Test public void test_case_1() throws Exception {
        driver.elementById("kw").sendKeys("中文");
        driver.sleep(1000);
        driver.elementById("su").click();
        driver.sleep(3000);

        String html = driver.source();
        Assert.assertThat(html, containsString("<html>"));

        driver.elementByXPath("//*[@id=\"kw\"]").sendKeys(" elementByXPath");
        driver.elementById("su").click();
        driver.takeScreenshot();
    }

    @Test public void test_case_2() throws Exception {
        System.out.println("test case #2");

        driver.get("https://www.baidu.com").elementById("kw").sendKeys("macaca");
        Element search = driver.elementByXPath("//*[@id=\"kw\"]");
        search.click();
        driver.sleep(3000);

        String html = driver.source();
        Assert.assertThat(html, containsString("<html>"));
        driver.takeScreenshot().sleep(3000);
    }

    @Test public void test_case_3() throws Exception {
        System.out.println("test case #3");

        driver.get("https://www.baidu.com");
        driver.elementsByXPath("//*[@id=\"u1\"]/a").getIndex(0).click();
        driver.sleep(3000);
    }

    @After public void tearDown() throws Exception {
        driver.quit();
    }
}
