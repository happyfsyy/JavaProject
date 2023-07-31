package thread;

/**
 * 测试Try，Catch，Finally。
 * 1. 在getIp1()方法中，如果在try中返回1，没有异常的情况下，最终得到的就是1
 */
public class TryTest {
    public static void main(String[] args){
        int ip=getIP1();
        System.out.println(ip);
    }
    static Integer getIP1(){
        try{
            return 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 2;
    }
}
