package org.chainOfResponsibility;

public record Message(Type type, String content, String senderEmail) {
    public enum Type {compensation, request, suggestion, feedback}
}