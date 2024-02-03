# Keyword Design

The following keywords will be the final keywords for the first release for this library. The keywords are broken down
into the following categories.

*Note Italics indicates optional*

## Display Keywords


| Keyword                             | Attributes                         | Default | Return Value | DescriptionUsage                                                  | Implemented                | Verified On Windows | Verified on linux |
| ----------------------------------- | ---------------------------------- | ------- | ------------ | ----------------------------------------------------------------- |----------------------------|---------------------|-------------------|
| Set Primary Display Reference       | referenceName                      |         |              | Sets a Reference name for a display                               | ![ok.svg](assets/ok.svg)   |![cross.svg](assets/cross.svg) |![cross.svg](assets/cross.svg) |
| Set Display By Id                   | displayId                          |         |              |                                                                   | ![ok.svg](assets/ok.svg)   |![cross.svg](assets/cross.svg) |![cross.svg](assets/cross.svg) |
| Set Display Reference               | referenceName, inRelationTo        |         |              | Sets a Reference based on an existing reference display           | ![ok.svg](assets/ok.svg)   |![cross.svg](assets/cross.svg) |![cross.svg](assets/cross.svg) |
| Set Monitored Display               | referenceName                      |         |              | Sets the Display being monitors                                   | ![ok.svg](assets/ok.svg)   |![cross.svg](assets/cross.svg) |![cross.svg](assets/cross.svg) |
| Set Monitored Area                  | x,y,width,height                   |         |              | Sets the monitored area for the selected display                  | ![ok.svg](assets/ok.svg)   |![cross.svg](assets/cross.svg) |![cross.svg](assets/cross.svg) |
| Reset Monitored Area                |                                    |         |              | Resets the monitored area to the original display size            | ![ok.svg](assets/ok.svg)   |![cross.svg](assets/cross.svg) |![cross.svg](assets/cross.svg) |
| Reset Monitored Area For Display Id | displayId                          |         |              |                                                                   | ![ok.svg](assets/ok.svg)   |![cross.svg](assets/cross.svg) |![cross.svg](assets/cross.svg) |
| Set Monitored Area For Display      | referenceName, x, y, width, height |         |              | Sets the monitored area for a given display reference             | ![ok.svg](assets/ok.svg)   |![cross.svg](assets/cross.svg) |![cross.svg](assets/cross.svg) |
| Reset Monitored Area For Display    | referenceName                      |         |              | Resets the given monitored display area for the specified monitor | ![ok.svg](assets/ok.svg)   |![cross.svg](assets/cross.svg) |![cross.svg](assets/cross.svg) |

## Settings Keywords 


| Keyword                   | Attributes           | Default | Return Value | Description                                      | Implemented                    | Verified On Windows              | Verified on Linux               |
| ------------------------- | -------------------- | ------- | ------------ | ------------------------------------------------ |--------------------------------|----------------------------------|---------------------------------|
| Set Timeout Time          | waitTime             |         |              | How long a match will try to find an image.      | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Set Poll Rate             | pollrate             |         |              |                                                  | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |                                  |
| Get Image Paths           |                      |         | List[String] |                                                  | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Set Result Path           | path                 |         |              | The location for the image results/              | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Set Match Percentage      | matchPercentage      |         |              | The percentage the image must match as a minimum | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Add Image Location        | path                 |         |              | Adds a path to search for images.                | ![ok.svg](assets/ok.svg)       |  ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Set Command Delay Speed   | delayTime            |         |              | Delay Time after each command                    | ![cross.svg](assets/cross.svg) | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Set Keystroke Speed       | timeBetweenKeystroke | 20ms    |              | Time between keystrokes                          | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Set OCR Segmentation Mode | mode                 |         |              | Sets the OCR Segmentation Mode                   | ![cross.svg](assets/cross.svg) | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Set OCR Language          | language             |         |              | Sets the language for OCR                        | ![cross.svg](assets/cross.svg) | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Set OCR Engine            | engine               |         |              | Sets the OCR Engine                              | ![cross.svg](assets/cross.svg) | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |

## Window Keywords


| Keyword                   | Attributes                | Default | Return Value                 | Description                                  | Implemented                    | Verified On Windows              | Verified on Linux               |
| ------------------------- | ------------------------- | ------- |------------------------------| -------------------------------------------- |--------------------------------|----------------------------------|---------------------------------|
| Get All Available Windows |                           |         | Array[windowName]            |                                              | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Get Window Dimensions     | windowName                |         | Dictionary(x,y,width,height) | returns the dimensions of a window.          | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Bring Window To Front     | windowName                |         | N/A                          | Bring the window to the front of visibility. | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Send Window To Back       | windowName                |         | N/A                          | Sends the window behind all other windows.   | ![cross.svg](assets/cross.svg) | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Resize Window             | windowName, width, height |         |                              | Resizes the window to the given dimensions.  | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Move Window               | windowName, x, y          |         |                              | Moves the window to the given coordinates.   | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |

## Image Match Keywords


