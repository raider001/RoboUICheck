# Keyword Design

The following keywords will be the final keywords for the first release for this library. The keywords are broken down
into the following categories.

*Note Italics indicates optional*

## Display Keywords


| Keyword                             | Attributes                         | Default | Return Value | DescriptionUsage                                                  | Implemented                                       |
| ----------------------------------- | ---------------------------------- | ------- | ------------ | ----------------------------------------------------------------- |---------------------------------------------------|
| Set Primary Display Reference       | referenceName                      |         |              | Sets a Reference name for a display                               | ![question.svg](assets/question.svg)              |
| Set Display By Id                   | displayId                          |         |              |                                                                   | ![question.svg](assets/question.svg)              |
| Set Display Reference               | referenceName, inRelationTo        |         |              | Sets a Reference based on an existing reference display           | ![question.svg](assets/question.svg)              |
| Set Monitored Display               | referenceName                      |         |              | Sets the Display being monitors                                   | ![question.svg](assets/question.svg)              |
| Set Monitored Area                  | x,y,width,height                   |         |              | Sets the monitored area for the selected display                  | ![question.svg](assets/question.svg)              |
| Reset Monitored Area                |                                    |         |              | Resets the monitored area to the original display size            | ![question.svg](assets/question.svg)              |
| Reset Monitored Area For Display Id | displayId                          |         |              |                                                                   | ![question.svg](assets/question.svg)              |
| Set Monitored Area For Display      | referenceName, x, y, width, height |         |              | Sets the monitored area for a given display reference             | ![question.svg](assets/question.svg)              |
| Reset Monitored Area For Display    | referenceName                      |         |              | Resets the given monitored display area for the specified monitor | ![question.svg](assets/question.svg)              |

## Settings Keywords 


| Keyword                   | Attributes           | Default | Return Value | Description                                      | Implemented                           |
| ------------------------- | -------------------- | ------- | ------------ | ------------------------------------------------ |---------------------------------------|
| Set Timeout Time          | waitTime             |         |              | How long a match will try to find an image.      | ![question.svg](assets/question.svg)  |
| Set Poll Rate             | pollrate             |         |              |                                                  | ![question.svg](assets/question.svg)  |
| Get Image Paths           |                      |         | List[String] |                                                  | ![question.svg](assets/question.svg)  |
| Set Result Path           | path                 |         |              | The location for the image results/              | ![question.svg](assets/question.svg)  |
| Set Match Percentage      | matchPercentage      |         |              | The percentage the image must match as a minimum | ![question.svg](assets/question.svg)  |
| Add Image Location        | path                 |         |              | Adds a path to search for images.                | ![question.svg](assets/question.svg)  |
| Set Command Delay Speed   | delayTime            |         |              | Delay Time after each command                    | ![cross.svg](assets/cross.svg)        |
| Set Keystroke Speed       | timeBetweenKeystroke | 20ms    |              | Time between keystrokes                          | ![question.svg](assets/question.svg)  |
| Set OCR Segmentation Mode | mode                 |         |              | Sets the OCR Segmentation Mode                   | ![cross.svg](assets/cross.svg)        |
| Set OCR Language          | language             |         |              | Sets the language for OCR                        | ![cross.svg](assets/cross.svg)        |
| Set OCR Engine            | engine               |         |              | Sets the OCR Engine                              | ![cross.svg](assets/cross.svg)        |

## Window Keywords


| Keyword                   | Attributes                | Default | Return Value                 | Description                                  | Implemented                          |
| ------------------------- | ------------------------- | ------- |------------------------------| -------------------------------------------- |--------------------------------------|
| Get All Available Windows |                           |         | Array[windowName]            |                                              | ![question.svg](assets/question.svg) |
| Get Window Dimensions     | windowName                |         | Dictionary(x,y,width,height) | returns the dimensions of a window.          | ![question.svg](assets/question.svg) |
| Bring Window To Front     | windowName                |         | N/A                          | Bring the window to the front of visibility. | ![question.svg](assets/question.svg) |
| Send Window To Back       | windowName                |         | N/A                          | Sends the window behind all other windows.   | ![cross.svg](assets/cross.svg)       |
| Resize Window             | windowName, width, height |         |                              | Resizes the window to the given dimensions.  | ![question.svg](assets/question.svg) |
| Move Window               | windowName, x, y          |         |                              | Moves the window to the given coordinates.   | ![question.svg](assets/question.svg) |

## Image Match Keywords


