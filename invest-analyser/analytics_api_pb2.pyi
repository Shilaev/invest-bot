from google.protobuf.internal import containers as _containers
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class NumberArray(_message.Message):
    __slots__ = ("numbers",)
    NUMBERS_FIELD_NUMBER: _ClassVar[int]
    numbers: _containers.RepeatedScalarFieldContainer[float]
    def __init__(self, numbers: _Optional[_Iterable[float]] = ...) -> None: ...

class AnalyticsResult(_message.Message):
    __slots__ = ("result",)
    RESULT_FIELD_NUMBER: _ClassVar[int]
    result: float
    def __init__(self, result: _Optional[float] = ...) -> None: ...
