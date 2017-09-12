package com.jigsaw

import com.jigsaw.initializers.Initializer

class Jigsaw {
    private List<Initializer> initializers
    private Map env

    Jigsaw(List<Initializer> initializers, Map env) {
        this.initializers = initializers
        this.env = env
    }

    void init() {
        for (Initializer initializer in initializers) {
            Thread.start {
                try {
                    sleep 1000
                    initializer.pre()
                    initializer.init(env)
                    initializer.start(env)
                    initializer.post()
                } catch (Exception e) {
                    println e.getMessage()
                    e.printStackTrace()
                    System.exit(1)
                }
            }
        }
    }
}
