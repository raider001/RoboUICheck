*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Suite Setup   Run Keywords  Add Image Location  ${CURDIR}/  AND
...    Bring Window To Front  Test Form    AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY  AND
...    Set Match Percentage    .9  AND
...    Set Result Path  ${OUTPUT_DIR}


*** Test Cases ***

Test Click Location
    &{dimensions}    Get Window Dimensions  Mouse Latency Test Form
    Click  x=${${dimensions}[x]+50}  y=${${dimensions}[y]+50}
    Click  x=${${dimensions}[x]+900}  y=${${dimensions}[y]+150}
