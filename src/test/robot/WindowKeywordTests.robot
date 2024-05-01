*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Library  Collections
Suite Setup   Run Keywords   Add Image Location  ${CURDIR}/  AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY  AND
...    Set Monitored Display    PRIMARY


*** Test Cases ***
Test Window Move
    [Documentation]  Verify that the window can be moved
    &{dimensions}=  Get Window Dimensions  Test Form
    Move Window  Test Form  100  200
    &{new_dimensions}=  Get Window Dimensions  Test Form
    IF  ${${dimensions}[x] + 100} != ${new_dimensions}[x] and ${${dimensions}[y] + 200} != ${new_dimensions}[y]
        Fail  Window did not move correctly.
    END

    IF  ${dimensions}[width] != ${new_dimensions}[width] and ${dimensions}[height] != ${new_dimensions}[height]
        Fail  Window width and height should not have moved.
    END
    [Teardown]  Move Window  Test Form  -100  -200

Test Move Window To Another Screen
    Move Window  Test Form  0  0  SECONDARY
    &{display}=  Get Display Dimensions    SECONDARY
    &{dimensions}=  Get Window Dimensions  Test Form
    IF  ${dimensions}[x] != ${display}[x] and ${dimensions}[y] != ${display}[y]
        Fail  Window did not move to the secondary display.
    END
    Move Window  Test Form  0  0  PRIMARY
    ${display}=  Get Display Dimensions    PRIMARY
    &{dimensions}=  Get Window Dimensions  Test Form
    IF  ${dimensions}[x] != ${display}[x] and ${dimensions}[y] != ${display}[y]
        Fail  Window did not move to the primary display.
    END

Test Resize Window
    [Documentation]  Verify that a window can be resized.
    &{dimensions}=  Get Window Dimensions  Test Form
    Resize Window  Test Form  500  500
    &{new_dimensions}=  Get Window Dimensions  Test Form
    IF  ${dimensions}[x] != ${new_dimensions}[x] and ${dimensions}[y] != ${new_dimensions}[y]
        Fail  Window should not have moved.
    END

    IF  ${new_dimensions}[width] != 500 and ${new_dimensions}[height] != 500
        Fail  Window did not resize correctly.
    END

    [Teardown]  Run Keywords  Sleep  1s  AND  Resize Window    Test Form  ${dimensions}[width]    ${dimensions}[height]

Test Get All Windows
    [Documentation]  Tests that windows are returned
    @{windows}=  Get All Available Windows
    List Should Contain Value    ${windows}    Test Form

Test Bring Window To Front
    [Documentation]  Tests that a window can be brought to the front
    Bring Window To Front  Blocking Form
    Verify Image Does Not Exist  testClick.png
    Bring Window To Front  Test Form
    Verify Image Exists  testClick.png
    [Teardown]  Bring Window To Front  Test Form

