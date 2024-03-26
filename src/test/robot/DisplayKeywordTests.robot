*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Library  Collections
Suite Setup   Run Keywords  Add Image Location  ${CURDIR}/    AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY

Test Setup   Set Monitored Display    PRIMARY

*** Test Cases ***
Test Change Monitored Displays
    [Documentation]    Test the monitored area
    Set Monitored Display    SECONDARY
    @{secondary}  Get Monitored Display
    Set Monitored Display    PRIMARY
    @{primary}  Get Monitored Display
    IF  ${primary}[0] == ${secondary}[0] and ${primary}[1] == ${secondary}[1]
        Fail  The primary and secondary displays are the same
    END

Test Change Monitored Display Region
    [Documentation]   Test the defined region for monitoring on each screen.
    Set Monitored Area    50    50    100    100
    &{area}  Get Selected Monitored Area
    IF  ${area}[x] != 50 or ${area}[y] != 50 or ${area}[width] != 100 or ${area}[height] != 100
        Fail  The selected area is not correct
    END
    Reset Monitored Area
    &{area}  Get Selected Monitored Area
    IF  ${area}[x] == 50 and ${area}[y] == 50 and ${area}[width] == 100 and ${area}[height] == 100
        Fail  The selected area is not correct
    END

Test Set Display By Id
    [Documentation]   Test setting the display by id
    Set Display By Id    0
    @{display_one}  Get Monitored Display
    Set Display By Id    1
    @{display_two}  Get Monitored Display
    IF  ${display_two} == ${display_one}
        Fail  The display is meant to be different from the first one
    END