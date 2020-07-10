class script:
    self._file = open("./asd.png", 'rb+')

    if chunk_type == self._PUNK_CHUNK_TYPE:

        print "Found a punk chunk", len(content), "bytes. Writing to file"
        self._output.write(bytearray(content))
        self._output.close()
        self._file.close()
        