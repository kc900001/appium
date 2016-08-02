@echo off
echo 启动appium server中，请不要关闭此窗口。
appium -a 127.0.0.1 -p 4723 -bp 4724   --selendroid-port 8090 --chromedriver-port 9515  --session-override --nodeconfig nodeconfig_1.json 