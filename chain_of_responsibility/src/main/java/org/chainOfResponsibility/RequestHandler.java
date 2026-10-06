package org.chainOfResponsibility;

public class RequestHandler extends Handler{

    @Override
    public void process(Message msg){
        if (msg.type()== Message.Type.request){
            System.out.println("RequestHandler: Handling contact request.");
            // contact requests can be forwarded to the appropriate department
            System.out.println("RequestHandler: Forwarding contact request from ["+msg.senderEmail()+"] to an appropriate department...");
            System.out.println("(Contact request message: ["+msg.senderEmail()+"]: '"+msg.content()+"')");
        } else {
            System.out.println("RequestHandler: Passing msg:"+msg.type());
            super.process(msg);
        }
    }
}