*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Library  Collections
Suite Setup    Add Image Location  ${CURDIR}/


*** Test Cases ***
Test Window Move
    [Documentation]  Verify that the window can be moved
    @{dimensions}=  Get Window Dimensions  Test Form
    Move Window  Test Form  100  200
    @{new_dimensions}=  Get Window Dimensions  Test Form

    IF  ${${dimensions}[0] + 100} != ${new_dimensions}[0] and ${${dimensions}[1] + 200} != ${new_dimensions}[1]
        Fail  Window did not move correctly.
    END

    IF  ${dimensions}[2] != ${new_dimensions}[2] and ${dimensions}[3] != ${new_dimensions}[3]
        Fail  Window width and height should not have moved.
    END
    [Teardown]  Move Window  Test Form  -100  -200

Test Resize Window
    [Documentation]  Verify that a window can be resized.
    @{dimensions}=  Get Window Dimensions  Test Form
    Resize Window  Test Form  500  500
    @{new_dimensions}=  Get Window Dimensions  Test Form
    IF  ${dimensions}[0] != ${new_dimensions}[0] and ${dimensions}[1] != ${new_dimensions}[1]
        Fail  Window should not have moved.
    END

    IF  ${new_dimensions}[2] != 500 and ${new_dimensions}[3] != 500
        Fail  Window did not resize correctly.
    END

    [Teardown]  Run Keywords  Sleep  1s  AND  Resize Window    Test Form  ${dimensions}[2]    ${dimensions}[3]

Test Get All Windows
    [Documentation]  Tests that windows are returned
    @{windows}=  Get All Available Windows
    List Should Contain Value    ${windows}    Test Form

Test Bring Window To Front
    [Documentation]  Tests that a window can be brought to the front
    Bring Window To Front  Blocking Form
    Verify Image Does Not Exist  test1.png
    Bring Window To Front  Test Form
    Verify Image Exists  test1.png
    [Teardown]  Bring Window To Front  Test Form

