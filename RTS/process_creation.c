#include<stdio.h>
#include<unistd.h>
#include<stdlib.h>

int main()
{
    int cpid;
    cpid=fork();
    if(cpid<0)
    {
        printf("\n Error ");
        exit(1);
    }
    else if(cpid==0)
    {
        printf("\n Hello I am the child process ");
        printf("\n My cpid is %d ",cpid);
        printf("\n My actual pid is %d \n ",getpid());
        exit(0);
    }
    else
    {
        printf("\n Hello I am the parent process ");
        printf("\n My actual pid is %d \n ",getpid());
        exit(1);
    }

}
