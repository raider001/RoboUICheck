*** Settings ***
Library  Remote         http://127.0.0.1:7442/
Library  BuiltIn
Suite Setup   Run Keywords  Add Image Location  ${CURDIR}/  AND
...    Bring Window To Front  Test Form    AND
...    Set Primary Display Reference    PRIMARY    AND
...    Set Display Reference    PRIMARY    SMALLER_THAN    SECONDARY  AND
...    Set Result Path  ${OUTPUT_DIR}


*** Test Cases ***
Test Keyboard KeyPress
    [Documentation]    Test Keyboard KeyPress
    Click  image=textarea.png  window=Test Form
    Press Keys    A
    Press Keys    B
    Press Keys    C
    Press Keys    D
    Press Keys    E
    Press Keys    F
    Press Keys    G
    Press Keys    H
    Press Keys    I
    Press Keys    J
    Press Keys    K
    Press Keys    L
    Press Keys    M
    Press Keys    N
    Press Keys    O
    Press Keys    P
    Press Keys    Q
    Press Keys    R
    Press Keys    S
    Press Keys    T
    Press Keys    U
    Press Keys    V
    Press Keys    W
    Press Keys    X
    Press Keys    Y
    Press Keys    Z
    Press Keys  SHIFT  A
    Press Keys  SHIFT  B
    Press Keys  SHIFT  C
    Press Keys  SHIFT  D
    Press Keys  SHIFT  E
    Press Keys  SHIFT  F
    Press Keys  SHIFT  G
    Press Keys  SHIFT  H
    Press Keys  SHIFT  I
    Press Keys  SHIFT  J
    Press Keys  SHIFT  K
    Press Keys  SHIFT  L
    Press Keys  SHIFT  M
    Press Keys  SHIFT  N
    Press Keys  SHIFT  O
    Press Keys  SHIFT  P
    Press Keys  SHIFT  Q
    Press Keys  SHIFT  R
    Press Keys  SHIFT  S
    Press Keys  SHIFT  T
    Press Keys  SHIFT  U
    Press Keys  SHIFT  V
    Press Keys  SHIFT  W
    Press Keys  SHIFT  X
    Press Keys  SHIFT  Y
    Press Keys  SHIFT  Z
    Verify Image Exists On Window    alphabet.png    Test Form
    Press Keys  CONTROL  A
    Verify Image Exists On Window    alphabetselected.png    Test Form
    Press Keys  BACK_SPACE
    Verify Image Does Not Exist On Window    alphabetselected.png    Test Form

Test Typing
    Click  image=textarea.png  window=Test Form
    Type  Hello world! this is a demonstration of typing with this framework.
    Verify Image Exists On Window    sentence.png    Test Form
    Press Keys  CONTROL  A
    Press Keys  BACK_SPACE