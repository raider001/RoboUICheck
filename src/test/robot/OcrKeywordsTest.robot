*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Library  Collections
Suite Setup    Add Image Location  ${CURDIR}/


*** Test Cases ***
Test Read Text From Form
    ${text}=    Get Text From Form    Test Form
    Should Be Equal As Strings    ${text}    Hello, World!

Test Get Words From Form
    ${words}=    Get Words From Form    Test Form