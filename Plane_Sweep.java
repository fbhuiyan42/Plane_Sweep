
package plane_sweep;

import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;

class Line
{
    int x1,y1,x2,y2;
    int segno;
    
    boolean isHorizontal() 
    { 
        return (y1 == y2);
    }
    
    boolean isVertical()   
    { 
        return (x1 == x2); 
    }
    
    static void SortX(Line line[],int N)
    {
        int flag = 1;
        Line temp;
        for(int i=0;i< N && flag==1;i++)
        {
            flag = 0;
            for (int j=0;j <N-1;j++)
            {
                if (line[j].y1 > line[j+1].y1)
                {
                    temp = line[j];
                    line[j] = line[j+1];
                    line[j+1] = temp;
                    flag = 1;
                }
                else if (line[j].y2 > line[j+1].y2)
                {
                    temp = line[j];
                    line[j] = line[j+1];
                    line[j+1] = temp;
                    flag = 1;
                }
                else if (line[j].x1 > line[j+1].x1)
                {
                    temp = line[j];
                    line[j] = line[j+1];
                    line[j+1] = temp;
                    flag = 1;
                }
                else if (line[j].x2 > line[j+1].x2)
                {
                    temp = line[j];
                    line[j] = line[j+1];
                    line[j+1] = temp;
                    flag = 1;
                }
            }
         }
     }
}

class Point implements Comparable<Point>
{
    int x;
    Line line;
    boolean start;
    boolean horizontal;

    Point(int x,Line line,boolean st,boolean hor) 
    {
        this.x=x;
        this.line=line;
        this.start=st;
        this.horizontal=hor;
    }
    
    public int compareTo(Point P) 
    {
        if(this.x < P.x) return -1;
        else if (this.x > P.x) return +1;
        else return  0; 
    }
    
    static void Generate_Event_Points(Line line[],int N,PriorityQueue<Point> pq)
    {
        for (int i = 0; i < N; i++) 
        {
            if (line[i].isVertical()) 
            {
                Point P = new Point(line[i].x1,line[i],true,false);
                pq.add(P);
            }
            else if (line[i].isHorizontal()) {
                Point P1 = new Point(line[i].x1,line[i],true,true);
                Point P2 = new Point(line[i].x2,line[i],false,true);
                pq.add(P1);
                pq.add(P2);
            }
        }
    }
    
    static void linesweep(int N,PriorityQueue<Point> pq,int list[])
    {
        int count[]=new int[1];
        count[0]=0;
        int active[]=new int[N];
        int[] xcoord=new int[N];
        int n=0;
        for(int i=0;i<N;i++) active[i]=-1;
        while (!pq.isEmpty()) 
        {
            Point p = pq.remove();
            if (p.horizontal==false) 
            {
                int a=count[0];
                search(list,p.line.y1,p.line.y2,count,active,N,n);
                for(int l=a;l<count[0];l++) xcoord[l]=p.x;
            }

            else if (p.start==true) 
            {
                int index=binarySearch(active,0,n,p.line.y1 );
                if (index < 0) 
                {
                    index = - index - 1;
                }
                System.arraycopy(active, index, active, index+1, n - index);
                n++;
                active[index]=p.line.y1;
            }

            else if (p.start==false) 
            {
                int index=binarySearch(active,0,n,p.line.y1 );
                 if (index < 0) 
                {
                    index = - index - 1;
                }
                active[index]=-1;
            }
        }
        System.out.println("No. of intersection : "+ count[0]);
        System.out.println("Intersecting Points : ");
        for(int i=0;i<count[0];i++) System.out.println("( "+xcoord[i]+", "+list[i]+" ) ");
    }  
  
    static void search(int list[],int y1,int y2,int count[],int active[],int N,int size)
    {
        int index=count[0];
        int index1=binarySearch(active,0,size,y1 );
        int index2=binarySearch(active,0,size,y2 );
        if (index1 < 0) 
        {
            index1 = - index1 - 1;
        }
        if (index2 < 0) 
        {
            index2 = - index2 - 1;
        }
        count[0]=count[0]+index2-index1;
        for(int i=index1;i<index2;i++) list[index++]=active[i];
    }   
    
   static int binarySearch(int a[],int low,int nElems,int key) 
   {
        if (nElems == 0) return 0;
        int high = nElems - 1;
        int mid = 0;
        while (true)
        {
            mid = (high + low) / 2;
            if (a[mid] == key) return mid;
            else if (a[mid] < key)
            {
                low = mid + 1; // its in the upper
                if (low > high) return mid + 1;
            }
            else
            {
                high = mid - 1; // its in the lower
                if (low > high)return mid;
            }
        }
    }
}

public class Plane_Sweep 
{
    public static Scanner in;
    public static void main(String[] args) throws IOException 
    {
        in = new Scanner(new File("input.txt"));
        int N=in.nextInt();
        Line line[]=new Line[N];
        int seg=0;
        for(int i=0;i<N;i++)
        {
            line[i]=new Line();
            line[i].x1=in.nextInt();
            line[i].y1=in.nextInt();
            line[i].x2=in.nextInt();
            line[i].y2=in.nextInt();
            line[i].segno=i;
        }
        Line.SortX(line,N);
        PriorityQueue<Point> pq = new PriorityQueue<Point>();
        Point.Generate_Event_Points(line,N,pq);
        int list[]=new int[N];
        Point.linesweep(N,pq,list);
    }
}

