package com.example.mensa.dataclasses;

import java.util.Map;

public class PubSubMessage {
        public String data;
        Map<String, String> attributes;
        String messageId;
        String publishTime;
}
