
# Starter-java
A simple resource to serve twiml files for your twilio needs

## Installation
1. Download and install maven
2. clone or download this repository
    git clone git://code.corp.twilio.com/arjuna/starter-java.git
3. Build
    cd starter-java
    mvn clean install
4. Add account information to server/starter-java.yml
    Edit following fields
        # twilio configuration
        twilioConfiguration:

          # account sid
          accountSid: AC............................

          # authtoken here
          authToken: ..............................
5. Run
    java -jar server/target/server-0.0.1-SNAPSHOT.jar server server/starter-java.yml

6. Send a text message
    Curl your test/send resource to send a text message.
    This will send a text message to 4155556666 form one of your twilio phone number(s)
    $> curl -X POST http://localhost:8060/test/send -d "To=4155556666" -d "Body=Ahoy Twilio"

7. To serve Twiml
    install ngrok
    ngrok 8060

## SmsResource
###Method:
    POST
###uri:
    http://localhost:18060/sms

###POST Parameters:
    SmsSid
    AccountSid
    SmsMessageSid
    To
    From
    Body
    ApiVersion
    SmsStatus
    FromZip
    FromCity
    FromState
    FromCountry
    ToZip
    ToCity
    ToState
    ToCountry

##Return applicaton/xml
    Twiml

##Resource for SdkTestResource
###Method:
    POST
###uri:
    http://localhost:18060/test/send

###POST Parameters:
    To - number to send a text message
    Body - Body of message

##Return Response
Status of txt message

##SynchronousResource
Somthing to build on
###Method:
    POST
###uri:
    http://localhost:18060/synchronous

