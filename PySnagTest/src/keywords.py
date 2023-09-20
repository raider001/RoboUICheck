# -*- coding: utf-8 -*-
data = {'stop_remote_server': {'args': [], 'doc': 'Stops the remote server.\n\nThe server may be configured so that users cannot stop it.'}, 'moveMouseToDisplay': {'args': ['display', 'x', 'y'], 'doc': 'Move Mouse To Display\n'}, 'setDisplay': {'args': ['arg0'], 'doc': 'Set Display\nSets the display to look at.\n'}, 'moveMouseTo': {'args': ['x', 'y'], 'doc': 'Move Mouse To\n'}, 'ping': {'args': [], 'doc': 'Ping\n'}, 'setResultPath': {'args': ['arg0'], 'doc': 'Set Result Path\nThe result path should be defined as a relative path\nbased on where log files are written to.\n'}, 'setPsm': {'args': ['psm'], 'doc': 'Set PSM\nExisting Modes\n0  OSD_ONLY Orientation and script detection (OSD) only.\n1  AUTO_OSD Automatic page segmentation with OSD.\n2  AUTO_ONLY Automatic page segmentation, but no OSD, or OCR.\n3  AUTO Fully automatic page segmentation, but no OSD. (Default)\n4  SINGLE_COLUMN Assume a single column of text of variable sizes.\n5  SINGLE_BLOCK_VERT_TEXT Assume a single uniform block of vertically aligned text.\n6  SINGLE_COLUMN Assume a single uniform block of text.\n7  SINGLE_LINE Treat the image as a single text line.\n8  SINGLE_WORD Treat the image as a single word.\n9  CIRCLE_WORD Treat the image as a single word in a circle.\n10 SINGLE_CHAR Treat the image as a single character.\n11 SPARSE_TEXT Sparse text. Find as much text as possible in no particular order.\n12 SPARSE_TEXT_OSD Sparse text with OSD.\n13 RAW_LINE Raw line. Treat the image as a single text line, bypassing hacks that are Tesseract-\n'}, 'findImage': {'args': ['image'], 'doc': 'Find Image\n'}, 'setPollRate': {'args': ['Set Poll Rate'], 'doc': 'Set Poll Rate\n'}, 'getImagePaths': {'args': [], 'doc': 'Get Image Paths\n'}, 'setCaptureRegion': {'args': ['arg0', 'arg1', 'arg2', 'arg3'], 'doc': 'Set Capture Region\nSets the display to look at.\n'}, 'click': {'args': ['button'], 'doc': 'Simulates a Mouse click\nAvailable options are:\n<ul>\n<li>LEFT</li><li>MIDDLE</li><li>RIGHT</li>\n</ul>\n\n'}, 'addImagePath': {'args': ['path'], 'doc': 'Add Image Path\n'}, 'setTimeoutTime': {'args': ['timeoutTime'], 'doc': 'Set Timeout Time\n'}, 'moveMouseToImage': {'args': ['image'], 'doc': 'Move Mouse To Image\nMoves the mouse to the center of the matched image on the screen.\n'}, 'setMouseMoveSpeed': {'args': ['speed'], 'doc': 'Set Mouse Move Speed'}, 'setMinSimilarity': {'args': ['Minimum Similarity'], 'doc': 'Set Min Similarity\n'}, 'moveMouse': {'args': ['xRelative', 'yRelative'], 'doc': 'Move Mouse\nMoves the mouse a relative distance based on its original location.\n'}, 'shutdown': {'args': [], 'doc': 'Shutdown\nShuts down JSnagTest\n'}, 'start': {'args': ['port'], 'doc': 'Starts the Snag Test Service'}, 'connect': {'args': ['port'], 'doc': 'Connects to the Snag Test Service'}}