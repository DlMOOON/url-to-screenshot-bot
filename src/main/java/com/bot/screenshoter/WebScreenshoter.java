package com.bot.screenshoter;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class WebScreenshoter {

    private WebDriver webDriver;

    public File takeSimpleScreenshot(String url) {
        log.info("Take a simple screenshot");

        webDriver.get(url);
        Screenshot screenshot = new AShot().takeScreenshot(webDriver);
        return getFileFromBufferedImage(screenshot.getImage(), "simple-screenshot");
    }

    public File takeLongScreenshot(String url) {
        log.info("Take a long screenshot");

        webDriver.get(url);
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1)).takeScreenshot(webDriver);
        return getFileFromBufferedImage(screenshot.getImage(), "long-screenshot");
    }

    public File takeCustomScreenshot(String url, Dimension dimension) {
        log.info("Take a custom screenshot");

        webDriver.get(url);
        webDriver.manage().window().setSize(dimension);

        Screenshot screenshot = new AShot().takeScreenshot(webDriver);

        webDriver.manage().window().fullscreen();
        return getFileFromBufferedImage(screenshot.getImage(), "custom-screenshot");
    }

    public File takeScreenshotWithScaling(String url, float dpr) {
        log.info("take a screenshot with scaling");
        webDriver.get(url);
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.scaling(dpr)).takeScreenshot(webDriver);
        return getFileFromBufferedImage(screenshot.getImage(), "scaling-screenshot");
    }

    private File getFileFromBufferedImage(BufferedImage image, String name) {
        File file = new File(name);

        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    @PostConstruct
    private void prepareAndRunWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("window-size=1920,1080");
        webDriver = new ChromeDriver(options);

        log.info("Web driver is prepared and running");
    }
}
