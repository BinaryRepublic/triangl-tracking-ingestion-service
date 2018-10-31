[![CircleCI](https://circleci.com/gh/codeuniversity/triangl-tracking-ingestion-service.svg?style=svg&circle-token=b99f88e5552bf9e270d2ed455a1b221163a48819)](https://circleci.com/gh/codeuniversity/triangl-tracking-ingestion-service)

# Triangl-Tracking-Ingestion-Service
**Url**: https://api.triangl.io/tracking-ingestion-service/

**Full API Documentation**: https://api.triangl.io/tracking-ingestion-service/swagger-ui.html

**Place in the Infrastructure**: https://github.com/codeuniversity/triangl-infrastructure

## For faster development this service currently combines the Computation and the Ingestion Service that we want to have seperate in the later stage

## Routes

- Send one DataPoint **POST** /tracking
- Send multiple DataPoints **POST** /tracking/multiple
- Get Routers lastSeen Timestamp **GET** /routers/lastSeen
- Read the buffer **GET** /read

## What does it do
This Service is an Endpoint for the Routers to send their DataPoints to. These DataPoints currently look the like the following:
```
{
    var routerId: "TheRouterId",

    var deviceId: "TrackedDeviceId",

    timestampString: "2018-10-15 09:00:00",   //GMT+00:00

    var signalStrength: -255,
}
```
The service then:
- calculates the location of the TrackedDevice,
- inserts it into the Google Datastore 
- notifies the [Pipeline](https://github.com/codeuniversity/triangl-processing-pipeline) over Google Pub/Sub about the new Tracking Point in the Datastore. 

The [Pipeline](https://github.com/codeuniversity/triangl-processing-pipeline) can then apply the changes to the Serving SQL Database.

## ComputeLocation function
The location calculation function works in the following way:

It starts in a second [kotlin coroutine](https://kotlinlang.org/docs/reference/coroutines-overview.html) and then checks
every 5 seconds if there are some locations to compute.

The computation currently works based on the RSSI (Received Signal Strength Indication). The location of the TrackedDevice is currently the location of the nearest router that tracked him.

## Tools used
- Objectify
  https://github.com/objectify/objectify
  Used to connect and write to Google Datastore. "Objectify is a Java data access API specifically designed for the Google Cloud Datastore"

## Environment Variables
The following Environment variables are need for this service:

```GOOGLE_APPLICATION_CREDENTIALS:{pathToGoogleKeyFile.json}```

Moreover, you can set the ```pubsub.topicId``` env variable via the console
to override the standard value ```pubsub.topicId=test```.

## Run
- With Gradle

  ```GOOGLE_APPLICATION_CREDENTIALS=/path/to/google/key/file.json ./gradlew bootRun```
