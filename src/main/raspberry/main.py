from bleak.backends.device import BLEDevice
from bleak import BleakClient, BleakScanner
from bleak.backends.characteristic import BleakGATTCharacteristic
import requests
import json
import logging
import asyncio
from enum import Enum
from datetime import datetime
from ugly import Ugly
import requests
from typing import List
from typing_extensions import Self
import os
from dataclasses import dataclass

class BLEServices(Enum):
    AccelGyroService = "00000000-0001-11e1-9ab4-0002a5d5c51b"
    pass

class BLECharacteristics(Enum):
    AccelGyroChar = "00140000-0001-11e1-ac36-0002a5d5c51b"


logging.getLogger(__name__)
logging.basicConfig(level=logging.INFO)


@dataclass
class ConfigurationManager:
    api_endpoint: str
    allowed_addresses: List[str]
    
    @classmethod
    def load_config(cls, path=os.curdir) -> Self:
        try:
            with open(os.path.join(path, "config.json"), "r") as file:
                config = json.load(file)
                return cls(
                            api_endpoint = config.get("api_endpoint"),
                            allowed_addresses = config.get("allowed_addresses")
                       )

        except FileNotFoundError:
            logging.error("Config file not found. Using default parameters.")
        except json.JSONDecodeError as e:
            logging.error(f"Error on config file json decoding : {e}. Using default parameters.")
        return cls(
            api_endpoint = "http://localhost:8080/",
            allowed_addresses = []
        )


def accel_gyro_handler(sender: BleakGATTCharacteristic, data: bytearray):
    # data received in little endian format
    print("received notification!")
    print(f"{sender.description}: {data}")

def temp_handler(sender: BleakGATTCharacteristic, data :bytearray):
    timestamp = datetime.utcnow().isoformat(timespec='microseconds')[:-3]
    logging.debug("received notification!")
    value = Ugly.decode_temperature(data)
    logging.info(f"Temperature: {value:.1f} at {timestamp}")

    response = requests.post("http://localhost:8887/releves/analogiques", 
                  json = 
                  {
                   "id": 1,
                   "dateTimeReleve": str(timestamp),
                   "valeur": round(value, 1),
                   "capteurAnalogiqueId": str(1)
                  }
                 )

    logging.info(response)
    logging.info(response.json())


async def connect(d: BLEDevice):
    client = BleakClient(d)
    await client.connect()
    for service in client.services:
        print('\t')
        print(service)
        for char in service.characteristics:
            print('\t')
            print(char)
    await client.start_notify("00140000-0001-11e1-ac36-0002a5d5c51b", accel_gyro_handler)
        # if service in BLEServices:
        #     pass


async def main():
    config = ConfigurationManager.load_config()
    device = await BleakScanner.find_device_by_address(
            config.allowed_addresses[0]
            )
    async with BleakClient(device) as client:
        await client.start_notify("00e00000-0001-11e1-ac36-0002a5d5c51b", accel_gyro_handler)
        await client.start_notify("00140000-0001-11e1-ac36-0002a5d5c51b", temp_handler)
        await asyncio.sleep(100000000)

asyncio.run(main())