| Keyword                                | Attributes                                                           | Default | Return       | Description                                             | Implemented                          |
| -------------------------------------- | -------------------------------------------------------------------- | ------- | ------------ | ------------------------------------------------------- |--------------------------------------|
| Verify Image Exists                    | imageName,*minMatchScore*, *waitTime*                                |         | actualScore  | Finds an Image in a the defined display area.           | ![question.svg](assets/question.svg) |
| Verify Image Exists On Display         | imageName, referenceName,*minMatchScore, waitTime*                   |         | actualScore  | Searches a specific display for the image defined       | ![question.svg](assets/question.svg) |
| Verify Image Exits On Window           | imageName, windowName,*minMatchScore, waitTime*                      |         | actualScore  | Searches a specific form for the defined image.         | ![cross.svg](assets/cross.svg)       |
| Verify Image Does Not Exist            | imageName,*minMatchScore*, *waitTime*                                |         | closestMatch | Verifies a image is not visible on the default display. | ![cross.svg](assets/cross.svg)       |
| Verify Image Does Not Exist On Display | imageName, referenceName,*minMatchScore, waitTime*                   |         | closestMatch |                                                         | ![cross.svg](assets/cross.svg)       |
| Verify Image Does Not Exist On Window  | imageName, windowName,*minMatchScore, waitTime*                      |         | closestMatch |                                                         | ![cross.svg](assets/cross.svg)       |
| Get Image Bounds                       | imageName                                                            |         | Dictionary   | Returns the bounds of the image on the default display. | ![cross.svg](assets/cross.svg)       |
| Get Image Bounds On Display            | imageName, referenceName                                             |         | Dictionary   |                                                         | ![cross.svg](assets/cross.svg)       |
| Get Image bounds On Window             | imageName, windowName                                                |         | Dictionary   |                                                         | ![cross.svg](assets/cross.svg)       |
| Move Mouse To Image                    | imageName                                                            |         |              |                                                         | ![cross.svg](assets/cross.svg)       |
| Click Image                            | imageName                                                            |         |              |                                                         | ![cross.svg](assets/cross.svg)       |
| Click Image On Display                 | imageName, referenceName                                             |         |              |                                                         | ![cross.svg](assets/cross.svg)       |
| Click Image On Window                  | imageName, windowName                                                |         |              |                                                         | ![cross.svg](assets/cross.svg)       |
| Drag Image To Image                    | sourceImage, targetImage                                             |         |              |                                                         | ![cross.svg](assets/cross.svg)       |
| Drag Image To Relative Location        | sourceImage, x, y                                                    |         |              |                                                         | ![cross.svg](assets/cross.svg)       |
| Drag Image To Fixed                    | sourceImage, x, y                                                    |         |              |                                                         | ![cross.svg](assets/cross.svg)       |
| Drag Image From Display To Display     | sourceImage, sourceDisplay,x1,y1, targetImage, targetDisplay, x2, y2 |         |              |                                                         | ![cross.svg](assets/cross.svg)       |

## Text Recognition


| Keyword                       | Attributes             | Returns                                    | Description             | Implemented                     |
| ----------------------------- | ---------------------- | ------------------------------------------ | ----------------------- |---------------------------------|
| Get Text                      |                        | All Interpreted Text                       | All Text on set display | ![cross.svg](assets/cross.svg)  |
| Get Text on Display           | displayReference       | All Interprested Test in Display Reference |                         | ![cross.svg](assets/cross.svg)  |
| Get Text on Window            | windowName             | All Interpreted Text in Window             |                         | ![cross.svg](assets/cross.svg)  |
| Verify Text Exists            | text                   |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Verify Text Exists On Display | text, displayReference |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Verify Text Exists On Window  | text, windowName       |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Verify Text In Image          | text, imageName        |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Click Text                    | text                   |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Click Text On Display         | text, displayReference |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Click Text On Window          | text, windowName       |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Click Text In Image           | text, imageName        |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Move Mouse To Text            | text                   |                                            | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Get Text Bounds               | text                   | Dictionary                                 | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Get Text Bounds On Window     | text, windowName       | Dictionary                                 | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |
| Get Text Bounds Matched Image | text, imageName        | Dictionary                                 | Regex Matching enabled  | ![cross.svg](assets/cross.svg)  |

## Controls


| Keyword               | Attributes          | Default | Returns | Description | Implemented                               |
| --------------------- | ------------------- | ------- | ------- | ----------- |-------------------------------------------|
| Move Mouse To         | x, y                |         |         |             | ![question.svg](assets/question.svg)      |
| Move Mouse To Display | referenceName, x, y |         |         |             | ![question.svg](assets/question.svg)      |
| Move Mouse To Image   | imageName           |         |         |             | ![question.svg](assets/question.svg)      |
| Move Mouse            | x,y                 |         |         |             | ![question.svg](assets/question.svg)      |
| Click Location        | x,y                 |         |         |             | ![cross.svg](assets/cross.svg)            |
| Type                  | text                |         |         |             | ![question.svg](assets/question.svg)      |
| Copy Selected Test    |                     |         |         |             | ![cross.svg](assets/cross.svg)            |
| Paste Selected Text   |                     |         |         |             | ![cross.svg](assets/cross.svg)            |
| Get Selected Text     |                     |         | text    |             | ![cross.svg](assets/cross.svg)            |
| Paste Text            | text                |         |         |             | ![cross.svg](assets/cross.svg)            |
| Press Keys            | keys                |         |         |             | ![question.svg](assets/question.svg)      |
| Hold Keys             | keys                |         |         |             | ![question.svg](assets/question.svg)      |
| Release Keys          | keys                |         |         |             | ![question.svg](assets/question.svg)      |
| Press Mouse Button    | button              |         |         |             | ![cross.svg](assets/cross.svg)            |
| Release Mouse Button  | button              |         |         |             | ![cross.svg](assets/cross.svg)            |
| Click Mouse Button    | button,_times_      |         |         |             | ![question.svg](assets/question.svg)      |
| Mouse Scroll Up       | ticks               |         |         |             | ![cross.svg](assets/cross.svg)            |
| Mouse Scroll Down     | ticks               |         |         |             | ![cross.svg](assets/cross.svg)            |
