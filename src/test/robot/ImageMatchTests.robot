*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Library  Collections
Suite Setup   Run Keywords  Add Image Location  ${CURDIR}/  AND
...    Bring Window To Front  Test Form    AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY  AND
...    Set Match Percentage    .95  AND
...    Set Result Path  ${OUTPUT_DIR}


*** Test Cases ***
Test Verify Image Exists
   [Documentation]  Tests that the basic verify Image Exist keyword functions as expected.
   Verify Image Exists  testClick.png

Verify Image Match Fails Properly
    [Documentation]  Tests that the match score resets back to original match score after a test case
    Verify Image Does Not Exist   fail.png

Test Stored Match Score Remains
    [Documentation]  Tests that the match score is correctly stored and is not modified for one-time changes.
    Verify Image Exists    fail.png    minMatchScore=0.005
    Verify Image Does Not Exist   fail.png

Test Match Percentage
    [Documentation]  Tests that the match percentage can be set and retrieved
    Verify Image Exists    fail.png    minMatchScore=0.005
    Verify Image Does Not Exist  fail.png   minMatchScore=0.95

Test Image Mask Verification
    [Documentation]  Tests that the image mask verification works as expected
    Verify Image Exists    transparencyTest.png

Test Image Verification On Display
    [Documentation]    Basic test to demosntrate display ID referencing and Display Image Verification
    Verify Image Does Not Exist On Display    testClick.png   SECONDARY
    Verify Image Exists On Display    testClick.png      PRIMARY

Test Image Verification On Form
    [Documentation]  Test form verification
    Verify Image Exists On Window    testClick.png    Test Form
    Verify Image Does Not Exist On Window    fail.png    Test Form

Test Image Bounds Verification
    [Documentation]  Tests that the image bounds verification works as expected
    &{bounds}    Get Image Bounds  testClick.png
    Dictionary Should Contain Key  ${bounds}  x
    Dictionary Should Contain Key  ${bounds}  y
    Dictionary Should Contain Key  ${bounds}  width
    Dictionary Should Contain Key  ${bounds}  height
    Get Image Bounds    testClick.png  window=Test Form
    TRY
        Get Image Bounds    testClick.png  display=SECONDARY
        Fail  Should not have gotten bounds as image does not exist on secondary display.
    EXCEPT
        Log  Test Passed
    END

