import time
import os
import codecs
from urllib import request

from robot.libraries.Process import Process
from robot.libraries.Remote import Remote

try:
    from .keywords import KEYWORDS
except ImportError:
    pass


class SnagTest:

    ROBOT_LIBRARY_SCOPE = 'GLOBAL'
    ROBOT_LIBRARY_VERSION = 'DEV'

    GEN = 'GEN'
    def __init__(self, port: int = 1337, mode: str = 'START'):
        """
        Initialises SnagTest
        """
        self.url = "http://127.0.0.1:" + str(port) + "/"
        self.remote: Remote | None = None
        if mode == self.GEN:
            self._generate_keywords(port)
        if mode == "START":
            self.remote = self._start_service(port)
        if mode == "CONNECT":
            self.remote = self._connect()

    def _start_service(self, port: int) -> Remote:
        print("Start Service")
        args: [str] = ["-jar", "../../JSnagTest/target/SnagTest.jar", "--port", str(port)]
        java = 'java'
        process: Process = Process()
        process.start_process(java, *args, shell=True)
        time.sleep(2)
        if not process.is_process_running():
            raise Exception("Program failed to start.")
        return self._connect()

    def generate_keywords(self, port: int):
        keywordDict = {}
        try:
            keywordList = self.get_keyword_names()
            for keywordName in keywordList:
                keywordDict[keywordName] = {}
                keywordDict[keywordName]['arg'] = self.get_keyword_arguments(keywordName)
                keywordDict[keywordName]['doc'] = self.get_keyword_documentation(keywordName)
            with codecs.open(os.path.join(os.path.abspath(os.path.dirname(__file__)), 'keywords.py'), 'w',
                             encoding='utf-8') as f:
                f.write('# -*- coding: utf-8 -*-\n')
                # keywords = ','.join(['"%s": %s' % (k, keywordDict[k]) for k in keywordDict.keys()])
                f.write('KEYWORDS = %s' % keywordDict )
        finally:
            print("Complete")

    def _connect(self) -> Remote:
        print("Waiting for handshake")
        max_attempts: int = 5
        attempts: int = 0
        remote: Remote | None = None

        while attempts < max_attempts:
            attempts = attempts + 1
            try:
                request.urlopen(self.url).read()
                break
            except Exception:
                if attempts >= max_attempts:
                    raise Exception("Connection failed")
                else:
                    time.sleep(1)
        print("Handshake Made")
        return Remote(self.url, 30)

    def run_keyword(self, name: str, arguments: [] = None):
        return self.remote.run_keyword(name, arguments, None)

    def get_keyword_names(self):
        if self.mode == self.GEN:
            return self.remote.get_keyword_names()
        return list(KEYWORDS.keys())

    def get_keyword_arguments(self, name):
        if self.mode == self.GEN:
            return self.remote.get_keyword_arguments(name)
        return KEYWORDS[name]['arg']

    def get_keyword_documentation(self, name):
        if self.mode == self.GEN:
            return self.remote.get_keyword_documentation(name)
        return KEYWORDS[name]['doc']

