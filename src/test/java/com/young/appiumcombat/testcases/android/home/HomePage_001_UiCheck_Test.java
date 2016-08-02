package com.young.appiumcombat.testcases.android.home; 
import org.testng.annotations.Test; 
import com.young.appiumcombat.base.BasePrepare; 
 import com.young.appiumcombat.utils.SuperAction; 
public class HomePage_001_UiCheck_Test extends BasePrepare{ 
@Test 
 public void uiCheck() { 
SuperAction.parseExcel("Home","001_UiCheck",appiumUtil,platformName);
 }
}