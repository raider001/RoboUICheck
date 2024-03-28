*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Suite Setup   Run Keywords  Add Image Location  ${CURDIR}/  AND
...    Bring Window To Front  Test Form    AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY


*** Test Cases ***
Test Move Mouse To Image
   Move Mouse To Image  test1.png

Test Move Mouse Between Displays
   Move Mouse    100    0
   Move Mouse To Display  SECONDARY  100  100
   Move Mouse To  400  400
   Move Mouse To Display  PRIMARY  100  100

Test Click Image
   Click Image  LEFT  test1.png
   Verify Image Exists On Window    labelconfirmation.png    Test Form
   [Teardown]  Click Image On Window  LEFT  test1.png  Test Form

Test Click Image On Window
   Click Image On Window  LEFT  test1.png  Test Form
   Verify Image Exists On Window    labelconfirmation.png  Test Form
   [Teardown]  Click Image On Window  LEFT  test1.png  Test Form

Test Click X Times
    Move Mouse To Image  clickCounter.png
    Click  LEFT  2
    Verify Image Exists On Window    two.png    Test Form
    [Teardown]  Click Image On Window  LEFT  test1.png  Test Form

Test Click Location
    &{dimensions}    Get Window Dimensions  Test Form
    Click Location  LEFT  ${${dimensions}[x]+50}  ${${dimensions}[y]+50}
    Verify Image Exists On Window    labelconfirmation.png    Test Form
    [Teardown]  Click  LEFT

Test Press And Release
    Move Mouse To Image    PAndRTest.png
    Press Mouse Button  Left
    Verify Image Exists On Window  pressed.png  Test Form
    Release Mouse Button  Left
    Verify Image Exists On Window  released.png  Test Form
    [Teardown]  Click Image On Window  LEFT  test1.png  Test Form

