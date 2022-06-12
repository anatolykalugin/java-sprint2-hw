package com.practicum.kvserver;

import java.io.IOException;

public class KVMain {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }
}
