#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <unistd.h>
#include <assert.h>
#include <ctype.h> 




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

/* 
 * atoi implementation 
 */  
int m_atoi(const char * str)
{  
    assert(str);  
      
    while(isspace(*str)) ++str;  
      
    int neg_flag = 0;  
    if('+' == *str)  
    {  
        neg_flag = 0;  
        ++ str;  
    }  
    else if('-' == *str)  
    {  
        neg_flag = 1;  
        ++ str;  
    }  
      
    int retv = 0;  
    while(isdigit(*str))  
    {  
        retv = retv * 10 + *str++ - '0';  
    }  
      
    return neg_flag ? 0-retv : retv;  
}  


int main(int argc, char *argv[])
{
	struct sockaddr_in clientaddr;
	int c_len = sizeof(clientaddr);
		
	int sockfd;
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if(sockfd < 0)
	{
		fprintf(stderr,"socket: %s\n", strerror(errno));
		exit(1);
	}
			
	//实现端口复用 ？？？ 为什么释放了socket还提示地址被占用？？
	int reuse = 1;
    setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof(reuse));

	//set ip and port
	struct sockaddr_in img_addr;
	memset(&img_addr,0,sizeof(img_addr));
	img_addr.sin_family = AF_INET;
	img_addr.sin_port = htons(m_atoi("9999"));
	img_addr.sin_addr.s_addr = INADDR_ANY;
			
	//bind
	int len = sizeof(img_addr);
	if(bind(sockfd,(struct sockaddr*)&img_addr, len) < 0)
	{
		fprintf(stderr,"Imgbind: %s\n",strerror(errno));
		exit(1);
	}
			
	if(listen(sockfd,1) < 0)
	{
		fprintf(stderr,"listen: %s\n",strerror(errno));
		exit(1);
	}
		
	int fd;	
	fd = accept(sockfd, (struct sockaddr*)&clientaddr, (socklen_t*)&c_len);
	if(fd < 0)
	{
		fprintf(stderr,"accept: %s\n",strerror(errno));	
	}
		
	do_service(fd);
	
	close(fd);
	close(sockfd);
	return 0;
}