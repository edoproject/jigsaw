package jigsaw

import com.jigsaw.Jigsaw
import com.jigsaw.initializers.Initializer

import hudson.Plugin
import org.junit.Before
import org.junit.Test

class JigsawTest {
    List<Initializer> initializers = new ArrayList<Initializer>()

    Map env = [
            'JENKINS_INIT': 'src/test/resources/'
    ]

    class PluginTest1 extends Plugin {

    }

    class PluginTest2 extends Plugin {

    }

    @Before
    void setUp() {
        initializers.add(new Initializer(new PluginTest1()) {
            @Override
            void init(Map env) {
                println("Testing init of PluginTest1")
            }
        })
        initializers.add(new Initializer(new PluginTest2()) {
            @Override
            void init(Map env) {
                println("Testing init of PluginTest2")
            }
        })
    }

    @Test
    void testJigsaw() {
        setUp()
        Jigsaw jigsaw = new Jigsaw(initializers, env)
        jigsaw.init()
        sleep(4000)
    }
}
