*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Suite Setup    Add Image Location  ${CURDIR}/


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
