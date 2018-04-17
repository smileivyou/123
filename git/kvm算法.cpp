#include <stdio.h>  
#include <stdlib.h>  
#include <time.h>  
#include <string.h>  
  
int next[1000];  //记录跳转位置   
int kmp_next(char *base)  
{  
    int i,j;  
    i=0;       
    j=-1;  
    next[0]=-1;      //记录第一个字符的跳转位置为-1   
    while(base[i]!='\0')  
    {  
        //如果跳转到-1，说明只能从头开始匹配；  
        //如果当前匹配点和跳转后的匹配点匹配   
        if(j==-1||(base[i]==base[j]))  
        {  
           //查看下一匹配点的情况   
           i++;  
           j++;  
           //如果下一匹配点还是匹配的，则下一跳地址一样   
           if(base[i]==base[j])  
           {  
               next[i]=next[j];  
           }  
           //如果一匹配点不匹配，则下一跳地址为当前前缀地址   
           else  
           {  
               next[i]=j;  
           }  
        }  
        else  
        {  
            j=next[j];  
        }  
    }  
}  
  
int kmp(char *b,char *base)  
{  
     int i,j;  
     i=0;j=0;  
     //通过跳转数组，进行快速匹配   
     while(b[i]!='\0'&&base[j]!='\0')  
     {  
        if(b[i]==base[j])  
        {  
            i++;j++;  
        }  
        else  
        {  
            j=next[j];  
            if(j==-1)  
            {  
               i++;j++;  
            }  
        }  
     }  
     if(base[j]=='\0')  
     {  
         return i-j;  
     }  
     else  
         return -1;  
}  
  
int main()  
{  
      char base[]="DABCDABD";  
      char b[1000];  
      kmp_next(base);  
      for(int i=0;i<strlen(base);i++)  
      {  
          printf("%d ",next[i]);  
      }  
      printf("\n %s \n",base);  
      while(scanf("%s",b)!=EOF)  
      {  
          printf("%d \n",kmp(b,base));                    
      }  
    system("pause");  
}  
