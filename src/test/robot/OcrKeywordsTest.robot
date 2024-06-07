*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Library  Collections
Suite Setup    Add Image Location  ${CURDIR}/


*** Test Cases ***
Test Read Text From Form
    ${text}=    Get Text From Form    Test Form
    Should Contain   ${text}    Test Click
    Should Contain   ${text}    Text Area
    Should Contain   ${text}    Test2
    Should Contain   ${text}    Click Counter.
    Should Contain   ${text}    PANdRTest

Test Get Words From Form
    ${words}=    Get Words From Form    Test Form