*** Settings ***
Library     BuiltIn
Library     ../PySnagTest/SnagTest.py


*** Test Cases ***
Test Robot Setup
    Log    Set up correctly!

Test dynamic Test
    Get Text
    Set Display    1
    Add Image Path    ${CURDIR}/images
    ${res}=    Get Image Paths
    Log    ${res}
    Set Primary Display Reference    PRIMARY
    Set Display Reference    SECONDARY    ABOVE    PRIMARY
    # Will move the mouse to display 0 at fixed position 100 100
    Move Mouse To Display    SECONDARY    100    100
    Set Timeout Time    1000
    # Will move the mouse to fixed position on current display mouse is on
    Move Mouse To    500    200
    # Will move the mouse to display 1 at fixed position 100 100
    Move Mouse To Display    PRIMARY    100    100
    # Will move the mouse to fixed position on current display mouse is on
    Move Mouse    500    300
    Move Mouse    -10    -50
    Move Mouse To Display    SECONDARY    500    500
    Set Display By Reference    SECONDARY
    Verify Image Exists    aCheckBox.png
    Verify Image Exists    buttonA.png

    Move Mouse To Image    textfield.png
    Move Mouse To Image    buttonA.png
    Click    LEFT
    Move Mouse To Image    textfield.png
    Click    LEFT
    Type    Hello
    Press Keys    CONTROL    A
    Verify Image Exists    failImage.png
    Verify failImage.png is on PRIMARY
    # Intentional fail at the end to ensure it cannot find image
    # Find Image    failImage.png
    # [Teardown]    Shutdown

Start
    Start    1338

Test Shutdown
    Connect    1338
    Shutdown
