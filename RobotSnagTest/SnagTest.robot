*** Settings ***
Library  BuiltIn
Library    ../PySnagTest/src/SnagTest.py

*** Test Cases ***
Test Robot Setup
    Log  Set up correctly!

Test DynamicTest
    Connect  1338
    #Start    1338
    Set Display    1
    Add Image Path  ${CURDIR}/images
    ${res}=  Get Image Paths
    Log  ${res}
    # Will move the mouse to display 0 at fixed position 100 100
    Move Mouse To Display  0  100  100
    # Will move the mouse to fixed position on current display mouse is on
    Move Mouse To  500  200
     # Will move the mouse to display 1 at fixed position 100 100
    Move Mouse To Display  1  100  100
    # Will move the mouse to fixed position on current display mouse is on
    Move Mouse  500  300
    Move Mouse  -10  -50

    Move Mouse To Display  0  500  500
    Set Display  0
    Find Image  aCheckBox.png
    Find Image  buttonA.png

    Move Mouse To Image  textfield.png
    Move Mouse To Image  buttonA.png
    Click    LEFT
    Move Mouse To Image  textfield.png
    Click    LEFT
    Type  Hello
    Press Key  A

    # Intentional fail at the end to ensure it cannot find image
    #Find Image  failImage.png
    #[Teardown]  Shutdown

Start
    Start    1338

Test Shutdown
    Connect  1338
    Shutdown
