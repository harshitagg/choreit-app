![ChoreIt!](choreit-app/res/drawable-xhdpi/ic_launcher.png)

#ChoreIt!

An Android app for managing daily chores.

##To run this project, you need to do this:

1. Set the ANDROID\_HOME environment variable to point to the location of your installed Android SDK 4.1.2 API level 16. For more information, look at [the documentation of maven-android-plugin](http://code.google.com/p/maven-android-plugin/wiki/GettingStarted).
2. Use [maven-android-sdk-deployer](https://github.com/mosabua/maven-android-sdk-deployer) to deploy google-play-services on your local machine.
3. Then, you can run "mvn clean install" in the main directory.

The Google+ Signin won't work without using the appropriate keystore. Please contact developer to obtain a signed apk.