| Keyword                                | Attributes                                                           | Default | Return       | Description                                             | Implemented                        | Verified On Windows              | Verified on Linux               |
| -------------------------------------- | -------------------------------------------------------------------- | ------- | ------------ | ------------------------------------------------------- |------------------------------------|----------------------------------|---------------------------------|
| Verify Image Exists                    | imageName,*minMatchScore*, *waitTime*                                |         | actualScore  | Finds an Image in a the defined display area.           | ![ok.svg](assets/ok.svg)           | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Verify Image Exists On Display         | imageName, referenceName,*minMatchScore, waitTime*                   |         | actualScore  | Searches a specific display for the image defined       | ![ok.svg](assets/ok.svg)           | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Verify Image Exits On Window           | imageName, windowName,*minMatchScore, waitTime*                      |         | actualScore  | Searches a specific form for the defined image.         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Verify Image Does Not Exist            | imageName,*minMatchScore*, *waitTime*                                |         | closestMatch | Verifies a image is not visible on the default display. | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Verify Image Does Not Exist On Display | imageName, referenceName,*minMatchScore, waitTime*                   |         | closestMatch |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Verify Image Does Not Exist On Window  | imageName, windowName,*minMatchScore, waitTime*                      |         | closestMatch |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Get Image Bounds                       | imageName                                                            |         | Dictionary   | Returns the bounds of the image on the default display. | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Get Image Bounds On Display            | imageName, referenceName                                             |         | Dictionary   |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Get Image bounds On Window             | imageName, windowName                                                |         | Dictionary   |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Move Mouse To Image                    | imageName                                                            |         |              |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Click Image                            | imageName                                                            |         |              |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Click Image On Display                 | imageName, referenceName                                             |         |              |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Click Image On Window                  | imageName, windowName                                                |         |              |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Drag Image To Image                    | sourceImage, targetImage                                             |         |              |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Drag Image To Relative Location        | sourceImage, x, y                                                    |         |              |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Drag Image To Fixed                    | sourceImage, x, y                                                    |         |              |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |
| Drag Image From Display To Display     | sourceImage, sourceDisplay,x1,y1, targetImage, targetDisplay, x2, y2 |         |              |                                                         | ![cross.svg](assets/cross.svg)     | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)  |

## Text Recognition


| Keyword                       | Attributes             | Returns                                    | Description             | Implemented                      | Verified On Windows               | Verified on Linux                 |
| ----------------------------- | ---------------------- | ------------------------------------------ | ----------------------- |----------------------------------|-----------------------------------|-----------------------------------|
| Get Text                      |                        | All Interpreted Text                       | All Text on set display | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Get Text on Display           | displayReference       | All Interprested Test in Display Reference |                         | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Get Text on Window            | windowName             | All Interpreted Text in Window             |                         | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Verify Text Exists            | text                   |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Verify Text Exists On Display | text, displayReference |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Verify Text Exists On Window  | text, windowName       |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Verify Text In Image          | text, imageName        |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Click Text                    | text                   |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Click Text On Display         | text, displayReference |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Click Text On Window          | text, windowName       |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Click Text In Image           | text, imageName        |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Move Mouse To Text            | text                   |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Get Text Bounds               | text                   | Dictionary                                 | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Get Text Bounds On Window     | text, windowName       | Dictionary                                 | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Get Text Bounds Matched Image | text, imageName        | Dictionary                                 | Regex Matching enabled  | ![cross.svg](assets/cross.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |

## Controls


| Keyword               | Attributes          | Default | Returns | Description | Implemented                    | Verified On Windows               | Verified on Linux                 |
|-----------------------| ------------------- | ------- | ------- | ----------- |--------------------------------|-----------------------------------|-----------------------------------|
| Move Mouse To         | x, y                |         |         |             | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Move Mouse To Display | referenceName, x, y |         |         |             | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Move Mouse To Image   | imageName           |         |         |             | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Move Mouse            | x,y                 |         |         |             | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Click Location        | x,y                 |         |         |             | ![cross.svg](assets/cross.svg) | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Type                  | text                |         |         |             | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Copy Selected Text    |                     |         |         |             | ![cross.svg](assets/cross.svg) | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Paste Selected Text   |                     |         |         |             | ![cross.svg](assets/cross.svg) | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Get Selected Text     |                     |         | text    |             | ![cross.svg](assets/cross.svg) | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Paste Text            | text                |         |         |             | ![cross.svg](assets/cross.svg) | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Press Keys            | keys                |         |         |             | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Hold Keys             | keys                |         |         |             | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Release Keys          | keys                |         |         |             | ![ok.svg](assets/ok.svg)  ![ok.svg](assets/ok.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Release Mouse Button  | button              |         |         |             | ![ok.svg](assets/ok.svg)  | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Click Mouse Button    | button,_times_      |         |         |             | ![ok.svg](assets/ok.svg)       | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Mouse Scroll Up       | ticks               |         |         |             | ![ok.svg](assets/ok.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |
| Mouse Scroll Down     | ticks               |         |         |             | ![ok.svg](assets/ok.svg)   | ![cross.svg](assets/cross.svg)    | ![cross.svg](assets/cross.svg)    |