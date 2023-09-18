*** Settings ***
Library  BuiltIn
Library    ../PySnagTest/src/SnagTest.py


*** Test Cases ***
Test Robot Setup
    Log  Set up correctly!

Test DynamicTest
    Connect  1338
    #Start    1338
    Set Display    1
    Add Image Path  ${CURDIR}/images
    ${res}=  Get Image Paths
    Log  ${res}
    Find Image  hammer.png
    Find Image  debug.png
    Find Image  fail.png
    #[Teardown]  Shutdown

Test Shutdown
    Connect  1338
    Shutdown
