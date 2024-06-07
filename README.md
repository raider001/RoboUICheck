# A Automated Testing Framework For UI using openCV and tessaract. 
Built as a plugin for robotframework.

Current Release: 1.0.0
## Documentation

This framework is setup as a Remote Library and be ran as a java application. It takes in the following parameters.

|arg|short arg| description|Mandatory|
|-|-|-|-|
|--port|-p|The port number the remote server will run under|yes|
|--log-location|-l|The location for where the sikuli logs are stored|no|
|--image-loc|-i|The location where images for the results are stored|no|

## Prerequestites
Java run time must be installed

## Run installation
1. Download the latest binary file located at: https://github.com/raider001/RoboUICheck/tree/master/release
2. Run java -jar UITestFramework-1.0.0.jar -p 7442

## Usage
In your robot file, add the remote library:
Library  Remote         http://127.0.0.1:7442/  AS  UI

Use as you would any Robot Remote Library

## Keywords
https://raider001.github.io/robouicheck/keyword_doc/doc.html

## Test Results
https://raider001.github.io/robouicheck/test_results/report.html
https://raider001.github.io/robouicheck/test_results/unittestresults.html
