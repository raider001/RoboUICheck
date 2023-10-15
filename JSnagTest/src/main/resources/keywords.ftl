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

<#list methods as method>
    @keyword("${method.robotName}")
    def ${method.pyName}(self<#if method.arguments?size != 0>, <#list method.arguments as arg>${arg.argName}<#if arg.defaultVal?trim != ''>=${arg.defaultVal}</#if><#if arg_has_next>, </#if></#list></#if>):
        """
        ${method.document}
        """
        return self.remote.run_keyword("${method.methodName}"<#if method.arguments?size != 0>, [<#list method.arguments as arg>${arg.argName}<#if arg_has_next>, </#if></#list>]<#else>, []</#if>, None)

</#list>