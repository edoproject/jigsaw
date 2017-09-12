package jigsaw.initializers.json

import com.jigsaw.initializers.Initializer
import com.jigsaw.initializers.json.Gel
import net.sf.json.JSONObject
import org.jenkinsci.plugins.globalEventsPlugin.GlobalEventsPlugin
import org.junit.Test
import org.kohsuke.stapler.StaplerRequest

class gelTest extends GroovyTestCase {

    Map env = [
            'JENKINS_INIT': 'src/test/resources/'
    ]

    @Override
    protected void setUp() throws Exception {
        super.setUp()
    }

    @Test
    void testInitializerGEL1() {
        // disable load method...
        GlobalEventsPlugin.DescriptorImpl.metaClass.load = {}
        // disable configure method...
        JSONObject expectedFormData = [
                "onEventGroovyCode"                     : "println(\"Hello\")",
                "disableSynchronization"                : true,
                "GlobalEventsPlugin__start"             : true,
                "GlobalEventsPlugin__stop"              : true,
                "RunListener__onStarted"                : false,
                "RunListener__onCompleted"              : false,
                "RunListener__onFinalized"              : true,
                "RunListener__onDeleted"                : false,
                "ComputerListener__onLaunchFailure"     : false,
                "ComputerListener__onOnline"            : false,
                "ComputerListener__onOffline"           : false,
                "ComputerListener__onTemporarilyOnline" : false,
                "ComputerListener__onTemporarilyOffline": false,
                "QueueListener__onEnterWaiting"         : false,
                "QueueListener__onEnterBlocked"         : false,
                "QueueListener__onEnterBuildable"       : false,
                "QueueListener__onLeft"                 : false,
                "scheduleTime"                          : 0,
                "classPath"                             : "src/test/resources"
        ]

        GlobalEventsPlugin.DescriptorImpl.metaClass.configure = {
            StaplerRequest req, JSONObject formData ->
                assertEquals(expectedFormData, formData)
                true
        }

        GlobalEventsPlugin plugin = new GlobalEventsPlugin()
        Initializer initializer = new Gel(plugin)
        initializer.init(env)
    }


    @Test
    void testInitializerGEL2() {
        // disable load method...
        GlobalEventsPlugin.DescriptorImpl.metaClass.load = {}
        GlobalEventsPlugin plugin = new GlobalEventsPlugin()

        GlobalEventsPlugin.DescriptorImpl.metaClass.configure = {
            StaplerRequest req, JSONObject formData ->
                plugin.getDescriptor().update(formData)
        }

        Initializer initializer = new Gel(plugin)
        initializer.init(env)

        GlobalEventsPlugin.DescriptorImpl descriptor = plugin.getDescriptor()
        assertTrue(descriptor.isEventEnabled('RunListener.onFinalized'))
        assertFalse(descriptor.isEventEnabled('RunListener.onCompleted'))
        assertEquals("println(\"Hello\")", descriptor.getOnEventGroovyCode())
    }
}
