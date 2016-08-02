package com.young.appiumcombat.testcases.android.search; 
import org.testng.annotations.Test; 
import com.young.appiumcombat.base.BasePrepare; 
 import com.young.appiumcombat.utils.SuperAction; 
public class SearchPage_001_SearchDemo_Test extends BasePrepare{ 
@Test 
 public void searchDemo() { 
SuperAction.parseExcel("Search","001_SearchDemo",appiumUtil,platformName);
 }
}