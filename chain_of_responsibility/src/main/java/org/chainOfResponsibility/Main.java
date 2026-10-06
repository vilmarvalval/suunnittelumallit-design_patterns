package org.chainOfResponsibility;

public class Main {
    public static void main(String[] args){
        Handler primaryHandler;

        Handler compensationHandler = new CompensationHandler();
        Handler requestHandler = new RequestHandler();
        Handler suggestionHandler = new SuggestionHandler();
        Handler feedbackHandler = new FeedbackHandler();
        compensationHandler.setNextHandler(requestHandler);
        requestHandler.setNextHandler(suggestionHandler);
        suggestionHandler.setNextHandler(feedbackHandler);
        primaryHandler = compensationHandler;

        Message compensation = new Message(Message.Type.compensation, "give me 500 bucks", "abcd.efg3@gmeil.com");
        Message request = new Message(Message.Type.request, "hi", "a@mail.fin");
        Message suggestion = new Message(Message.Type.suggestion, "cam we please exit the app", "nail@enamel.dom");
        Message feedback = new Message(Message.Type.feedback, "Can we have pizza?", "john.doe@netscape.gov");

        primaryHandler.process(compensation);
        primaryHandler.process(request);
        primaryHandler.process(suggestion);
        primaryHandler.process(feedback);
    }
}