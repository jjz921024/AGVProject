#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <unistd.h>
#include <assert.h>
#include <ctype.h> 
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

int getServiceSocket(int port)
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
    struct sockaddr_in addr;
    memset(&addr,0,sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);
    addr.sin_addr.s_addr = INADDR_ANY;
        
    //bind
    int len = sizeof(addr);
    if(bind(sockfd,(struct sockaddr*)&addr, len) < 0)
    {
        fprintf(stderr,"bind: %s\n",strerror(errno));
        exit(1);
    }
        
    if(listen(sockfd,2) < 0)
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

    return fd;
}