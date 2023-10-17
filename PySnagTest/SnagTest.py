import os
import pathlib
import sys
import time

from robot.api import Failure
from robot.api.deco import keyword
from robot.libraries.Process import Process
from robot.libraries.Remote import Remote

sys.path.insert(0, str(pathlib.Path(__file__).parent))


def _path() -> str:
    return os.path.dirname(os.path.realpath(__file__)) + "\\"


class SnagTest:
    """
    Provides a library for interacting and validating programs within an Operating system.
    """
    remote: Remote = None

    @keyword("Connect", types={"port": int})
    def connect(self, port: int = 1338):
        self.remote = Remote("127.0.0.1:" + str(port) + "/")

    @keyword("Start", types={"port": int})
    def start(self, port: int = 1338):
        args: [str] = ["-jar", _path() + "\\..\\..\\JSnagTest\\target\\SnagTest.jar", "--port", str(port)]
        java = 'java'
        process: Process = Process()

        process.start_process(java, *args, shell=True)
        time.sleep(2)
        if not process.is_process_running():
            Failure("Service failed to start")
            self.remote = Remote("127.0.0.1:" + str(port) + "/")

    @keyword("Add Image Path")
    def add_image_path(self, path):
        """
        Add Image Path

| variable | default  | unit                  |
| path     |   N/A    | File Path location    |

Adds a path to search for images. Multiple paths can be added.


        """
        return self.remote.run_keyword("addImagePath", [path], None)

    @keyword("Set Display")
    def set_display(self, display_id):
        """
        Set Display

| variable       | default  | unit                  |
| displayId      |   0      | integer|String        |

Sets the display to look at.

        """
        return self.remote.run_keyword("setDisplay", [display_id], None)

    @keyword("Set Timeout Time")
    def set_timeout_time(self, timeout_time):
        """
        Set Timeout Time

| variable | default  | unit            |
| pollRate |   0.95   | percent decimal |

Sets the maximum time to wait for an image to appear. Note that the test event can take longer based on poll rate
as it does not include evaluation time.

        """
        return self.remote.run_keyword("setTimeoutTime", [timeout_time], None)

    @keyword("Set Display By Reference")
    def set_display_by_reference(self, display):
        """
        Set Display Reference

| variable       | default  | unit                  |
| displayId      |   N/A    | string                |

Sets the display to look at.

        """
        return self.remote.run_keyword("setDisplayByReference", [display], None)

    @keyword("Set Result Path")
    def set_result_path(self, result_path):
        """
        Set Result Path

| variable       | default  | unit                  |
| resultPath     |   ./    | File Path location    |

The result path should be defined as a relative path
based on where log files are written to.

        """
        return self.remote.run_keyword("setResultPath", [result_path], None)

    @keyword("Find Image")
    def find_image(self, image_path):
        """
        Find Image

| variable | default  | unit            |
| image |   0.95   | percent decimal    |

Looks for the given image on the screen.
The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
See:
- Set Timeout Time
- Set Minimum Simularity
- Set Poll Rate

for more information

        """
        return self.remote.run_keyword("findImage", [image_path], None)

    @keyword("Set Min Similarity")
    def set_min_similarity(self, min_similarity):
        """
        Set Min Similarity

| variable | default  | unit            |
| pollRate |   0.95   | percent decimal |

Sets the minimum accepted similarity as a percentage. When attempting to find image matching, if the image
score is below the given similarity, it wil lbe considered a failure

        """
        return self.remote.run_keyword("setMinSimilarity", [min_similarity], None)

    @keyword("Set Poll Rate")
    def set_poll_rate(self, poll_rate):
        """
        Set Poll Rate

| variable | default | unit         |
| pollRate |   100   | milliseconds |

Sets the rate the adaption will attempt to poll for updates.
The poll rate speed is dependent on the system it is actioned on.

Note that if the poll rate is too slow, validation can take longer as it validates the images received
sequentially to assess the actual time the image has been displayed on the screen (with the accuracy of the poll rate).


        """
        return self.remote.run_keyword("setPollRate", [poll_rate], None)

    @keyword("Get Image Paths")
    def get_image_paths(self):
        """
        Get Image Paths

Returns all the image paths registered to the agent.
Useful for debugging.

        """
        return self.remote.run_keyword("getImagePaths", [], None)

    @keyword("Set Capture Region")
    def set_capture_region(self, x, y, width, height):
        """
        Set Capture Region
Sets the capture region for the currently selected display.

| variable   | default                        | unit                                               |
| x          |   0                            | integer x >=0 | < display width                    |
| y          |   0                            | integer y >=0 | < display height                   |
| width      |   selected display width       | integer x + display width > 0 | < display width    |
| height     |   selected display height      | integer y + display width > 0 | < display width    |
The display is selected by:
- Set Display

        """
        return self.remote.run_keyword("setCaptureRegion", [x, y, width, height], None)

    @keyword("Ping")
    def ping(self):
        """
        Ping

        """
        return self.remote.run_keyword("ping", [], None)

    @keyword("Shutdown")
    def shutdown(self):
        """
        Shutdown
Shuts down JSnagTest

        """
        return self.remote.run_keyword("shutdown", [], None)

    @keyword("Set Psm")
    def set_psm(self, psm):
        """
        Set PSM
Existing Modes
0  OSD_ONLY Orientation and script detection (OSD) only.
1  AUTO_OSD Automatic page segmentation with OSD.
2  AUTO_ONLY Automatic page segmentation, but no OSD, or OCR.
3  AUTO Fully automatic page segmentation, but no OSD. (Default)
4  SINGLE_COLUMN Assume a single column of text of variable sizes.
5  SINGLE_BLOCK_VERT_TEXT Assume a single uniform block of vertically aligned text.
6  SINGLE_COLUMN Assume a single uniform block of text.
7  SINGLE_LINE Treat the image as a single text line.
8  SINGLE_WORD Treat the image as a single word.
9  CIRCLE_WORD Treat the image as a single word in a circle.
10 SINGLE_CHAR Treat the image as a single character.
11 SPARSE_TEXT Sparse text. Find as much text as possible in no particular order.
12 SPARSE_TEXT_OSD Sparse text with OSD.
13 RAW_LINE Raw line. Treat the image as a single text line, bypassing hacks that are Tesseract-

        """
        return self.remote.run_keyword("setPsm", [psm], None)

    @keyword("Hold Key")
    def hold_key(self, key):
        """
        Holds the requested key. It wont be released until the release key has been called

Available Options:
 - ENTER
 - BACK_SPACE
 - TAB
 - CANCEL
 - CLEAR
 - SHIFT
 - CONTROL
 - ALT
 - PAUSE
 - CAPSLOCK
 - ESCAPE
 - SPACE
 - PAGE_UP
 - PAGE_DOWN
 - END
 - HOME
 - LEFT
 - UP
 - RIGHT
 - DOWN
 - DELETE
 - F1
 - F2
 - F3
 - F4
 - F5
 - F6
 - F7
 - F8
 - F9
 - F10
 - F11
 - F12
 - F13
 - F14
 - F15
 - F16
 - F17
 - F18
 - F19
 - F20
 - F21
 - F22
 - F23
 - F24
 - PRINT_SCREEN
 - INSERT
 - HELP
 - META
 - A
 - B
 - C
 - D
 - E
 - F
 - G
 - H
 - I
 - J
 - K
 - L
 - M
 - N
 - O
 - P
 - Q
 - R
 - S
 - T
 - U
 - V
 - W
 - X
 - Y
 - Z

        """
        return self.remote.run_keyword("holdKey", [key], None)

    @keyword("Press Keys")
    def press_keys(self, key1, key2=None, key3=None, key4=None, key5=None):
        """
        presses a set of keys, then releases them immediately afterwords.

Available Options:
 - ENTER
 - BACK_SPACE
 - TAB
 - CANCEL
 - CLEAR
 - SHIFT
 - CONTROL
 - ALT
 - PAUSE
 - CAPSLOCK
 - ESCAPE
 - SPACE
 - PAGE_UP
 - PAGE_DOWN
 - END
 - HOME
 - LEFT
 - UP
 - RIGHT
 - DOWN
 - DELETE
 - F1
 - F2
 - F3
 - F4
 - F5
 - F6
 - F7
 - F8
 - F9
 - F10
 - F11
 - F12
 - F13
 - F14
 - F15
 - F16
 - F17
 - F18
 - F19
 - F20
 - F21
 - F22
 - F23
 - F24
 - PRINT_SCREEN
 - INSERT
 - HELP
 - META
 - A
 - B
 - C
 - D
 - E
 - F
 - G
 - H
 - I
 - J
 - K
 - L
 - M
 - N
 - O
 - P
 - Q
 - R
 - S
 - T
 - U
 - V
 - W
 - X
 - Y
 - Z

        """
        return self.remote.run_keyword("pressKeys", [key1, key2, key3, key4, key5], None)

    @keyword("Release Key")
    def release_key(self, key):
        """
        Releases the requested key

Available Options:
 - ENTER
 - BACK_SPACE
 - TAB
 - CANCEL
 - CLEAR
 - SHIFT
 - CONTROL
 - ALT
 - PAUSE
 - CAPSLOCK
 - ESCAPE
 - SPACE
 - PAGE_UP
 - PAGE_DOWN
 - END
 - HOME
 - LEFT
 - UP
 - RIGHT
 - DOWN
 - DELETE
 - F1
 - F2
 - F3
 - F4
 - F5
 - F6
 - F7
 - F8
 - F9
 - F10
 - F11
 - F12
 - F13
 - F14
 - F15
 - F16
 - F17
 - F18
 - F19
 - F20
 - F21
 - F22
 - F23
 - F24
 - PRINT_SCREEN
 - INSERT
 - HELP
 - META
 - A
 - B
 - C
 - D
 - E
 - F
 - G
 - H
 - I
 - J
 - K
 - L
 - M
 - N
 - O
 - P
 - Q
 - R
 - S
 - T
 - U
 - V
 - W
 - X
 - Y
 - Z

        """
        return self.remote.run_keyword("releaseKey", [key], None)

    @keyword("Type")
    def type(self, message):
        """
        Types the given message

        """
        return self.remote.run_keyword("type", [message], None)

    @keyword("Move Mouse To Display")
    def move_mouse_to_display(self, display, x, y):
        """
        Move Mouse To Display

| variable  | default | unit         |
| display   |   N/A   | monitorId    |
| x         |   N/A   | pixel        |
| y         |   N/A   | pixel        |


        """
        return self.remote.run_keyword("moveMouseToDisplay", [display, x, y], None)

    @keyword("Move Mouse To")
    def move_mouse_to(self, x, y):
        """
        Move Mouse To

        """
        return self.remote.run_keyword("moveMouseTo", [x, y], None)

    @keyword("Set Mouse Move Speed")
    def set_mouse_move_speed(self, speed):
        """
        Set Mouse Move Speed
        """
        return self.remote.run_keyword("setMouseMoveSpeed", [speed], None)

    @keyword("Move Mouse To Image")
    def move_mouse_to_image(self, image):
        """
        Move Mouse To Image
Moves the mouse to the center of the matched image on the screen.

        """
        return self.remote.run_keyword("moveMouseToImage", [image], None)

    @keyword("Move Mouse")
    def move_mouse(self, x_relative, y_relative):
        """
        Move Mouse

| variable  | default | unit         |
| xRelative |   N/A   | pixel        |
| yRelative |   N/A   | pixel        |

Moves the mouse a relative distance based on its original location.

        """
        return self.remote.run_keyword("moveMouse", [x_relative, y_relative], None)

    @keyword("Click")
    def click(self, button, count=1):
        """
        Simulates a Mouse click
Available options are:
 - LEFT
 - MIDDLE
 - RIGHT

        """
        return self.remote.run_keyword("click", [button, count], None)

    @keyword("Set Display Reference")
    def set_display_reference(self, arg0, arg1, arg2):
        """
            Sets the reference display to the given reference name
    Options:
     LEFT
     RIGHT
     ABOVE
     BELOW
     SMALLER_THAN
     LARGER_THAN

        """
        return self.remote.run_keyword("setDisplayReference", [arg0, arg1, arg2], None)

    @keyword("Set Primary Display Reference")
    def set_primary_display_reference(self, reference_name):
        """
        Sets the primary display to the given reference name

        """
        return self.remote.run_keyword("setPrimaryDisplayReference", [reference_name], None)

