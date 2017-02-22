# boot_multipart
Figure out Spring Boot Multipart Support

This project is loosely based on Spring's "Getting Started - Uploading Files" tutorial:
https://spring.io/guides/gs/uploading-files/

Further inspiration gleaned from https://murygin.wordpress.com/2014/10/13/rest-web-service-file-uploads-spring-boot/

## Running

To build boot_multipart from scratch:

```
$ ./gradlew clean build --info
```

That will generate a JAR file in the `build/libs` directory that can be run using:

```
$ java -jar build/libs/boot_multipart-*.jar
```

You can run a Spring Boot app through Gradle from the command line like:
```
$ ./gradlew bootRun

```

### Note that this tutorial stores the uploaded files temporarily at ./upload-dir/

The ./upload-dir/ directory is initialized and/or cleaned at startup time.  Its not meant for production.  Its just
here for the demo.

# Working curls

```
curl -i -X POST 'http://localhost:8080/robert/v1/140218/scouting_activities' \
-H 'Content-type:multipart/mixed' \
-H 'x-user-id:140218' \
-H 'x-http-request-id:POST_scouting_activities' \
-H 'x-http-caller-id: tcc-rkuhar.local' \
-F 'image=@Smile_128x128.png;type=image/png' \
-F 'scouting_activity={
  "field": 14006513,
  "longitude": -93.2038253,
  "latitude": 38.5203231,
  "note": "This is the center of Dino Head.",
  "scouting_date": "2017-01-19T22:56:04.836Z"
};type=application/json'

curl -i -X POST 'http://localhost:8080/robert/v1/140218/scouting_activities' \
-H 'Content-type:multipart/mixed' \
-H 'x-user-id:140218' \
-H 'x-http-request-id:POST_scouting_activities' \
-H 'x-http-caller-id: tcc-rkuhar.local' \
-F 'image=@Smile_128x128.png;type=image/png' \
-F 'scouting_activity={
  "field": 14006513,
  "longitude": -93.2038253,
  "latitude": 38.5203231,
  "note": "This is the center of Dino Head.",
  "scouting_date": "2017-01-19T22:56:04.836Z"
}'

curl -i -X POST 'http://localhost:8080/robert/v1/140218/scouting_activities' \
-H 'Content-type:multipart/mixed' \
-H 'x-user-id:140218' \
-H 'x-http-caller-id: tcc-rkuhar.local' \
-H 'x-http-request-id:POST_scouting_activities' \
-F 'image=@Smile_128x128.png;type=image/png' \
-F 'scouting_activity=@scoutingFrackingCurl.json;type=application/json'

curl -i -X POST 'http://localhost:8080/robert/v1/140218/scouting_activities' \
-H 'Content-type:multipart/mixed' \
-H 'x-user-id:140218' \
-H 'x-http-caller-id: tcc-rkuhar.local' \
-H 'x-http-request-id:POST_scouting_activities' \
-F 'scouting_activity=@scoutingFrackingCurl.json;type=application/json'


curl -i -X POST 'http://localhost:8080/robert/v1/140218/scouting_activities' \
-H 'Content-type:application/json' \
-H 'x-user-id:140218' \
-H 'x-http-caller-id: tcc-rkuhar.local' \
-H 'x-http-request-id:POST_thirdTry_scouting_activity_in_body' \
-d '{
  "field": 1928292,
  "longitude": -96.80190201101068,
  "latitude": 42.528416974741084,
  "note": "This is the center of my field",
  "scouting_date": "2016-04-12T22:10:17.469Z"
}'

# The Web does multipart/form-data if it has an image
curl -i -X POST 'http://localhost:8080/robert/v1/140218/scouting_activities' \
-H 'Content-type:multipart/form-data' \
-H 'x-user-id:140218' \
-H 'x-http-caller-id: tcc-rkuhar.local' \
-H 'x-http-request-id:POST_scouting_activities' \
-F 'image=@Smile_128x128.png;type=image/png' \
-F 'scouting_activity=@scoutingFrackingCurl.json;type=application/json'

# The Web does application/json if it does not have an image
curl -i -X POST 'http://localhost:8080/robert/v1/140218/scouting_activities' \
-H 'Content-type:application/json' \
-H 'x-user-id:140218' \
-H 'x-http-caller-id: tcc-rkuhar.local' \
-H 'x-http-request-id:POST_scouting_activities_no_image' \
--data-binary '{"field":24846935,"note":"Hello","longitude":-90.5984887,"latitude":40.225,"scouting_date":"2017-02-22"}'
```

Spring does show some promise in being able to do just general unmapped multipart crap

```
$ curl -i -v -X POST 'http://localhost:8080/robert/forthTry' \
-H 'Content-type:multipart/mixed' \
-H 'x-user-id:140218' \
-H 'x-http-request-id:POST_forthTry_File_and_Type_multipart_mixed' \
-F 'image=@Smile_128x128.png;type=image/png' \
-F 'scouting_activity=@scoutingFrackingCurl.json;type=application/json'

$ curl -i -v -X POST 'http://localhost:8080/robert/forthTry' \
-H 'x-user-id:140218' \
-H 'x-http-request-id:POST_forthTry_File_and_Type_multipart_form' \
-F 'image=@Smile_128x128.png;type=image/png' \
-F 'scouting_activity=@scoutingFrackingCurl.json;type=application/json'
```

...except this last one...it seems incapable of dealing with multiple copies of the same parameter.  Ugh.

```
curl -i -v -X POST 'http://localhost:8080/robert/forthTry' \
-H 'x-user-id:140218' \
-H 'x-http-request-id:POST_forthTry_File_and_Type_multipart_form' \
-F 'image=@Smile_128x128.png;type=image/png' \
-F 'scouting_activity=@some.json;type=application/json' \
-F 'scouting_activity=@scoutingFrackingCurl.json;type=application/json'
```

# Conclusion:  Spring Sucks!!!!

The abstraction is so opaque as to be completely useless without reading all the sourcecode.  Virtually all of the
Stack Overvlow questions have one aspect or another wrong.  The Documentation does nothing but explain the happieset
of Happy Paths.  For Multipart File Upload, Spring is useless and I am grumpy.

