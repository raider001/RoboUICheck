*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Suite Setup   Run Keywords  Add Image Location  ${CURDIR}/  AND
...    Bring Window To Front  Test Form    AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY



*** Test Cases ***
Test Verify Image Exists
   [Documentation]  Tests that the basic verify Image Exist keyword functions as expected.
   Verify Image Exists  test1.png

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
    Verify Image Does Not Exist On Display    test1.png   SECONDARY
    Verify Image Exists On Display    test1.png      PRIMARY

Test Image Verification On Form
    [Documentation]  Test form verification
    Verify Image Exists On Window    test1.png    Test Form
    Verify Image Does Not Exist On Window    fail.png    Test Form