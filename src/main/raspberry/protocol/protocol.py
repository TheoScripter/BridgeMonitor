from typing import Sequence, Union
from typing_extensions import Self
from dataclasses import dataclass, field
from abc import ABC, abstractmethod
from .units import Unit, AccelerometerUnit

@dataclass(frozen=True)
class IMU(ABC):
    """
    A class to represent a generic IMU data information

    ...

    Attributes
    ----------
    x : float
        Value associated with first coordinate
    y : float
        Value associated with second coordinate
    z : float
        Value associated with third coordinate
    unit : Unit
        Unit of the stored data
    calibration_factor : Union[float, Sequence[float]]
        Generic calibration factor[s] to be used on the convert() method
        so that the values are correctly scaled

    Methods
    -------
    info(additional=""):
        Prints the person's name and age.
    """
    x                  : float
    y                  : float
    z                  : float
    unit               : Unit
    calibration_factor : Union[float, Sequence[float]]

    @classmethod
    @abstractmethod
    def convert(cls, x: Union[int, float], y: Union[int, float], z: Union[int, float]) -> Self:
        return cls(x, y, z)

    def __bool__(self) -> bool:
        return not isinstance(self.unit, Unit)

    def __add__(self, imu: Self) -> Self:
        return self.__class__(
                self.x + imu.x,
                self.y + imu.y,
                self.z + imu.z,
                )

    def __sub__(self, imu: Self) -> Self:
        return self.__class__(
                self.x - imu.x,
                self.y - imu.y,
                self.z - imu.z,
                )

    # TODO: add scalar multiplication
    def __mul__(self, imu: Self) -> Self:
        return self.__class__(
                self.x * imu.x,
                self.y * imu.y,
                self.z * imu.z,
                )

    def __truediv__(self, imu: Self) -> Self:
        return self.__class__(
                self.x / imu.x,
                self.y / imu.y,
                self.z / imu.z,
                )

    def __int__(self) -> Self:
        return self * self.calibration_factor


@dataclass(frozen=True)
class AccelerometerValue(IMU):
    unit               : Unit                          = AccelerometerUnit()
    # Following the sensitivity on the datasheet, don't ask me why
    calibration_factor : Union[float, Sequence[float]] = 0.122 # for FS=+-4

    @classmethod
    def convert(cls, x: Union[int, float], y: Union[int, float], z: Union[int, float]) -> Self:
        return super().convert(x * cls.calibration_factor,
                               y * cls.calibration_factor,
                               z * cls.calibration_factor)
