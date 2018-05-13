import java.util.Stack;
import java.util.NoSuchElementException;
import java.util.Scanner;
public class Queue<E> extends Stack<E>{
    private  Stack<E> stk;
    private int maxSize=0;
    public Queue( ){ };
    public Queue(int m_size)
    {	
    	super();
    	maxSize=m_size;
    }
    public int getLen()
    {
    	if(!this.isEmpty())
    		return this.size();
    	else return stk.size();
    }
    public boolean add(E e)
    {
    	/*the queue is full*/
    	if(this.size()>=maxSize)
    		throw new IllegalStateException();
    	/*s1 is not empty or both s1 and s2 is empty*/
    	if(!this.isEmpty()||this.isEmpty()&&stk.isEmpty())
    		this.push(e);
    	/*s1 is empty but s2 not*/
    	else if(this.isEmpty()&&!stk.isEmpty())
		{
			while(!stk.isEmpty())
				this.push(stk.pop());
			this.push(e);
		}
		else;
    	return true;
    }
    public boolean offer(E e)
    {  
    	if(this.getLen()>=maxSize)
    		return false;
		if(!this.isEmpty()||this.isEmpty()&&stk.isEmpty())
			this.push(e);
		else if(this.isEmpty()&&!stk.isEmpty())
		{
			while(!stk.isEmpty())
				this.push(stk.pop());
			this.push(e);
		}
		else;
		return true;
    }
    public E remove( )
    {
    	/*s2 is not empty,s2.pop*/
    	if(!stk.isEmpty())
		{
			E e=stk.pop();
			return e;
		}
		/*s1 is empty,throw an Exception*/
		else if(this.isEmpty()){
    		throw new NoSuchElementException();
		}
		else{
			/*s1 is empty but s2 not,s1 pop to s2*/
			/*return the top element of s2*/
    		while(!this.isEmpty())
				stk.push(this.pop());
    		E e=stk.pop();
    		return e;
		}
    }
    public E poll( )
    {
		if(!stk.isEmpty())
		{
			E e=stk.pop();
			return e;
		}
		else if(this.isEmpty()){
			return null;
		}
		else{
			while(!this.isEmpty())
				stk.push(this.pop());
			E e=stk.pop();
			return e;
		}
    }
    public E m_peek( ) 
    {
    	if(!stk.isEmpty())
    		return stk.peek();
		else if(!this.isEmpty())
		{
			while(!this.isEmpty())
				stk.push(this.pop());
			return stk.peek();
		}
		else return null;
    }
    public E element( )
    {
		if(!stk.isEmpty())
			return stk.peek();
		else if(!this.isEmpty())
		{
			while(!this.isEmpty())
				stk.push(this.pop());
			return stk.peek();
		}
		else{
			throw  new NoSuchElementException();
		}
    }
    public boolean isEmp(){
		if(this.isEmpty()&&stk.isEmpty())
			return true;
		else return false;
	}

   public static void main(String [] args)
   {
	   Queue<Integer> queue=new Queue<>(4);
	   queue.stk=new Stack<>();
	   Scanner input=new Scanner(System.in);
	   System.out.println("input the number of data:");
	   int num=input.nextInt();
	   System.out.println("input the data:");
	   for(int i=0;i<num;i++)
	   {
		   int a=input.nextInt();
		   queue.add(new Integer(a));
	   }
	   System.out.println("add "+num+" integers,the size is "+queue.getLen());
	   System.out.println("peek the top element "+queue.m_peek());
	   System.out.println("remove the top element "+queue.remove());
	   System.out.println("the top element is "+queue.element()+",the size is "+queue.getLen());
	   System.out.println("poll the top element "+queue.poll()+",the size is "+queue.getLen());
	   System.out.println("offer two integers:");
	   int a=input.nextInt();
	   int b=input.nextInt();
	   queue.offer(new Integer(a));
	   queue.offer(new Integer(b));
	   System.out.println("offer tow integers,the size is "+queue.getLen());
	   //队列已经满了，测试异常
	   try {//测试add
	   	System.out.println("add an integer:");
	   	a=input.nextInt();
		queue.add(new Integer(a));
	} catch (IllegalStateException e) {
		System.out.println("add-test:IllegalStateException:the queue is full...");
	}
	//测试offer
	   System.out.println("offer an integer:");
	   a=input.nextInt();
	if(!queue.offer(new Integer(a)))
			System.out.println("offer test:the queue is full...");
	System.out.println("clear the queue:");
	while(!queue.isEmp())
		System.out.print(queue.poll()+" ");
	System.out.println(" ");
	//测试poll
	if(queue.poll()==null)
		System.out.println("poll test:the queue is empty...");
	//测试peek
	if(queue.m_peek()==null)
		System.out.println("peek test:the queue is empty...");
	//测试remove
	try{
		queue.remove();
	} catch (NoSuchElementException e){
		System.out.println("remove-test:NoSuchElementException:the queue is full...");
	}
	//测试element
	try {
		queue.element();
	} catch (NoSuchElementException e) {
		System.out.println("element-test:NoSuchElementException:the queue is full...");
	}
	input.close();
   }
}