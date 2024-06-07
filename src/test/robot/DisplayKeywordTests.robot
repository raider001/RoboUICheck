*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Library  Collections
Suite Setup   Run Keywords  Add Image Location  ${CURDIR}/    AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY  AND
...    Set Result Path  ${OUTPUT_DIR}

Test Setup   Set Monitored Display    PRIMARY

*** Test Cases ***
Test Change Monitored Displays
    [Documentation]    Test the monitored area
    Set Monitored Display    SECONDARY
    &{secondary}  Get Selected Display Dimensions
    Set Monitored Display    PRIMARY
    &{primary}  Get Selected Display Dimensions
    IF  ${primary}[x] == ${secondary}[x] and ${primary}[y] == ${secondary}[y]
        Fail  The primary and secondary displays are the same
    END

Test Change Monitored Display Region
    [Documentation]   Test the defined region for monitoring on each screen.
    Set Monitored Area    50    50    100    100
    &{area}  Get Selected Display Monitored Area
    IF  ${area}[x] != 50 or ${area}[y] != 50 or ${area}[width] != 100 or ${area}[height] != 100
        Fail  The selected area is not correct
    END
    Reset Monitored Area
    &{area}  Get Selected Display Monitored Area
    IF  ${area}[x] == 50 and ${area}[y] == 50 and ${area}[width] == 100 and ${area}[height] == 100
        Fail  The selected area is not correct
    END

Test Set Display By Id
    [Documentation]   Test setting the display by id
    Set Display By Id    0
    &{display_one}  Get Selected Display Dimensions
    Set Display By Id    1
    &{display_two}  Get Selected Display Dimensions
    IF  ${display_two} == ${display_one}
        Fail  The display is meant to be different from the first one
    END
    [Teardown]  Set Monitored Display  PRIMARY