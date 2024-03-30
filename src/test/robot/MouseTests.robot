*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Suite Setup   Run Keywords  Add Image Location  ${CURDIR}/  AND
...    Bring Window To Front  Test Form    AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY


*** Test Cases ***
Test Move Mouse To Image
   Move Mouse To Image  testClick.png

Test Move Mouse Between Displays
   Move Mouse    100    0
   Move Mouse To Display  SECONDARY  100  100
   Move Mouse To  400  400
   Move Mouse To Display  PRIMARY  100  100

Test Click Image
   Click  image=testClick.png
   Verify Image Exists On Window    labelconfirmation.png    Test Form
   [Teardown]  Click  image=testClick.png  window=Test Form

Test Click Image On Window
   Click  image=testClick.png  window=Test Form
   Verify Image Exists On Window    labelconfirmation.png  Test Form
   [Teardown]  Click  image=testClick.png  window=Test Form

Test Click X Times
    Move Mouse To Image  clickCounter.png
    Click  times=2
    Verify Image Exists On Window    two.png    Test Form
    [Teardown]  Click  image=testClick.png  window=Test Form

Test Click Location
    &{dimensions}    Get Window Dimensions  Test Form
    Click  x=${${dimensions}[x]+50}  y=${${dimensions}[y]+50}
    Verify Image Exists On Window    labelconfirmation.png    Test Form
    [Teardown]  Click  x=${${dimensions}[x]+50}  y=${${dimensions}[y]+50}

Test Press And Release
    Move Mouse To Image    PAndRTest.png
    Press Mouse Button  Left
    Verify Image Exists On Window  pressed.png  Test Form
    Release Mouse Button  Left
    Verify Image Exists On Window  released.png  Test Form
    [Teardown]  Click  image=testClick.png  window=Test Form

Test Wheel Up And Down
    Move Mouse To Image    PAndRTest.png
    Mouse Scroll  1
    Verify Image Exists On Window  mousedown.png  Test Form
    Mouse Scroll  -1
    Verify Image Exists On Window  mouseup.png  Test Form
    [Teardown]  Click  image=testClick.png  window=Test Form

Click Image On Window
    Click  x=100  y=100
    Click  display=SECONDARY  x=100  y=100
    Click  display=PRIMARY  x=400  y=400
    Click  window=Test Form  x=100  y=150
    Click  image=testClick.png
    Click  image=testClick.png  window=Test Form
    TRY
        Click  image=testClick.png  display=SECONDARY
        Fail  This image should not have been visible
    EXCEPT
        Log  Test Passed
    END