CC = gcc
TARGET = CarMain CarSocket ImgSocket

all:$(TARGET)
.PHONY:all

CarMain: CarMain.cpp
	$(CC) CarMain.cpp -o CarMain


CarSocket: CarSocket.cpp
	$(CC) CarSocket.cpp -o CarSocket


ImgSocket: ImgSocket.cpp
	$(CC) ImgSocket.cpp -o ImgSocket

clean:
	$(RM) $(TARGET)
