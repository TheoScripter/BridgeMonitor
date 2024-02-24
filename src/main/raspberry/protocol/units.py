from abc import ABC
from dataclasses import dataclass
from typing_extensions import Self
from .singleton import ABCSingletonMeta, Singleton


@dataclass(frozen=True)
class Unit(ABC, metaclass=ABCSingletonMeta):
    name : str
    description : str

@dataclass(frozen=True)
class AccelerometerUnit(Unit, metaclass=ABCSingletonMeta):
    name: str = "mg"
    description: str = "10^-3 g (gravity acceleration)"

@dataclass(frozen=True)
class TemperatureUnit(Unit, metaclass=ABCSingletonMeta):
    name: str = "°C"
    description: str = "Temperature in degrees celcius"
