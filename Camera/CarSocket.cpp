#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <unistd.h>
#include <assert.h>
#include <ctype.h> 
#include "myutil.h"

void do_service(int fd, int *pipe_ctof){
	unsigned char buf_sock[10];
	
	if(recv(fd, buf_sock, sizeof(buf_sock), 0) > 0) //从上位机接收数据    socket读是否阻塞
	{			
		if(write(pipe_ctof[1], buf_sock, sizeof(buf_sock)) < 0)//向父进程发送
		{
			fprintf(stderr,"pipe write error: %s\n", strerror(errno));
		}
		//printf("socket rec:%x,%x,%x,%x,%x,%x,%x\n",buf_sock[0],buf_sock[1],buf_sock[2],buf_sock[3],buf_sock[4],buf_sock[5],buf_sock[6]);
	}
}


/*void out_client(struct sockaddr_in clientaddr) //输入客户端信息
{
		char buffer[16];
		inet_ntop(AF_INET, &clientaddr.sin_addr.s_addr, buffer, sizeof(clientaddr));
		unsigned short port = ntohs(clientaddr.sin_port);
		printf("client ip:%s (%d)\n",buffer,port);
}*/ 

int main(int argc, char *argv[])
{
		int pipe_ctof[2];	
		//int pipe_ftoc[2];
		
		//子进程 写
		pipe_ctof[0] = m_atoi(argv[1]);
		pipe_ctof[1] = m_atoi(argv[2]);

		//子进程 读
/*		pipe_ftoc[0] = m_atoi(argv[3]);
		pipe_ftoc[1] = m_atoi(argv[4]);*/

		int fd = getServiceSocket(9191);
		
		close(pipe_ctof[0]);
		//close(pipe_ftoc[1]);
		while(1)
		{
			do_service(fd, pipe_ctof);
		
		}
		close(pipe_ctof[1]);
		close(fd);

		return 0;
}























