*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Suite Setup   Run Keywords  Add Image Location  ${CURDIR}/  AND
...    Bring Window To Front  Test Form    AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY  AND
...    Set Match Percentage    .9


*** Test Cases ***
Test Move Mouse To Image
   Move Mouse To  image=testClick.png

Test Move Mouse To Image On Display
   &{bounds}=  Get Image Bounds    testClick.png    display=PRIMARY
   &{display_bounds}=  Get Display Dimensions  PRIMARY
   Move Mouse To  image=testClick.png  display=PRIMARY
   &{mouse_position}=  Get Mouse Position
   ${x}=  Evaluate  ${display_bounds}[x] + ${bounds}[x] + ${bounds}[width] / 2
   ${y}=  Evaluate  ${display_bounds}[y] + ${bounds}[y] + ${bounds}[height] / 2
   ${x_range}=  Evaluate ${x} - ${mouse_position}[x]
   ${y_range}=  Evaluate ${y} - ${mouse_position}[y]
   IF  ${x_range} > 3 or ${x_range} < 3 or ${y_range} > 3 or ${y_range}
       Fail  Mouse did not move to the correct location
   END

   Move Window  Test Form  x=0  y=0  display=SECONDARY
   &{bounds}=  Get Image Bounds    testClick.png    display=SECONDARY
   &{display_bounds}=  Get Display Dimensions  SECONDARY
   Move Mouse To  image=testClick.png  display=SECONDARY
   &{mouse_position}=  Get Mouse Position
   ${x}=  Evaluate  ${display_bounds}[x] + ${bounds}[x] + ${bounds}[width] / 2
   ${y}=  Evaluate  ${display_bounds}[y] + ${bounds}[y] + ${bounds}[height] / 2
   ${x_range}=  Evaluate ${x} - ${mouse_position}[x]
   ${y_range}=  Evaluate ${y} - ${mouse_position}[y]
   IF  ${x_range} > 3 or ${x_range} < 3 or ${y_range} > 3 or ${y_range}
       Fail  Mouse did not move to the correct location
   END
   [Teardown]  Move Window  Test Form  x=0  y=0  display=PRIMARY

Test Move Mouse To Image On Window
   &{bounds}=  Get Image Bounds    testClick.png  window=Test Form
   &{display_bounds}=  Get Display Dimensions  PRIMARY
   Move Mouse To  image=testClick.png  window=Test Form
   &{mouse_position}=  Get Mouse Position
   ${x}=  Evaluate  ${display_bounds}[x] + ${bounds}[x] + ${bounds}[width] / 2
   ${y}=  Evaluate  ${display_bounds}[y] + ${bounds}[y] + ${bounds}[height] / 2

   IF  ${x} != ${mouse_position}[x] or ${y} != ${mouse_position}[y]
       Fail  Mouse did not move to the correct location
   END

   Move Window  Test Form  x=0  y=0  display=SECONDARY
   &{bounds}=  Get Image Bounds    testClick.png  window=Test Form
   &{display_bounds}=  Get Display Dimensions  SECONDARY
    Move Mouse To  image=testClick.png  window=Test Form
    &{mouse_position}=  Get Mouse Position
    ${x}=  Evaluate  ${display_bounds}[x] + ${bounds}[x] + ${bounds}[width] / 2
    ${y}=  Evaluate  ${display_bounds}[y] + ${bounds}[y] + ${bounds}[height] / 2

    IF  ${x} != ${mouse_position}[x] or ${y} != ${mouse_position}[y]
        Fail  Mouse did not move to the correct location
    END
    [Teardown]  Move Window  Test Form  x=0  y=0  display=PRIMARY

Test Move Mouse Between Displays
   Move Mouse    100    0
   Move Mouse To  display=SECONDARY  x=100  y=100
   Move Mouse To  x=400  y=400
   Move Mouse To  display=PRIMARY  x=100  y=100

Test Click Image
   Click  image=testClick.png
   Verify Image Exists On Window    labelconfirmation.png    Test Form
   [Teardown]  Click  image=testClick.png  window=Test Form

Test Click Image On Window
   Click  image=testClick.png  window=Test Form
   Verify Image Exists On Window    labelconfirmation.png  Test Form
   [Teardown]  Click  image=testClick.png  window=Test Form

Test Click X Times
    Move Mouse To  image=clickCounter.png
    Click  times=2
    Verify Image Exists On Window    two.png    Test Form
    [Teardown]  Click  image=testClick.png  window=Test Form

Test Click Location
    &{dimensions}    Get Window Dimensions  Test Form
    Click  x=${${dimensions}[x]+50}  y=${${dimensions}[y]+50}
    Verify Image Exists On Window    labelconfirmation.png    Test Form
    [Teardown]  Click  x=${${dimensions}[x]+50}  y=${${dimensions}[y]+50}

Test Press And Release
    Move Mouse To    image=PAndRTest.png
    Press Mouse Button  Left
    Verify Image Exists On Window  pressed.png  Test Form
    Release Mouse Button  Left
    Verify Image Exists On Window  released.png  Test Form
    [Teardown]  Click  image=testClick.png  window=Test Form

Test Wheel Up And Down
    Move Mouse To    image=PAndRTest.png
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