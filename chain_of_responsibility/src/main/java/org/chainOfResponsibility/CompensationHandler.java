package org.chainOfResponsibility;

import java.util.Scanner;

public class CompensationHandler extends Handler{
    Scanner scan = new Scanner(System.in);
    @Override
    public void process(Message msg){
        if (msg.type()== Message.Type.compensation){
            System.out.println("CompensationHandler: Handling Compensation claim.");
            //compensation claims can be reviewed and approved or rejected
            System.out.println("\n["+msg.senderEmail()+"] has send the following compensation claim:");
            System.out.println(msg.content());
            System.out.println("\nApprove? y/N");
            System.out.println(scan.nextLine().equalsIgnoreCase("y")? "Approved.\n" : "Rejected.\n");
        } else {
            System.out.println("CompensationHandler: Passing msg:"+msg.type());
            super.process(msg);
        }
    }
}