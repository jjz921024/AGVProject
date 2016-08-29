#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <unistd.h>
#include <assert.h>
#include <ctype.h> 
#include "myutil.h"


/*
	打开图片并发送
*/
void do_service(int fd){
			
	FILE *img_fd;

	printf("take a photo......\n");
	system("raspistill -o 1.jpeg");

	printf("prepare send photo\n");
	img_fd = fopen("/home/pi/jjz/1.jpeg", "rb");
	if(img_fd == NULL)
	{
		fprintf(stderr,"file no found: %s\n", strerror(errno));		
	}

	fseek(img_fd, 0, SEEK_END);
	int filelen = ftell(img_fd);
	fseek(img_fd, 0, SEEK_SET);

	char img_buf[filelen + 1];

	//fread(img_buf, sizeof(char), filelen, img_fd);


	if(fread(img_buf, sizeof(char), filelen, img_fd) >= filelen)
	{
		printf("sending....\n");
		if(send(fd, img_buf, sizeof(img_buf), 0) < 0)
		{
			fprintf(stderr,"socket send error: %s\n", strerror(errno));		
		}
		
	}
	printf("finish to send a photo\n");
}


int main(int argc, char *argv[])
{
	
	int fd = getServiceSocket(9999);	
	
	do_service(fd);
	
	close(fd);
	return 0;
}