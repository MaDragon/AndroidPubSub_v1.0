# Android PubSub Sample

This sample demonstrates use of the AWS IoT APIs to securely publish to and subscribe from MQTT topics.  It uses Cognito authentication in conjunction with AWS IoT to create an identity (client certificate and private key) and store it in a Java keystore.  This identity is then used to authenticate to AWS IoT.  Once a connection to the AWS IoT platform has been established, the application presents a simple UI to publish and subscribe over MQTT.  After certificate and private key have been added to the keystore the app will use these for future connections.

## Requirements

* AndroidStudio or Eclipse
* Android API 10 or greater

## Using the Sample

1. Import the AndroidPubSub project into your IDE.
   - If you are using Android Studio:
      * From the Welcome screen, click on "Import project".
      * Browse to the AndroidPubSub directory and press OK.
      * Accept the messages about adding Gradle to the project.
      * If the SDK reports some missing Android SDK packages (like Build Tools or the Android API package), follow the instructions to install them.
   - If you are using Eclipse:
      * Go to File -> Import. Import Wizard will open.
      * Select General -> Existing Projects into Workspace. Click Next.
      * In Select root directory, browse to the samples directory.
      * Select the AndroidPubSub project to import.
      * Click Finish.
      
1. Import the libraries :
   - If you use Android Studio, Gradle will take care of downloading these dependencies for you.
   - If you use Eclipse, you will need to download the AWS SDK for Android (http://aws.amazon.com/mobile/sdk/) and extract and copy these jars into the 'libs' directory for the project:
      * aws-android-sdk-core-X.X.X.jar
      * aws-android-sdk-iot-X.X.X.jar

1. This sample requires Cognito to authorize to AWS IoT in order to create a device certificate. Use Amazon Cognito to create a new identity pool.
    1. In the [Amazon Cognito Console](https://console.aws.amazon.com/cognito/), select`Create Identity Pool`.
    1. Ensure`Enable access to unauthenticated identities` is checked. This allows the sample application to assume the unauthenticated role associated with this identity pool.
    
        **Important**: see note below on unauthenticated user access.
        
    1. Obtain the `PoolID` constant.  This will be used in the application.
    1. As part of creating the identity pool Cognito will setup two roles in [Identity and Access Management (IAM)](https://console.aws.amazon.com/iam/home#roles).  These will be named something similar to:`Cognito_PoolNameAuth_Role` and`Cognito_PoolNameUnauth_Role`.
    1. Now we will attach a policy to the unauthenticated role which has permissions to access the required AWS IoT APIs.  This is done by first creating an IAM Policy in the [IAM Console](https://console.aws.amazon.com/iam/home#policies) and then attaching it to the unauthenticated role.  Below is an example policy which can be used with the sample application.  This policy allows the application to create a new certificate (including private key) as well as attach an existing policy to a certificate.

        ```
        {
          "Version": "2012-10-17",
          "Statement": [
            {
              "Effect": "Allow",
              "Action": [
                "iot:AttachPrincipalPolicy",
                "iot:CreateKeysAndCertificate"
              ],
              "Resource": [
                "*"
              ]
            }
          ]
        }
        ```

        More information on AWS IAM roles and policies can be found [here](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies_manage.html).  More information on AWS IoT policies can be found [here](http://docs.aws.amazon.com/iot/latest/developerguide/authorization.html).

        **Note**: to keep this example simple it makes use of unauthenticated users in the identity pool.  This can be used for getting started and prototypes but unauthenticated users should typically only be given read-only permissions if used in production applications.  More information on Cognito identity pools including the Cognito developer guide can be found [here](http://aws.amazon.com/cognito/).

1. Note that the application does not actually create the AWS IoT policy itself, rather it relies on a policy to already be created in AWS IoT and then makes a call to attach that policy to the newly created certificate.  To create a policy in AWS IoT,
    1. navigate to the [AWS IoT Console](https://console.aws.amazon.com/iot/home)
    1. Click on Create a Resource
    1. Click on Create a Policy
    1. Give the policy a name.  Note this name as this is the string you will use in the application when making the attach policy API call.
    1. The policy should be created to allow connecting to AWS IoT as well as allowing publishing, subscribing and receiving messages on whatever topics you will use in the sample application.  Below is an example policy.  This policy allows access to all topics under your AWS IoT account.   To scope this policy down to specific toipcs specify them explicitly as ARNs in the resource section: `"Resource": "arn:aws:iot:<REGION>:<ACCOUNT ID>:topic/mytopic/mysubtopic"`.  Note that the first `topic` is an ARN specifer so this example actually specifies the topic `mytopic/mysubtopic`.

        ```
        {
          "Version": "2012-10-17",
          "Statement": [
            {
              "Effect": "Allow",
              "Action": "iot:Connect",
              "Resource": "*"
            },
            {
              "Effect": "Allow",
              "Action": [
                "iot:Publish",
                "iot:Subscribe",
                "iot:Receive"
              ],
              "Resource": "*"
            }
          ]
        }
        ```

1. Open the AndroidPubSub project.

1. Open `PubSubActivity.java` and update the following constants with the appropriate values:

    ```
    CUSTOMER_SPECIFIC_ENDPOINT = "<CHANGE_ME>";
    COGNITO_POOL_ID = "<CHANGE_ME>";
    AWS_IOT_POLICY_NAME = "CHANGE_ME";
    MY_REGION = Regions.US_EAST_1;
    KEYSTORE_NAME = "iot_keystore";
    KEYSTORE_PASSWORD = "password";
    CERTIFICATE_ID = "default";
    ```
    The customer specific endpoint, Cognito pool ID, Region and AWS IoT policy name will need to be updated to reflect the values in your account.  The policy name is the name used when creating the IoT policy above.  For the other parameters the default values will work for this sample application.  The following describes these parameters in case they need to be updated going forward past this sample. The keystore name is the name used when writing the keystore file to the application's file directory.  The password is the password given to protect the keystore when written.  Certificate ID is the alias in the keystore for the certificate and private key entry.  If you end up creating a keystore off of the device you will need to update this to match the alias given when importing the certificate into the keystore.

1. Build and run the sample app.

1. The sample application will allow you to connect to the AWS IoT platform, and then publish or subscribe to a topic using MQTT.

Note: This application also contains commented-out code for acccessing a KeyStore that was deployed as a resource file as part of an APK.


## if Change the code
### To receive BLE signal from Sensmitters.
-PubSubActivity.java
-line 194.
```
 private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback()
```

### Detect Noise level in surrounding environment,
-PubSubActivity.java
-line 237  
```
   MediaRecorder mRecorder  = new MediaRecorder();
```
```  
mRecorder.setOutputFile("/dev/null");
```
### Add more sensor in Phone
-DeviceSensor.java
-line 57.
```
public DeviceSensor(Context context) {}
``` 
### change sensor sampling rate, 
-DeviceSensor.java
-line 68
``` 
public void startDeviceSensor() {} 
```
