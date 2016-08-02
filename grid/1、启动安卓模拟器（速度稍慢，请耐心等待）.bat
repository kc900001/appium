@echo off
echo 安卓模拟器启动中，请稍等。
echo 注意:在启动过程中，不要关闭此命令窗口。启动完成之后此窗口也不能关闭！
emulator -port 5676 -avd appium-test-avd -scale 0.6
emulator -port 5678 -avd appium-test-avd-1 -scale 0.6

