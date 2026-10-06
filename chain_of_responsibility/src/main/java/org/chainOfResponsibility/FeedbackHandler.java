package org.chainOfResponsibility;

import java.util.Scanner;

public class FeedbackHandler extends Handler{
    Scanner scan = new Scanner(System.in);
    @Override
    public void process(Message msg){
        if (msg.type()== Message.Type.feedback){
            System.out.println("FeedbackHandler: Handling general feedback.");
            //general feedback can be analyzed and responded to
            System.out.println("\n["+msg.senderEmail()+"] has send the following feedback message:");
            System.out.println(msg.content());
            System.out.println("\nRespond? y/N");
            if (scan.nextLine().equalsIgnoreCase("y")){
                System.out.println("Write your response.");
                String ans = scan.nextLine();
                System.out.println("Response sent.\n");
            } else {
                System.out.println("Feedback ignored.\n");
            }
        } else {
            System.out.println("FeedbackHandler: Passing msg:"+msg.type());
            super.process(msg);
        }
    }
}