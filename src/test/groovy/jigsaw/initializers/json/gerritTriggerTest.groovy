package jigsaw.initializers.json

import com.jigsaw.initializers.Initializer
import com.jigsaw.initializers.json.GerritTrigger
import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer
import com.sonyericsson.hudson.plugins.gerrit.trigger.JenkinsAwareGerritHandler
import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.IGerritHudsonTriggerConfig
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.PluginConfig
import com.sonymobile.tools.gerrit.gerritevents.GerritHandler
import com.sonymobile.tools.gerrit.gerritevents.GerritSendCommandQueue
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import test.common.WithPlugins

import static junit.framework.Assert.assertEquals

class gerritTriggerTest {
    @Rule
    public JenkinsRule jenkins = new JenkinsRule()


    class WonderfulPlugin extends com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl {
        private static final Logger logger = LoggerFactory.getLogger(PluginImpl.class)
        static private GerritHandler gerritEventManager
        private transient volatile boolean active = false

        @Override
        void save() {

        }

        @Override
        public void start() throws Exception {

            logger.info("Starting Wonderful Gerrit-Trigger Plugin")
            PluginConfig cfg = new PluginConfig()
            cfg.setNumberOfReceivingWorkerThreads(1)
            cfg.setNumberOfSendingWorkerThreads(1)
            GerritSendCommandQueue.initialize(cfg)
            gerritEventManager = new JenkinsAwareGerritHandler(cfg.getNumberOfReceivingWorkerThreads())
            for (GerritServer s : servers) {
                s.start()
            }
            active = true
        }

        @Override
        public boolean isActive() {
            return active
        }
    }

    Map env = [
            'JENKINS_INIT': 'src/test/resources/'
    ]

    @Test
    @WithPlugins("gerrit-trigger.hpi,build-failure-analyzer.hpi,groovy-events-listener-plugin.hpi")
    void testInitializerGerritTrigger() {
        PluginImpl plugin = new WonderfulPlugin()
        plugin.start()

        Initializer initializer = new GerritTrigger(plugin)
        initializer.init(env)

        List<GerritServer> gerritServers = plugin.getServers()
        IGerritHudsonTriggerConfig config = gerritServers[0].getConfig()
        assertEquals("gerrit_not_default review <CHANGE>,<PATCHSET> --message 'Build Started <BUILDURL> <STARTED_STATS>' --verified <VERIFIED> --code-review <CODE_REVIEW> --label WaitForVerification=-1 --notify NONE",
                config.getGerritCmdBuildStarted())
    }
}
