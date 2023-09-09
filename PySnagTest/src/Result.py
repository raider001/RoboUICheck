import json


class Result:
    """
    Basic result container for json objects sent from an external application.
    """
    _isSuccess: bool
    _message: str

    def __init__(self, data: str):
        loaded: dict = json.loads(data)
        self._isSuccess = loaded["isSuccess"]
        self._message = loaded["info"]

    def is_success(self) -> bool:
        return self._isSuccess

    def get_message(self) -> str:
        return self._message
