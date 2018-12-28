SPDX-License-Identifier: BSD-3-Clause-Clear

Copyright (c) 2018 Qualcomm Technologies, Inc.

All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted (subject to the limitations in the disclaimer below) provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

Neither the name of Qualcomm Technologies, Inc. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

## DVS Authority Android App

### Documentation

[DVS-API-Installation-Guide-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS-API-Installation-Guide-1.0.0.pdf)<br />
[DVS-SPA-Installation-Guide-Authority-App-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS-SPA-Installation-Guide-Authority-App-1.0.0.pdf)<br />
[DVS-SPA-Installation-Guide-Public-App-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS-SPA-Installation-Guide-Public-App-1.0.0.pdf)<br />
[DVS-Authority-Web-App-UserGuide-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS-Authority-Web-App-UserGuide-1.0.0.pdf)<br />
[DVS-Public-Web-App- User-Guide-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS-Public-Web-App-%20User-Guide-1.0.0.pdf)<br />
[DVS-Developer-Guide-iOS-App-Authority-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS-Developer-Guide-iOS-App-Authority-1.0.0.pdf)<br />
[DVS-Developer-Guide-iOS-App-Public-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS-Developer-Guide-iOS-App-Public-1.0.0.pdf)<br />
[DVS-Developer-Guide-Android-App-Authority-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS-Developer-Guide-Android-App-Authority-1.0.0.pdf)<br />
[DVS-Developer-Guide-Android-App-Public-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS-Developer-Guide-Android-App-Public-1.0.0.pdf)<br />
[DVS-Authority-Mobile-App-User-Guide-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS-Authority-Mobile-App-User-Guide-1.0.0.pdf)<br />
[DVS -Public-Mobile- App-User-Guide-1.0.0.pdf](https://github.com/dirbs/Documentation/blob/master/Device-Verification-Subsystem/DVS%20-Public-Mobile-%20App-User-Guide-1.0.0.pdf)<br />

### Frontend Application Repos

[Device-Verification-Subsystem-Authority-Frontend](https://github.com/dirbs/Device-Verification-Subsystem-Authority-Frontend)<br />
[Device-Verification-Subsystem-Public-Frontend](https://github.com/dirbs/Device-Verification-Subsystem-Public-Frontend)<br />

### Backend Application Repos

[Device-Verification-Subsystem](https://github.com/dirbs/Device-Verification-Subsystem)<br />

### Mobile Application Repos

[Device-Verification-Subsystem-Public-Android](https://github.com/dirbs/Device-Verification-Subsystem-Public-Android)<br />
[Device-Verification-Subsystem-Authority-iOS](https://github.com/dirbs/Device-Verification-Subsystem-Authority-iOS)<br />
[Device-Verification-Subsystem-Public-iOS](https://github.com/dirbs/Device-Verification-Subsystem-Public-iOS)<br />

###	System Requirements
####	Software Requirements
-	JDK 1.8 or more
-	Android Studio 3.1.2
-	Android SDK v27 (minimum)
-	Minimum Android version 16
-	Gradle 4.4
-	Android Plugin version 3.1.2

###	Keycloak Configuration
-	Loggedin in IAM
-	Go to “Clients” section
-	Click on “DVS-app”  client
-	In settings tab enter callback “com.qualcomm.dvsauthorized:/callback” in “Valid Redirect URL” field

###	App Configuration
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
