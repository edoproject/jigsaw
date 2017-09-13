package jigsaw.initializers.json

import com.jigsaw.initializers.Initializer
import com.jigsaw.initializers.json.Bfa
import com.sonyericsson.jenkins.plugins.bfa.PluginImpl
import com.sonyericsson.jenkins.plugins.bfa.db.KnowledgeBase
import com.sonyericsson.jenkins.plugins.bfa.model.FailureCause
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import test.common.WithPlugins

import static junit.framework.Assert.*

class bfaTest {
    @Rule
    public JenkinsRule jenkins = new JenkinsRule()


    class WonderfulPlugin extends PluginImpl {
        void load() {

        }

        void save() {

        }
    }

    Map env = [
            'JENKINS_INIT': 'src/test/resources/'
    ]

    @Test
    @WithPlugins("build-failure-analyzer.hpi,groovy-events-listener-plugin.hpi")
    void testInitializerBFAKnowledgeBaseSetup() {
        PluginImpl plugin = new WonderfulPlugin()
        plugin.start()

        KnowledgeBase knowledgeBase = plugin.getKnowledgeBase()
        knowledgeBase.addCause(new FailureCause("name1", "description1"))
        knowledgeBase.addCause(new FailureCause("name2", "description2"))

        Initializer initializer = new Bfa(plugin)
        initializer.init(env)

        // Configure is swapping KnowledgeBase
        knowledgeBase = plugin.getKnowledgeBase()
        assertEquals(3, knowledgeBase.getCauses().size())
    }

    @Test
    @WithPlugins("build-failure-analyzer.hpi,groovy-events-listener-plugin.hpi")
    void testInitializerBFAPluginSetup() {
        PluginImpl plugin = new WonderfulPlugin()
        plugin.start()
        Initializer initializer = new Bfa(plugin)
        initializer.init(env)

        assertTrue(plugin.isGlobalEnabled())
        assertFalse(plugin.isDoNotAnalyzeAbortedJob())
        assertTrue(plugin.isGerritTriggerEnabled())
        assertTrue(plugin.isTestResultParsingEnabled())
        assertEquals('', plugin.getTestResultCategories())
        assertEquals(0, plugin.getMaxLogSize())
        assertEquals(3, plugin.getNrOfScanThreads())

    }
}
