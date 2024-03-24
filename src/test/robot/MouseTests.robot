*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Suite Setup   Run Keywords  Add Image Location  ${CURDIR}/  AND
...    Bring Window To Front  Test Form    AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY



*** Test Cases ***
Test Move Mouse
   Move Mouse To Image  test1.png
   Move Mouse    100    0
   Move Mouse To Display  SECONDARY  100  100
   Move Mouse To  400  400