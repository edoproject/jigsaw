package com.jigsaw.initializers

import hudson.Plugin

class Initializer {
    protected Plugin plugin

    Initializer(Plugin plugin = null){
        this.plugin = plugin
    }

    void pre(){
        println("Preparing to initialize plugin": plugin.getClass().getCanonicalName())
    }

    void init(Map env) {
        println("Initializing plugin": plugin.getClass().getCanonicalName())
    }

    void start(Map env) {
        println("Starting plugin": plugin.getClass().getCanonicalName())
    }

    void post(){
        println("After initializing plugin": plugin.getClass().getCanonicalName())
    }
}