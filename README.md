# DVS Authority Android App
##	System Requirements
###	Software Requirements
-	JDK 1.8 or more
-	Android Studio 3.1.2
-	Android SDK v27 (minimum)
-	Minimum Android version 16
-	Gradle 4.4
-	Android Plugin version 3.1.2

##	Keycloak Configuration
-	Loggedin in IAM
-	Go to “Clients” section
-	Click on “DVS-app”  client
-	In settings tab enter callback “com.qualcomm.dvsauthorized:/callback” in “Valid Redirect URL” field

##	App Configuration
-	To change the logo of app go to app/res/drawable folder and paste logo file but make sure the file name should be company_logo.png
-	To change the colors of the app go to app/res/values/colors.xml file and mention hex color code of your required color
-	Go to app/res/values/strings.xml file and add IAM URL in value section of  “iam_url”
<string name=”iam_url”>Enter IAM URL HERE</string>
-	Add Realm name in value section of  “realm” field
<string name=”realm”>ENTER REALM NAME HERE</string>
-	Add API Gateway URL in value section of  “api_gateway” field
<string name=”api_gateway_url”>ENTER API GATEWAY URL HERE</string>
-	Add client id in value section of “client_id”
<string name=”client_id”>ENTER CLIENT ID HERE</string>
