import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by hanxi on 25/10/2015.
 */
public class test {
    public static class ListNode {
       int val;
       ListNode next;
       ListNode(int x) { val = x; }
    }
    public static void main(String[] args){
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(1);
        System.out.println(isPalindrome(head));
    }

    public static boolean isPalindrome(ListNode head) {
        // Write your code here
        if(head==null) return true;
        if(head.next==null) return true;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode front = dummy;
        ListNode cur = dummy.next;
        front.next = null;
        while(cur!=null){
            //System.out.println(cur.val);
            ListNode nt = cur.next;
            cur.next = front;
            front = cur;
            cur = nt;
        }
        ListNode k = front;
        ListNode m = head;
        while(m!=null){
            System.out.println(k.val);
            k = k.next;
            System.out.println(m.val);
            m = m.next;
        }
        return cur == head;
    }
}
