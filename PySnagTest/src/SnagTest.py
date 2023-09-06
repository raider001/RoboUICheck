import pathlib
import sys
import os
import time
import codecs
from robot.api import Failure
from robot.api.deco import keyword
from robot.libraries.Process import Process
from robot.libraries.Remote import Remote

sys.path.insert(0, str(pathlib.Path(__file__).parent))
try:
    from keywords import data
except ImportError:
    data = {}


def _path() -> str:
    return os.path.dirname(os.path.realpath(__file__)) + "\\"


class SnagTest:
    """
    Provides a library for interacting and validating programs within an Operating system.
    """
    ARGS = "args"
    DOC = "doc"
    keywords = {}
    remote: Remote | None = None

    def __init__(self):
        self.remote = None

    @keyword("Start", types={"port": int})
    def start(self, port: int = 1337):
        args: [str] = ["-jar", _path() + "\\..\\..\\JSnagTest\\target\\SnagTest.jar", "--port", str(port)]
        java = 'java'
        process: Process = Process()
        process.start_process(java, *args, shell=True)
        time.sleep(2)
        if not process.is_process_running():
            Failure("Service failed to start")
        self.remote = Remote("127.0.0.1:" + str(port) + "/")

    def generate_keywords(self):
        self._generate_keywords()

    def get_keyword_names(self):
        return list(data.keys())

    def run_keyword(self, name, args, kwargs):
        if name == "start":
            self.start(args[0])
        else:
            self.remote.run_keyword(name, args, kwargs)

    def get_keyword_arguments(self, name):
        return data[name][self.ARGS]

    def get_keyword_documentation(self, name):
        return data[name][self.DOC]

    def _generate_keywords(self):
        self.start(1337)
        keyword_data = {}
        keyword_names: [str] = self.remote.get_keyword_names()
        for name in keyword_names:
            print("Generating doc for " + name)
            keyword_data[name] = {}
            keyword_data[name][self.ARGS] = self.remote.get_keyword_arguments(name)
            keyword_data[name][self.DOC] = self.remote.get_keyword_documentation(name)

        self._add_non_dynamic_keywords(keyword_data)

        with codecs.open(os.path.join(os.path.abspath(os.path.dirname(__file__)), 'keywords.py'), 'w',
                         encoding='utf-8') as f:
            f.write('# -*- coding: utf-8 -*-\n')
            # keywords = ','.join(['"%s": %s' % (k, keywordDict[k]) for k in keywordDict.keys()])
            f.write('data = %s' % keyword_data)

        self.stop()

    def _add_non_dynamic_keywords(self, keywords):
        keywords["start"] = {}
        keywords["start"][self.ARGS] = ["port"]
        keywords["start"][self.DOC] = "Starts the Snag Test Service"

    def stop(self):
        self.remote.run_keyword("shutdown", [], {})
