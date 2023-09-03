*** Settings ***
Library  SnagTest  GEN
Library  BuiltIn

*** Test Cases ***
Test Robot Setup
    Log  Set up correctly!

Test SnagTest Connection
    Ping