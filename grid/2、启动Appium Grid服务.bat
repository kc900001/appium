@echo off
echo 正在启动appium grid服务，请稍等。
echo 启动完毕之后，请不要关闭此窗口。
echo 当你看到“Selenium Grid hub is up and running”则表示启动成功。
java -jar selenium-server-standalone.jar -role hub
