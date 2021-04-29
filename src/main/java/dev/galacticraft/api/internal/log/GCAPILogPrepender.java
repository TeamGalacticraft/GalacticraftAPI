package dev.galacticraft.api.internal.log;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory2;
import org.apache.logging.log4j.message.MessageFormatMessage;
import org.apache.logging.log4j.message.SimpleMessage;

public enum GCAPILogPrepender implements MessageFactory2 {
    INSTANCE;

    @Override
    public Message newMessage(Object message) {
        return new SimpleMessage("[GalacticraftAPI] " + message);
    }

    @Override
    public Message newMessage(String message) {
        return new SimpleMessage("[GalacticraftAPI] " + message);
    }

    @Override
    public Message newMessage(CharSequence charSequence) {
        return new SimpleMessage("[GalacticraftAPI] " + charSequence);
    }

    @Override
    public Message newMessage(String message, Object p0) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, p0);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, p0, p1);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, p0, p1, p2);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, p0, p1, p2, p3);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, p0, p1, p2, p3, p4);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public Message newMessage(String message, Object... params) {
        return new MessageFormatMessage("[GalacticraftAPI] " + message, params);
    }
}
