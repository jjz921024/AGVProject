CC = gcc
TARGET = CarMain CarSocket ImgSocket

all:$(TARGET)
.PHONY:all

CarMain: CarMain.cpp
	$(CC) CarMain.cpp -o CarMain


CarSocket: CarSocket.cpp
	$(CC) CarSocket.cpp myutil.cpp -o CarSocket


ImgSocket: ImgSocket.cpp
	$(CC) ImgSocket.cpp myutil.cpp -o ImgSocket

clean:
	$(RM) $(TARGET)
