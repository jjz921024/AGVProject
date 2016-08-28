#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
	

/*
	清除文件描述符的FD_CLOEXEC控制位
*/
void clr_fd(int fd)
{
    int val;
    printf("hahahahaha\n");
    if(val = fcntl(fd, F_GETFD) < 0)
    	fprintf(stderr,"fcntl F_GETFD errno %s\n",strerror(errno));

    val &= ~FD_CLOEXEC; 

    if(fcntl(fd,F_SETFD,val) < 0)
    	fprintf(stderr,"fcntl F_SETFD errno %s\n",strerror(errno));
}


int main(int argc, char *argv[])
{	

	int pipe_ftoc[2];
	int pipe_ctof[2];

	//管道	父进程读管道
	if(pipe2(pipe_ctof, O_NONBLOCK) <  0)
	{
		fprintf(stderr,"pipe %s\n",strerror(errno));		
	}
	//父进程写管道  该管道设置为非阻塞
/*	if(pipe2(pipe_ftoc, O_NONBLOCK) <  0) 
	{
		fprintf(stderr,"pipe %s\n",strerror(errno));		
	}*/


	//读
	char pipe_ctof0[30], pipe_ctof1[30];
	memset(pipe_ctof0, 0, sizeof(pipe_ctof0));  
	memset(pipe_ctof1, 0, sizeof(pipe_ctof1));

	snprintf(pipe_ctof0, sizeof(pipe_ctof0), "%d", pipe_ctof[0]);  
	snprintf(pipe_ctof1, sizeof(pipe_ctof1), "%d", pipe_ctof[1]);

	//写
/*	char pipe_ftoc0[30], pipe_ftoc1[30];
	memset(pipe_ftoc0, 0, sizeof(pipe_ftoc0));  
	memset(pipe_ftoc1, 0, sizeof(pipe_ftoc1));

	snprintf(pipe_ftoc0, sizeof(pipe_ftoc0), "%d", pipe_ftoc[0]);  
	snprintf(pipe_ftoc1, sizeof(pipe_ftoc1), "%d", pipe_ftoc[1]);*/


	pid_t pid;
	pid = fork(); 
	
	if(pid < 0)
	{
		fprintf(stderr,"fork error :%s\n",strerror(errno));
		exit(1);
	}
	else if(pid > 0)//parent process
	{
		close(pipe_ctof[1]);
		close(pipe_ftoc[0]);
		unsigned char buf_rec[10] = {};

		/*
			主进程接收CarSocket发来的字节，处理业务逻辑
		*/
		while(1)
		{	
			if(read(pipe_ctof[0], buf_rec, sizeof(char)) > 0) //不阻塞   是否			//sizeof(buf_rec)
			{
				if(buf_rec[0] == 0x66)  //拍照指令
				{
					/*
						调用ImgSocket进程，完成拍照，传图片
					*/
					pid_t img_pid;
					img_pid = fork(); 
					if(img_pid < 0)
					{
						fprintf(stderr,"fork error :%s\n",strerror(errno));
						exit(1);
					}
					else if(img_pid == 0)
					{
						execl("/home/pi/jjz/ImgSocket", "ImgSocket", (char *)0);
					}
					
				}
				//printf("main rec:%x,%x,%x,%x,%x,%x,%x\n",buf_rec[0],buf_rec[1],buf_rec[2],buf_rec[3],buf_rec[4],buf_rec[5],buf_rec[6]);
			}			
		}
		close(pipe_ctof[0]);
	}
	else  //child process
	{	
		/*
			调用接收数据的Socket进程
		*/	
		execl("/home/pi/jjz/CarSocket", "CarSocket", pipe_ctof0, pipe_ctof1, (char *)0);
	}
}