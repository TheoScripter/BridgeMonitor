from struct import unpack

class Ugly:
    @staticmethod
    def decode_temperature(raw_data: bytearray) -> float:
        return unpack("<f", raw_data)[0]
