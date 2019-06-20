# AddressLib
仿京东 选择省市县地址
[![](https://jitpack.io/v/ForMuHan/AddressLib.svg)](https://jitpack.io/#ForMuHan/AddressLib)

Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.ForMuHan:AddressLib:V1.0'
	}
  
Step3.activity调用


	ChooseCityUtil util = new ChooseCityUtil();
        util.createDialog(this, new ChooseCityInterface() {
            @Override
            public void sure(String[] newCityArray) {
                Log.e("syw", newCityArray[0] + newCityArray[1] + newCityArray[2]);
            }

            @Override
            public void cancel() {

            }
        });
