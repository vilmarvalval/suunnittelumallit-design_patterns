package org.chainOfResponsibility;

import java.util.Scanner;

public class SuggestionHandler extends Handler{
    Scanner scan = new Scanner(System.in);
    @Override
    public void process(Message msg){
        if (msg.type()== Message.Type.suggestion){
            System.out.println("SuggestionHandler: Handling development suggestion.");
            // development suggestions can be logged and prioritized
            System.out.println("\n["+msg.senderEmail()+"] has send the following development suggestion:");
            System.out.println(msg.content());
            System.out.println("\nLog? Y/n");
            boolean ans = scan.nextLine().equalsIgnoreCase("n");
            System.out.println(ans? "Suggestion ignored." : "Suggestion logged." );
            if (!ans) {
                System.out.println("Prioritize? y/N");
                System.out.println(scan.nextLine().equalsIgnoreCase("y") ? "Suggestion prioritized." : "Not prioritized.");
            }
            System.out.println();
        } else {
            System.out.println("SuggestionHandler: Passing msg:"+msg.type());
            super.process(msg);
        }
    }
}