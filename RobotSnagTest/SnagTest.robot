*** Settings ***
Library     BuiltIn
Library  Remote  http://127.0.0.1:8270/

*** Test Cases ***
Test Robot Setup
    Log    Set up correctly!

Test OCR
    Test Keyword
